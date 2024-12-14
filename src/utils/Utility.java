package utils;

import java.util.Random;
import java.util.Scanner;

public class Utility {

    public static void welcomePage() {
        Utility.clearScreen(0);
        System.out.println("---------------------Welcome to Wellness Tracker---------------------");
        System.out.println("A place to monitor your wellness!");
        System.out.println("Your meals, sleep, and workout are all stored here!");
        System.out.println("---------------------------------------------------------------------");
    }

    public static void clearScreen(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
            System.out.println("\033[H\033[2J");
            System.out.flush();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public static void pauseScreen(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public static void pauseClearScreen(Scanner scanner, int seconds) {
        System.out.print("\nPress ENTER to continue...");
        scanner.nextLine();
        clearScreen(seconds);
    }

    //Prints a motivational quote before terminating the program
    public static void programExit() {
        clearScreen(1);
        System.out.println();
        System.out.println("A quote to inspire you before you go.");
        System.out.println();
        
        WellnessQuotes.Quote[] quotes = WellnessQuotes.Quote.values();
        Random random = new Random();
        int index = random.nextInt(quotes.length);
        WellnessQuotes.Quote randomQuote = quotes[index];

        System.out.println("\"" + randomQuote.getText() + "\"");
        System.out.println("- " + randomQuote.getAuthor());

        System.out.println();
        System.out.println("THANK YOU FOR USING THE PROGRAM.");
        }


    //Password must not be below 8 characters
    public static void validateUsername(String username, String fieldName) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        } 
        if (username.length() < 8) {
            throw new IllegalArgumentException(username + " length is below 8 characters. Please try again.");
        }
    } 

    //Password must not be below 8 characters
    //Password must contain atleast one integer
    public static void validatePassword(String password, String fieldName) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        } 
        if (password.length() < 8) {
            throw new IllegalArgumentException(fieldName + " length is below 8 characters. Please try again.");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException(password + " as a password does not contain any integers. Please include atleast one integer.");
        }
    }
    
      //Text cannot be null or empty
    public static void validateText(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }

    //Integer value cannot be zero or negative
    public static void validateInt(int number, String fieldName) {
        if (number <= 0) {
            throw new IllegalArgumentException(fieldName + " cannot be zero or negative");
        }
    }

    //Double value cannot be zero or negative
    public static void validateDouble(double number, String fieldName) {
        if (number <= 0) {
            throw new IllegalArgumentException(fieldName + " cannot be zero or negative");
        }
    }

}
