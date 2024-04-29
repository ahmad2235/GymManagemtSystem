package com.gym;

import com.gym.DAOs.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
//        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
//             Session session = factory.openSession()) {
//            List<Employees> employees = new LinkedList<>();
//            List<ClientDAO> clients = new LinkedList<>();
//            //initialize tables
//            Employees employeesDAO1 = new Employees();
//            Stakeholder stakeholderDAO = new Stakeholder();
//            SportsDAO sportDAO = new SportsDAO();
//            stakeholderDAO.addEmployee();
//            System.out.println("enter your password to add employees");
//            employees = (List<Employees>) session.createQuery("FROM Employees").list();
//            Scanner scanner1 = new Scanner(System.in);
//            String stakeholderPassword;
//            boolean rightPassword = false;
//            char w;
//            do {
//
//                stakeholderPassword = scanner1.next();
//                for (Employees employeesDAO : employees) {
//
//                    if (Objects.equals(stakeholderPassword, employeesDAO.getPassword())) {
//                        rightPassword = true;
//                        break;
//                    }
//                }
//                if (rightPassword) {
//                    employeesDAO1.addEmployee();
//                    sportDAO.addSport();
//                } else {
//                    System.out.println("wrong password try again");
//                }
//                System.out.println("press any key to keep inserting or E to exit");
//                w = scanner1.next().toUpperCase().charAt(0);
//            } while (w != 'E');
//            session.update(employees);
//
//            Stakeholder stakeholder = new Stakeholder();
//            SportsDAO sportsDAO = new SportsDAO();
//            ClientDAO clientDAO = new ClientDAO();
//            CardDAO cardDAO = new CardDAO();
//            SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
//            boolean valiable = false;
//            System.out.println(employees);
//
//            System.out.println("enter employee password and username to add client");
//
//            Scanner scanner = new Scanner(System.in);
//            char z;
//            do {
//                System.out.println("username");
//                String valiableUsername = scanner.next();
//                System.out.println("password");
//                String valiablePassword = scanner.next();
//                scanner.nextLine();
//
//                for (Employees employeesDAO : employees) {
//                    if (Objects.equals(valiablePassword, employeesDAO.getPassword()) &&
//                            Objects.equals(valiableUsername, employeesDAO.getUserName())) {
//                        valiable = true;
//                    }
//                }
//                if (valiable) {
//                    System.out.println("you can add client");
//                    clientDAO.addClient();
//                    subscriptionDAO.addSubscribe();
//                } else {
//                    System.out.println("wrong password or username press any key to  try again");
//                }
//
//                z = scanner.next().toUpperCase().charAt(0);
//            } while (z != 'E');
//            System.out.println(clients);
//
//        }


        Employees employee=new Employees();
        Stakeholder stakeholder=new Stakeholder();
        //Stakeholder.addEmployee();
        //Employees.addEmployee();
        employee.deleteEmployee();
        //employee.print();
    }

}