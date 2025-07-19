package com.bauto.mqttgateway.model;

public class ModelDataSupply {

    private static String[] locationCodes = new String[] { "ZONE-A", "ZONE-B", "ZONE-C", "ZONE-X" };

    public static final String GetLocation() {
        return locationCodes[(int) Math.abs(Math.random() * 4)];
    }

    public static final String GetLeadTime() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        int daysToAdd = (int) (Math.random() * 365); // Random days up to a year
        int hoursToAdd = (int) (Math.random() * 24); // Random hours
        int minutesToAdd = (int) (Math.random() * 60); // Random minutes

        java.time.LocalDateTime futureDateTime = now.plusDays(daysToAdd)
                .plusHours(hoursToAdd)
                .plusMinutes(minutesToAdd)
                .withSecond(0);

        return futureDateTime.format(java.time.format.DateTimeFormatter.ISO_DATE_TIME) + "Z";
    }

    public static final int GetQuantity() {
        return (int) (Math.random() * 1000);
    }

    public static final int GetTad() {
        return (int) (Math.random() * 100);
    }

    public static final String GetAddNote() {
        return "Random Note: " + Math.random();
    }

    public static final String getPartNumber() {
        return "PN-00" + (int) Math.random() * 99;
    }
}
