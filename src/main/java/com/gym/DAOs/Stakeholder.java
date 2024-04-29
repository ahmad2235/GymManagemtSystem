package com.gym.DAOs;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Entity
@DiscriminatorValue("Stakeholder")
public class Stakeholder extends Employees {
    static List<Stakeholder> employees = new LinkedList<>();
    //@Column(name = "username", columnDefinition = "VARCHAR(255) DEFAULT 'firstName'")
//    private String userName;

    public Stakeholder(String firstName, String lastName, String gender, String password, String userName) {
        super(firstName, lastName, gender, password, userName);
        //this.serialNumber = getNextSerialNumber();
        this.id = getId();

    }

    public Stakeholder() {
        this("", "", "", "", ""); // Call the parameterized constructor with default values
    }

    public static void addEmployee() {

        Session session = null;
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory()) {
            session = factory.openSession();

            System.out.println("add employees: ");
            Scanner scanner = new Scanner(System.in);
            char x = 0;

            do {
                session.clear();
                // Create a session
                // Begin a transaction
                Stakeholder stakeholder = new Stakeholder();
                session.beginTransaction();
                System.out.print("first name: ");
                stakeholder.setFirstName(scanner.next());
                System.out.print("last name: ");
                stakeholder.setLastName(scanner.next());
                System.out.println("user name: ");
                stakeholder.setUserName(stakeholder.validateUserName(scanner.next()));
                System.out.print("password: ");
                stakeholder.setPassword(stakeholder.validatePassword(scanner.next()));
                // Check if an employee with the same first name, last name, and password exists
                Query query = session.createQuery("FROM Employees WHERE firstName = :firstName AND lastName = :lastName AND password = :password");
                query.setParameter("firstName", stakeholder.getFirstName());
                query.setParameter("lastName", stakeholder.getLastName());
                query.setParameter("password", stakeholder.getPassword());

                List<Employees> existingEmployees = query.getResultList();
                System.out.println(existingEmployees);
                if (!existingEmployees.isEmpty()) {
                    System.out.println("employee already exists.");

                    session.getTransaction().rollback();
                    continue;
                }
                System.out.println("gender ");
                stakeholder.setGender(scanner.next());
                employees.add(stakeholder);
                System.out.println("Press 'E' to exit or any other key to continue inserting employees: ");
                x = scanner.next().toUpperCase().charAt(0);
                session.save(stakeholder);
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

        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }



    public String getId() {

        return "STK-" + Integer.toString((int) (Math.random() * 1000));
    }

    public void print() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            // Load employees from the database into the LinkedList
            employees = (List<Stakeholder>) session.createQuery("FROM Employees").list();
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
        return "Stakeholders{" +
                "id='" + id + '\'' +
                ", serialNumber=" + serialNumber +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", gender='" + getGender() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", userName='" + getUserName() + '\'' +
                '}' + '\n';
    }
}
