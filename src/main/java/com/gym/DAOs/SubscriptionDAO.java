package com.gym.DAOs;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Entity
public class SubscriptionDAO {
    private static List<SubscriptionDAO> subcribedSports = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int numberOfSports;
    private LocalDate date;
    private float totalAmount;
    @ManyToOne
    @JoinColumn(name = "clientDAO_id")
    private ClientDAO clientDAO;
    @ManyToOne
    @JoinColumn(name = "cardDAO_id")
    private CardDAO cardDAO;

    public SubscriptionDAO() {
        this.date = LocalDate.now();
        this.setCardDAO(cardDAO);
        this.setTotalAmount(totalAmount);
        this.setNumberOfSports(numberOfSports);
    }

    public static List<SubscriptionDAO> getSubscribedSports() {
        return subcribedSports;
    }

    public static void setSubscribedSports(List<SubscriptionDAO> suscribedSports) {
        SubscriptionDAO.subcribedSports = suscribedSports;
    }

    public int getNumberOfSports() {
        return numberOfSports;
    }

    public void setNumberOfSports(int numberOfSports) {
        this.numberOfSports = numberOfSports;
    }

    public LocalDate getDate() {
        return date;
    }


    public CardDAO getCardDAO() {
        return cardDAO;
    }

    public void setCardDAO(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void addSubscribe() {
        try (SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
             Session session = factory.openSession()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Your card number: ");
            int cardNumberInput = scanner.nextInt();

            CardDAO.cards = session.createQuery("FROM CardDAO").list();
            SportsDAO.sports = session.createQuery("FROM SportsDAO").list();

            char exitChoice;
            boolean cardNumberFound = false;

            for (CardDAO cardDAO : CardDAO.cards) {
                if (cardNumberInput == cardDAO.getCardNumber()) {
                    cardNumberFound = true;

                    if (cardDAO.getEndDate().isAfter(LocalDate.now())) {
                        System.out.println(SportsDAO.sports);

                        do {
                            SubscriptionDAO subscriptionDAO = new SubscriptionDAO();

                            System.out.println("Enter the sport (serial number) you want to subscribe: ");
                            int sportIdInput = scanner.nextInt();

                            for (SportsDAO sportsDAO : SportsDAO.sports) {
                                if (sportIdInput == sportsDAO.getSportId() && sportsDAO.getNumOfPlaces() > 0) {
                                    session.beginTransaction();

                                    // Assuming subscribedSports is a list in SubscriptionDAO
                                    subcribedSports.add(subscriptionDAO);
                                    sportsDAO.setNumOfPlaces(sportsDAO.getNumOfPlaces() - 1);

                                    // Set relationships and save entities
                                    subscriptionDAO.setCardDAO(cardDAO);
                                    subscriptionDAO.setNumberOfSports(subscriptionDAO.getSubscribedSports().size());

                                    if (getSubscribedSports().size() == 2) {
                                        cardDAO.setSubscriberType("silver");
                                        setTotalAmount(500-cardDAO.calculateDiscount());
                                    } else if (getSubscribedSports().size() >= 3) {
                                        cardDAO.setSubscriberType("gold");
                                        setTotalAmount(500-cardDAO.calculateDiscount());
                                    }
                                    session.save(sportsDAO);
                                    session.save(subscriptionDAO);
                                    session.update(cardDAO);
                                    session.getTransaction().commit();
                                } else if (sportIdInput == sportsDAO.getSportId()) {
                                    System.out.println("No available places for the selected sport.");
                                }


                            }

                            System.out.println("Press any key to continue adding sports, or 'E' to exit: ");
                            exitChoice = scanner.next().toUpperCase().charAt(0);

                        } while (exitChoice != 'E');
                    } else {
                        System.out.println("The card is expired.");
                    }
                    break;
                }
            }

            if (!cardNumberFound) {
                System.out.println("Invalid card number.");
            }
            
        }
    }

}


