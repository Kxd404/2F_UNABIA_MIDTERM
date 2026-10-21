package com.unabia.ui;

import com.unabia.dao.ScenarioDao;
import com.unabia.model.ScenarioEntity;

import java.util.List;
import java.util.Scanner;

/**
 * Interactive console menu. No SQL or JDBC here - everything goes through the DAO.
 */
public class ScenarioMenu {

    private static final String LINE = "=".repeat(60);

    private final ScenarioDao dao = new ScenarioDao();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> checkInLuggage();
                case "2" -> viewCheckedLuggage();
                case "3" -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nInvalid choice. Please enter 1, 2, or 3.\n");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println(LINE);
        System.out.println("            AIRPORT LUGGAGE CLAIM REGISTRY");
        System.out.println(LINE);
        System.out.println("[1] Check-In Luggage");
        System.out.println("[2] View Checked Luggage");
        System.out.println("[3] Exit System");
        System.out.println(LINE);
    }

    private void checkInLuggage() {
        System.out.println("\n--- CHECK-IN LUGGAGE ---");

        String name = promptNonEmpty("Enter Passenger Name: ");
        String flight = promptNonEmpty("Enter Flight Number: ");
        double weight = promptWeight("Enter Luggage Weight (KG): ");
        String status = promptStatus("Enter Status (CHECKED / LOADED / CLAIMED): ");

        // id = 0 placeholder; the database assigns the real id automatically
        ScenarioEntity entity = new ScenarioEntity(0, name, flight, weight, status);

        if (dao.insert(entity)) {
            System.out.println("\nSUCCESS: Baggage successfully checked in!\n");
        } else {
            System.out.println("\nERROR: Could not save the record.\n");
        }
    }

    private void viewCheckedLuggage() {
        List<ScenarioEntity> records = dao.findAll();

        System.out.println();
        System.out.println("=".repeat(63));
        System.out.println("                    CHECKED LUGGAGE LOGS");
        System.out.println("=".repeat(63));

        String border = "+----+----------------------+-----------+-----------+-----------+";
        System.out.println(border);
        System.out.printf("| %-2s | %-20s | %-9s | %-9s | %-9s |%n",
                "ID", "PASSENGER NAME", "FLIGHT NO", "WEIGHT_KG", "STATUS");
        System.out.println(border);

        if (records.isEmpty()) {
            System.out.printf("| %-59s |%n", "No records found.");
        } else {
            for (ScenarioEntity r : records) {
                System.out.printf("| %-2d | %-20s | %-9s | %-9.1f | %-9s |%n",
                        r.id(), r.passengerName(), r.flightNo(), r.weightKg(), r.status());
            }
        }

        System.out.println(border);
        System.out.println("Total Records Displayed: " + records.size());
        System.out.println();
    }

    // ---------- input helpers (all read with nextLine so no stray newline is left) ----------

    private String promptNonEmpty(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    private double promptWeight(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim();
            try {
                double weight = Double.parseDouble(value);
                if (weight > 0) {
                    return weight;
                }
                System.out.println("Weight must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private String promptStatus(String message) {
        while (true) {
            System.out.print(message);
            String value = scanner.nextLine().trim().toUpperCase();
            if (value.equals("CHECKED") || value.equals("LOADED") || value.equals("CLAIMED")) {
                return value;
            }
            System.out.println("Status must be CHECKED, LOADED, or CLAIMED.");
        }
    }
}
