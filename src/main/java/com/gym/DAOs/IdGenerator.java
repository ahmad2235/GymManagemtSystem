package com.gym.DAOs;

public class IdGenerator {

    public static String EmployeeId(Employees employee) {
        return "EMP-" + employee.getFirstName().substring(0, 3).toUpperCase() +
                employee.getLastName().substring(0, 3).toUpperCase() +
                "-" + generateRandom();
    }
/*
    public static String StakeholderId(Stakeholder stakeholder) {
        return "STK-" + stakeholder.getCompanyName().substring(0, 3).toUpperCase() +
                "-" + generateRandom();
    }

    public static String ClientId(ClientDAO client) {
        return "CLI-" + client.getBusinessType().substring(0, 3).toUpperCase() +
                "-" + generateRandom();
    }
*/
    private static String generateRandom() {
        return Integer.toString((int) (Math.random() * 1000));
    }

}
