package profile;

import java.util.InputMismatchException;
import java.util.Scanner;

import menu.HomeMenu;
import utils.Utility;

public class ProfileManager {

    private ProfileDAO profileDAO;
    private Scanner scanner;

    public ProfileManager() {
        this.profileDAO = new ProfileDAO();
        this.scanner = new Scanner(System.in); 
    }

    // Show profile
    public void showProfile(Profile profile) {
        System.out.println(profile.toString());
    }

    // Profile options (edit, delete)
    public void profileOptions(Profile profile) {
        while (true) {
            showProfile(profile);
            System.out.println();
            System.out.println("1. Edit profile");
            System.out.println("2. Delete Profile");
            System.out.println("3. Return to Dashboard");
            System.out.print("Enter choice (1-3): ");
            try {
            int choice = scanner.nextInt();  
            Utility.validateInt(choice, "Choice");

            switch (choice) {
                case 1: // Edit profile
                    Utility.clearScreen(0);
                    editProfile(profile);  
                    break;
                case 2: // Delete profile
                    deleteProfile(profile);
                    break;
                case 3: // Return to dashboard
                    Utility.clearScreen(0);
                    return;
                default:
                    System.out.println("Invalid choice. Please choose from 1-3.");
                    Utility.pauseClearScreen(scanner, 0);  
            }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                Utility.clearScreen(1);
            }
        }
    }

    // Edit profile (update name, age, etc.)
    public void editProfile(Profile profile) {
        showProfile(profile);  // Show profile before editing
        System.out.println();
        System.out.println("1. Name\t\t 4. Weight");
        System.out.println("2. Age\t\t 5. Height");
        System.out.println("3. Gender\t 6. Back to Profile");
        System.out.print("Choose which to edit (1-6): ");
        
        int edit_choice = scanner.nextInt();  
        scanner.nextLine(); 

        switch (edit_choice) {
            case 1: // Edit name (first name and last name)
                String newFirst_name, newLast_name;

                System.out.print("Enter new first name: ");
                newFirst_name = scanner.nextLine();

                System.out.print("Enter new last name: ");
                newLast_name = scanner.nextLine();

                profile.setFirst_name(newFirst_name);
                profile.setLast_name(newLast_name);

                break;

            case 2: // Edit age
                System.out.print("Enter new age: ");
                int newAge = scanner.nextInt();

                profile.setAge(newAge);
                break;

            case 3: // Edit gender
                scanner.nextLine(); 
                System.out.print("Enter new gender: ");
                String newGender = scanner.nextLine();
                profile.setGender(newGender);
                break;

            case 4: // Edit weight
                System.out.print("Enter new weight (in kg): ");
                double newWeight = scanner.nextDouble();

                profile.setWeight(newWeight);
                break;

            case 5: // Edit height
                System.out.print("Enter new height (in cm): ");
                double newHeight = scanner.nextDouble();

                profile.setHeight(newHeight);
                break;

            case 6: // Go back to Profile
                Utility.clearScreen(0);
                return; 

            default:
                System.out.println("Invalid choice. Please choose from 1-6.");
                Utility.pauseClearScreen(scanner, 1); 
        }

        // Update profile in the database if modified
        if (profileDAO.updateProfile(profile, profile.getUser_id())) {
            Utility.clearScreen(0);
            System.out.println("Profile updated successfully!");
            System.out.println();
            showProfile(profile);  
            Utility.pauseClearScreen(scanner, 1); // 
        } else {
            System.out.println("Failed to update profile. Please try again.");
            Utility.pauseClearScreen(scanner, 1);
        }
    }

    // Delete profile
    public void deleteProfile(Profile profile) {
        scanner.nextLine();
        Utility.clearScreen(0);
        System.out.print("Are you sure you want to delete your profile? (y/n): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            if (profileDAO.deleteProfile(profile.getUser_id())) {
                Utility.clearScreen(0);
                System.out.println("Profile deleted successfully!");
                System.out.println("Returning to home menu...");
                Utility.pauseScreen(1);
                HomeMenu homeMenu = new HomeMenu();  // You can replace this with your actual HomeMenu or LoginManager class
                homeMenu.showHomeMenu();

            } else {
                System.out.println("Failed to delete profile.");
            }
        } else {
            System.out.println("Profile deletion cancelled.");
        }
    }

    // Get profile by user ID
    public Profile getProfile(int userId) {
        return profileDAO.getProfile(userId);
    }
}
