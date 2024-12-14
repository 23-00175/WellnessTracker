package menu;

import java.util.Scanner;

import profile.Profile;
import profile.ProfileManager;
import tracker.TrackerManager;
import users.Users;
import utils.Utility;

public class LoggedInMenu {
    private Scanner scanner;
    private ProfileManager profileManager;
    private TrackerManager trackerManager;
    private Users users;

    public LoggedInMenu() {
        this.scanner = new Scanner(System.in);
        this.profileManager = new ProfileManager();
        this.trackerManager = new TrackerManager();
    }

    // Dashboard for logged-in user
    public void dashboard(Users user) {
        this.users = user;  
        Profile profile = profileManager.getProfile(user.getUser_id());
        HomeMenu homeMenu = new HomeMenu();
    
        boolean loggedInStatus = true;
        Utility.clearScreen(1);
    
        while (loggedInStatus) {
            System.out.println("Welcome, " + profile.getFirst_name() + " " + profile.getLast_name() + "!");
            System.out.println("1. Profile");
            System.out.println("2. Tracker");
            System.out.println("3. Logout (Home Menu)");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
    
            try {
                String input = scanner.nextLine();  
                int choice = Integer.parseInt(input);  
    
                switch (choice) {
                    case 1:
                        Utility.clearScreen(0);
                        profileManager.profileOptions(profile);
                        break;
                    case 2:
                        Utility.clearScreen(0);
                        trackerManager.showTracker(user); 
                        break;
                    case 3:
                        System.out.println("\nLogging out...");
                        this.users = null;
                        homeMenu.showHomeMenu();  
                        break;
                    case 4:
                        Utility.programExit();
                        System.exit(0);
                        scanner.close();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        Utility.pauseClearScreen(scanner, 0);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                Utility.clearScreen(1);
            }
        }
    }
}
