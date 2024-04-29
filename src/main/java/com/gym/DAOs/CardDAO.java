package com.gym.DAOs;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Entity
public class CardDAO {
    static List<CardDAO> cards = new LinkedList<>();

    private final LocalDate startDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cardNumber;

    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    @Temporal(TemporalType.DATE)
    private String subscriberType;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL)
    private ClientDAO client;


    public CardDAO() {
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now().plusMonths(1); // Initialize endDate to current date
    }

    public CardDAO(int cardNumber) {
        this.startDate = LocalDate.now();
        this.cardNumber = getNextCardNumber();
        this.setEndDate(endDate);
    }

    protected static int getNextCardNumber() {
        if (cards.isEmpty()) {
            return 1;
        } else {
            return cards.stream()
                    .mapToInt(CardDAO::getCardNumber)
                    .max()
                    .orElse(1) + 1;
        }
    }

    public String getSubscriberType() {
        return subscriberType;
    }

    public void setSubscriberType(String subscriberType) {
        this.subscriberType = subscriberType;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ClientDAO getClient() {
        return client;
    }

    public void setClient(ClientDAO client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "CardDAO{" +
                "startDate=" + startDate +
                ", cardNumber=" + cardNumber +
                ", endDate=" + endDate +
                ", subscriberType='" + subscriberType + '\'' +
                '}';
    }

    public void createCard() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            session.beginTransaction();

            CardDAO cardDAO = new CardDAO();

            System.out.println("Set period of subscription (in months): ");
            int subscriptionMonths = new Scanner(System.in).nextInt();

            // Set the end date based on the input period
            cardDAO.setEndDate(cardDAO.getStartDate().plusMonths(subscriptionMonths));

            // Update the endDate field in the CardDAO instance
            session.save(cardDAO);

            session.getTransaction().commit();
        }
    }


    public float calculateDiscount() {
        switch (subscriberType.toLowerCase()) {
            case "silver":
                return 0.10f; // 10% discount
            case "gold":
                return 0.15f; // 15% discount
            default:
                return 0.0f; // No discount for other types
        }
    }

    public void print() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            cards = (List<CardDAO>) session.createQuery("FROM CardDAO").list();
            if (cards.isEmpty()) {
                System.out.println("no cards to print");
            } else {
                System.out.println("current cards");
                System.out.println(cards);
            }
        }
    }

//    public void deletCard() {
//        try (SessionFactory factory = new Configuration().configure("hiberbnate.cfg.xml").buildSessionFactory();
//             Session session = factory.openSession()) {
//
//        }
//        }
    }

