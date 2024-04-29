package com.gym;

import com.gym.DAOs.Employees;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Scanner;

public class PasswordHashingExample {

    public static void main(String[] args) {
        String password = "myPassword123";

        // Generate a salt
        String salt = BCrypt.gensalt();

        // Hash the password with the salt
        String hashedPassword = BCrypt.hashpw(password, salt);

        System.out.println("Salt: " + salt);
        System.out.println("Hashed Password: " + hashedPassword);

        // Verify a password
        String inputPassword = "myPassword123";
        if (BCrypt.checkpw(inputPassword, hashedPassword)) {
            System.out.println("Password Matched");
        } else {
            System.out.println("Password Not Matched");
        }
    }
}

//    public static void addEmployee() {
//
//        Session session = null;
//        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory()) {
//            session = factory.openSession();
//            //to see what employees we have before inserting
//            //print();
//            System.out.println("add employees: ");
//            Scanner scanner = new Scanner(System.in);
//            char x = 0;
//
//            do {
//                session.clear();
//                // Create a session
//                // Begin a transaction
//                Employees employees = new Employees();
//                session.beginTransaction();
//                System.out.print("first name: ");
//                employees.setFirstName(scanner.nextLine());
//                System.out.print("last name: ");
//                employees.setLastName(scanner.nextLine());
//                System.out.print("user name: ");
//                employees.setUserName(employees.validateUserName(scanner.nextLine()));
//                System.out.print("password: ");
//                String checkingPassword = employees.validatePassword(scanner.nextLine());
//
//                // Check if an employee with the same username and password exists
//                Query query = session.createQuery("FROM Employees WHERE userName = :userName");
//                query.setParameter("userName", employees.userName);
//
//                List<Employees> existingEmployees = query.getResultList();
//                System.out.println(existingEmployees);
//
//                if (!existingEmployees.isEmpty()) {
//                    for (Employees existingEmployee : existingEmployees) {
//                        if (BCrypt.checkpw(checkingPassword, existingEmployee.getPassword())) {
//                            System.out.println("employee already exists! please try again.");
//                            break;
//
//                        }
//                    }
//                    session.getTransaction().rollback();
//                    continue;
//                }
//                // continue adding the employee
//                employees.setPassword(BCrypt.hashpw(checkingPassword, BCrypt.gensalt()));
//                System.out.println("gender ");
//                employees.setGender(scanner.nextLine());
//                Employees.employees.add(employees);
//                System.out.println("Press 'E' to exit or any other key to continue inserting employees: ");
//                x = scanner.next().toUpperCase().charAt(0);
//                session.save(employees);
//                session.getTransaction().commit();
//                System.out.println("a new Employee is added successfully!");
//            }
//            while (x != 'E');
//
//        } catch (Exception exception) {
//            if (session != null && session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            exception.printStackTrace();
//            System.out.println("something wrong happened please try again");
//            //addEmployee();
//        } finally {
//            if (session != null && session.isOpen()) {
//                session.close();
//            }
//        }
//    }


//    public void deleteEmployee() {
//        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
//             Session session = factory.openSession()) {
//            Employees employeeToRemove = null;
//            Scanner scanner = new Scanner(System.in);
//            session.beginTransaction();
//            // Load employees from the database into the LinkedList
//            employees = (List<Employees>) session.createQuery("FROM Employees").list();
//
//            // Print the current employees with their serial numbers
//            System.out.println("Current employees:");
//            for (Employees employee : employees) {
//                System.out.println("Serial Number: " + employee.getSerialNumber());
//            }
//            if (employees.isEmpty()) {
//                System.out.println("there is no employees to delete");
//                return;
//
//            }
//            System.out.println("Delete an employee with serial number: ");
//            int x = scanner.nextInt();
//
//            for (Employees employee : employees) {
//                if (x == employee.getSerialNumber()) {
//                    // Found the employee, mark it for removal
//                    employeeToRemove = employee;
//                    break;
//                }
//            }
//
//            if (employeeToRemove != null) {
//                // we don't remove the object from the list because hibernate handle it after committing session
//                session.delete(employeeToRemove);
//                System.out.println("An employee has been removed");
//                session.getTransaction().commit();
//            } else {
//                System.out.println("Invalid serial number or employee not found");
//            }
//        }
//    }