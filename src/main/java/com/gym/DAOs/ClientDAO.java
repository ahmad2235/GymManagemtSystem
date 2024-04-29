package com.gym.DAOs;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Entity
public class ClientDAO {
    static List<ClientDAO> clients = new LinkedList<>();
    static List<SportsDAO> subscribedSports = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int serialNumber;
    @OneToMany(mappedBy = "clientDAO", cascade = CascadeType.ALL)
    private List<SubscriptionDAO> subscriptions = new ArrayList<>();
    private String id;
    private String firstName;
    private String lastName;
    private int age;
    private float weight;
    private float height;
    private int CardNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_number")
    private CardDAO card;
    public ClientDAO() {
        this.serialNumber = getNextSerialNumber();
        this.id = getId();
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAge(age);
        this.setHeight(height);
        this.setWeight(weight);
        this.setCard(card);

    }

    protected static int getNextSerialNumber() {
        if (clients.isEmpty()) {
            return 1;
        } else {
            return clients.stream()
                    .mapToInt(ClientDAO::getSerialNumber)
                    .max()
                    .orElse(1) + 1;
        }
    }

    public List<SubscriptionDAO> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionDAO> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public int getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(int cardNumber) {
        CardNumber = cardNumber;
    }

    public CardDAO getCard() {
        return card;
    }

    public void setCard(CardDAO card) {
        this.card = card;
    }


    @Override
    public String toString() {
        return "ClientDAO{" +
                "serialNumber=" + serialNumber +
                ", id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                "\n\t" +
                ", card=" + card +
                '}' + '\n';
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public String getId() {

        return "CLI-" + Integer.toString((int) (Math.random() * 1000));
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void addClient() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            Scanner scanner = new Scanner(System.in);
            char x;
            do {
                session.clear();
                session.beginTransaction();

                // Create a CardDAO instance
                SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
                CardDAO cardDAO = new CardDAO();
                session.save(cardDAO); // Save the cardDAO to generate an ID

                ClientDAO clientDAO = new ClientDAO();
                System.out.println("first name: ");
                clientDAO.setFirstName(scanner.next());
                System.out.println("last name: ");
                clientDAO.setLastName(scanner.next());
                System.out.println("age: ");
                clientDAO.setAge(scanner.nextInt());
                System.out.println("height: ");
                clientDAO.setHeight(scanner.nextFloat());
                System.out.println("weight: ");
                clientDAO.setWeight(scanner.nextFloat());

                // Set the card for the client
                clientDAO.setCard(cardDAO);

                // Set the cardNumber for the client
                clientDAO.setCardNumber(cardDAO.getCardNumber());
                subscriptions.add(subscriptionDAO);
                clients.add(clientDAO);

                System.out.println("press E to exit or any other key to keep inserting");
                x = scanner.next().toUpperCase().charAt(0);
                session.save(clientDAO);
                session.getTransaction().commit();
            } while (x != 'E');
        }
    }


    public void deleteClient() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            ClientDAO clientToRemove = null;
            Scanner scanner = new Scanner(System.in);
            session.beginTransaction();
            clients = (List<ClientDAO>) session.createQuery("FROM ClientDAO").list();
            System.out.println("current clients");
            for (ClientDAO client : clients) {
                System.out.println("serial number: " + client.getSerialNumber());

            }
            if (clients.isEmpty()) {
                System.out.println("there is no clients to delete");
                return;
            }
            System.out.println("delete a client with serial number: ");
            int x = scanner.nextInt();
            for (ClientDAO client : clients) {
                if (x == client.getSerialNumber()) {
                    clientToRemove = client;
                    break;
                }
            }
            if (clientToRemove != null) {
                session.delete(clientToRemove);
                System.out.println("a client has been removed");
                session.getTransaction().commit();
            } else {
                System.out.println("invalid serial number ");
            }
        }
        print();
    }

    public void print() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            // Load employees from the database into the LinkedList
            clients = (List<ClientDAO>) session.createQuery("FROM ClientDAO").list();
            // Print the current employees
            if (clients.isEmpty()) {
                System.out.println("no clients to print");
            } else {
                System.out.println("Current clients: ");
                System.out.println(clients);
            }
        }
    }


}

