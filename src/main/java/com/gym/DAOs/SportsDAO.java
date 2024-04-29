package com.gym.DAOs;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static jakarta.persistence.GenerationType.AUTO;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
public class SportsDAO {

    static List<SportsDAO> sports = new LinkedList<>();
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int sportId;
    private String sportName;
    private int roomNum;
    private int numOfPlaces;
    private String coachName;
    private String timing;

    public SportsDAO() {
        //this.sportId = getNextId();
        this.setSportName(sportName);
        this.setCoachName(coachName);
        this.setNumOfPlaces(numOfPlaces);
        this.setRoomNum(roomNum);
    }


    public int getSportId() {
        return sportId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public int getNumOfPlaces() {
        return numOfPlaces;
    }

    public void setNumOfPlaces(int numOfPlaces) {
        this.numOfPlaces = numOfPlaces;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public void addSport() {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        try (Session session = factory.openSession()) {
            Scanner scanner = new Scanner(System.in);
            char x;
            do {
                session.clear();
                SportsDAO sportsDAO = new SportsDAO();
                session.beginTransaction();
                System.out.println("sport name");
                sportsDAO.setSportName(scanner.next());
                System.out.println("room number");
                sportsDAO.setRoomNum(scanner.nextInt());

                scanner.nextLine();

                System.out.println("coach name");
                sportsDAO.setCoachName(scanner.nextLine());
                System.out.println("number of places");
                sportsDAO.setNumOfPlaces(scanner.nextInt());
                scanner.nextLine();

                System.out.println("set timing ");

                // Consume the newline character after reading the integer
                sportsDAO.setTiming(scanner.nextLine());
                sports.add(sportsDAO);
                System.out.println("press E to exit or any other key to keep inserting");
                x = scanner.next().toUpperCase().charAt(0);
                session.save(sportsDAO);
                session.getTransaction().commit();
            } while (x != 'E');
        }
    }

    public void deleteSport() {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        try (Session session = factory.openSession()) {
            SportsDAO sportToRemove = null;
            Scanner scanner = new Scanner(System.in);
            session.beginTransaction();
            sports = (List<SportsDAO>) session.createQuery("FROM SportsDAO").list();
            for (SportsDAO sportsDAO : sports) {
                System.out.println("sport name" + sportsDAO.sportName + ", id: " + sportsDAO.sportId);
            }
            if (sports.isEmpty()) {
                System.out.println("there is no sports to remove");
                return;
            }
            System.out.println("delete sport with id: ");
            int x = scanner.nextInt();
            for (SportsDAO sportsDAO : sports) {
                if (x == sportsDAO.getSportId()) {
                    sportToRemove = sportsDAO;
                    break;
                }
            }
            if (sportToRemove != null) {
                //we don't remove it from the list because hibernate manages it after committing session
                session.delete(sportToRemove);
                System.out.println("a sport has been removed");
            } else {
                System.out.println("invalid id ");
            }
            session.getTransaction().commit();
        }
    }


    public void print() {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        try (Session session = factory.openSession()) {
            sports = (List<SportsDAO>) session.createQuery("FROM SportsDAO").list();
            if (sports.isEmpty()) {
                System.out.println("there is no sports to show");

            } else {
                System.out.println("current sports");
                System.out.println(sports);
            }

        }
    }

    @Override
    public String toString() {
        return "SportsDAO{" +
                "sportId=" + sportId +
                ", sportName='" + sportName + '\'' +
                ", roomNum=" + roomNum +
                ", numOfPlaces=" + numOfPlaces +
                ", coachName='" + coachName + '\'' +
                ", timing='" + timing + '\'' +
                '}' + '\n';
    }
}
