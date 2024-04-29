package com.gym.DAOs;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "employee_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Employee")
public class Employees {
    static List<Employees> employees = new LinkedList<>();

    protected String id;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "serialNumber")
    protected int serialNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private String password;
    private String userName;

    public Employees(String firstName, String lastName, String gender, String password, String userName) {
        //this.serialNumber = getNextSerialNumber();
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setGender(gender);
        this.setUserName(userName);
        this.setPassword(password);
        this.id = getId();
    }

    public Employees() {
        this("", "", "", "", "");
    }

    public static void addEmployee() {

        Session session = null;
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory()) {
            session = factory.openSession();
            //to see what employees we have before inserting
            //print();
            System.out.println("add employees: ");
            Scanner scanner = new Scanner(System.in);
            char x = 0;

            do {
                session.clear();
                // Create a session
                // Begin a transaction
                Employees employees = new Employees();
                session.beginTransaction();
                System.out.print("first name: ");
                employees.setFirstName(scanner.nextLine());
                System.out.print("last name: ");
                employees.setLastName(scanner.nextLine());
                System.out.print("user name: ");
                employees.setUserName(employees.validateUserName(scanner.nextLine()));
                System.out.print("password: ");

                // Check if an employee with the same username
                Query query = session.createQuery("FROM Employees WHERE userName = :userName");
                query.setParameter("userName", employees.userName);

                List<Employees> existingEmployees = query.getResultList();
                System.out.println(existingEmployees);

                if (!existingEmployees.isEmpty()) {
                    System.out.println("username already already exists! please try again.");
                    session.getTransaction().rollback();
                    continue;
                }
                // continue adding the employee
                employees.setPassword(employees.validatePassword(scanner.nextLine()));
                System.out.println("gender ");
                employees.setGender(scanner.nextLine());
                Employees.employees.add(employees);
                System.out.println("Press 'E' to exit or any other key to continue inserting employees: ");
                x = scanner.next().toUpperCase().charAt(0);
                session.save(employees);
                session.getTransaction().commit();
                System.out.println("a new Employee is added successfully!");
            }
            while (x != 'E');

        } catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            exception.printStackTrace();
            System.out.println("something wrong happened please try again");
            //addEmployee();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    protected String validateUserName(String userName) {
        Scanner scanner = new Scanner(System.in);
        while (userName.contains(" ")) {
            System.out.println("user name cannot contains spaces! please try again.");
            userName = scanner.nextLine();
        }
        return userName;
    }
    //to generate a serial number before the database
//    protected static int getNextSerialNumber() {
//        if (employees.isEmpty()) {
//            return 1;
//        } else {
//            return employees.stream()
//                    .mapToInt(Employees::getSerialNumber)
//                    .max()
//                    .orElse(1) + 1;
//        }
//    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public String getId() {
        return "EMP-" + (int) (Math.random() * 1000);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;

    }

    protected String validatePassword(String password) {
        Scanner scanner = new Scanner(System.in);
        while (password.length() < 4) {
            System.out.println("Password must be at least 4 characters.");
            password = scanner.next();
        }
        return password;
    }

    public void deleteEmployee() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            session.beginTransaction();

            Scanner scanner = new Scanner(System.in);
            boolean validSerialNumber = false;
            int serialNumber;

            while (!validSerialNumber) {
                System.out.println("Enter the serial number of the employee you want to delete:");
                try {
                    serialNumber = Integer.parseInt(scanner.nextLine());
                    if (serialNumber > 0) {
                        validSerialNumber = true;
                        Employees employee = session.get(Employees.class, serialNumber);
                        if (employee != null) {
                            session.delete(employee);
                            System.out.println("Employee with serial number " + serialNumber + " has been deleted.");
                        } else {
                            System.out.println("Employee with serial number " + serialNumber + " not found.");
                        }
                    } else {
                        System.out.println("Serial number must be greater than 0. Please enter a valid serial number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid serial number.");
                }
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong. Please try again.");
        }
    }

    public void findEmployee() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            employees = (List<Employees>) session.createQuery("FROM Employees").list();
            // Print the current employees with their serial numbers
            System.out.println("Current employees:");
            for (Employees employee : employees) {
                System.out.println("Serial Number: " + employee.getSerialNumber());
            }
            Scanner scanner = new Scanner(System.in);
            Employees employee;
            System.out.println("Find an employee with serial number: ");
            int x = scanner.nextInt();
            for (Employees value : employees) {
                employee = value;
                if (x == employee.getSerialNumber()) {
                    System.out.println(employee);
                }
            }
            System.out.println("invalid serial number");
        }
    }

    public void print() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            // Load employees from the database into the LinkedList
            employees = (List<Employees>) session.createQuery("FROM Employees").list();
            // Print the current employees
            if (employees.isEmpty()) {
                System.out.println("no employees to print");
            } else {
                System.out.println("Current employees:");
                System.out.println(employees);
            }
        }
    }

    @Override
    public String toString() {
        return "Employees{" +
                "id='" + id + '\'' +
                ", serialNumber=" + serialNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
