package main;

import java.util.Scanner;

import menu.HomeMenu;
import utils.Utility;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        WellnessDB.getConnection();
        HomeMenu homeMenu = new HomeMenu();


        //Welcome page
        boolean status = true;
        while (status) {
            try {
                Utility.welcomePage();
                System.out.println("1. Start");
                System.out.println("2. Exit");
                System.out.print("Enter choice: ");
                int start_choice = scanner.nextInt();

                switch (start_choice) {
                    case 1:
                        homeMenu.showHomeMenu();
                        break;
                    case 2:
                        Utility.programExit();
                        status = false;
                        scanner.close();
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose from 1 or 2 only.");
                        Utility.pauseClearScreen(scanner,0);
                }
            } catch (Exception e) {
                System.out.println("\nInvalid input. Please enter a number from the choices.");
                Utility.clearScreen(1);
            }
        }
    }
}
