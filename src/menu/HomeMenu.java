package menu;

import java.util.Scanner;

import profile.Profile;
import profile.ProfileDAO;
import users.Users;
import users.UsersDAO;
import utils.Utility;

public class HomeMenu {
    private Scanner scanner;
    private UsersDAO usersDAO;
    private ProfileDAO profileDAO;

    public HomeMenu() {
    scanner = new Scanner(System.in);
    usersDAO = new UsersDAO();
    profileDAO = new ProfileDAO();
    }

    public void showHomeMenu() {
        boolean menuStatus = true;
        
        while (menuStatus) {
            Utility.clearScreen(0);

            System.out.println("Home Menu");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Return to Start Menu");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            
            try {
                String input = scanner.nextLine(); 
                int choice = Integer.parseInt(input); 

                Utility.validateInt(choice, "Choice");

                switch (choice) {
                    case 1:
                        Utility.clearScreen(0);
                        login();
                        break;
                    case 2:
                        Utility.clearScreen(0);
                        signup();
                        break;
                    case 3:
                        System.out.println("Returning to Start Menu...");
                        Utility.clearScreen(0);
                        return;
                    case 4:
                        Utility.programExit();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose from 1-4.");
                        Utility.clearScreen(1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number from 1-4.");
                Utility.clearScreen(1);
            }
        }
    }

    //Login
    public void login() {
        if (!usersDAO.hasUsers()) {
            System.out.println("No users available to login. Please sign up first.");
            Utility.pauseClearScreen(scanner, 0);
            return;
        } else {
            System.out.println("Login Window");
            System.out.println("--------------------------------------------------");
            String username;
    
            while (true) {
                System.out.print("Enter username (press ENTER to return to menu): ");
                username = scanner.nextLine();
                if (username.isEmpty()) {
                    Utility.clearScreen(0);
                    return;
                } else {
                    Users user = usersDAO.getUser(username); 
                    if (user != null) {
                        System.out.println("Username found.");
                        System.out.println("--------------------------------------------------");
                        checkPassword(user);
                        return;
                    } else {
                        System.out.println("Username not found. Please try again.");
                    }
                }
            }
        }
    }

    // Checks password if it exists with the username
    public void checkPassword(Users user) {
        while (true) {
            System.out.print("Enter password (press ENTER to go return to menu): ");
            String password = scanner.nextLine();
            if (password.isEmpty()) {
                Utility.clearScreen(0);
                return;
            }
            if (user.getPassword().equals(password)) {
                System.out.println("--------------------------------------------------");
                System.out.println("\nLogin successful!");
                Utility.clearScreen(1);
                
                ProfileDAO profileDAO = new ProfileDAO();
                Profile profile = profileDAO.getProfile(user.getUser_id()); 
                if (profile == null) {
                    System.out.println("Profile not found. Proceeding to profile creation.");
                    Utility.clearScreen(1);
                    createProfile(user); 
                } else {
                    Utility.clearScreen(0);
                    System.out.println("Profile found. Loading data...");
                    LoggedInMenu loggedInMenu = new LoggedInMenu(); 
                    loggedInMenu.dashboard(user);
                }
            } else {
                System.out.println("Incorrect password. Please try again.");
            }
        }
    }
    
    //Signup
    public void signup() {
        System.out.println("Account Creation");
        System.out.println("--------------------------------------------------");
        String username, password;

        while (true) {
            try {
                System.out.print("Enter username (atleast 8 characters): ");
                username = scanner.nextLine();   
                // Check if username already exists, not empty and has atleast 8 characters before proceeding
                Utility.validateUsername(username, "Username");
                
                if (usersDAO.getUser(username) != null) {
                    System.out.println("Username already exists. Please try another name.");
                    continue;
                }
                break;

            } catch (IllegalArgumentException e) {
                System.out.println("Error. Please try again.");
            }
        }

        while (true) {    
            try {    
                System.out.println("--------------------------------------------------");
                System.out.print("Enter password (atleast 8 characters and one number): ");
                password = scanner.nextLine();
                // Check if password is not empty, has atleast 8 characters, has one integer
                Utility.validatePassword(password, "Password");
                break;

            } catch (IllegalArgumentException e) {
                System.out.println("Error. PLease try another password.");
            }
        }

        Users newUser = new Users(username, password);
        
        if (usersDAO.createUser(newUser)) {
            System.out.println("--------------------------------------------------");
            System.out.println("Account created successfully! You can now login.");
            Utility.clearScreen(1);
        } else {
            System.out.println("Account creation failed.");
        }
    }

    // Profile creation
    public void createProfile(Users user) {
        System.out.println("\nProfile Creation");
    
        String first_name, last_name, gender;
        int age;
        double weight, height;
    
        while (true) {
            try {
                System.out.print("Enter first name: ");
                first_name = scanner.nextLine();
                Utility.validateText(first_name, "First name");
                break; 
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // Validate last name
        while (true) {
            try {
                System.out.print("Enter last name: ");
                last_name = scanner.nextLine();
                Utility.validateText(last_name, "Last name");
                break;  
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // Validate gender
        while (true) {
            try {
                System.out.print("Enter gender: ");
                gender = scanner.nextLine();
                Utility.validateText(gender, "Gender");
                break; 
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // Validate age
        while (true) {
            try {
                System.out.print("Enter age: ");
                age = scanner.nextInt();
                scanner.nextLine();  
                Utility.validateInt(age, "Age");
                break;  
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for age.");
                scanner.nextLine(); 
            }
        }

        // Validate weight
        while (true) {
            try {
                System.out.print("Enter weight (in kg): ");
                weight = scanner.nextDouble();
                scanner.nextLine(); 
                Utility.validateDouble(weight, "Weight");
                break; 
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for weight.");
                scanner.nextLine();  
            }
        }

        // Validate height
        while (true) {
            try {
                System.out.print("Enter height (in cm): ");
                height = scanner.nextDouble();
                scanner.nextLine(); 
                Utility.validateDouble(height, "Height");
                break;  
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for height.");
            }
        }
    
        // Create the profile using the user_id from the logged-in user
        Profile profile = new Profile(user.getUser_id(), first_name, last_name, age, gender, weight, height);
    
        // Save the profile to the database
        if (profileDAO.createProfile(profile)) {
            System.out.println("Profile created successfully.");
            Utility.pauseScreen(1);
    
            // After profile creation, show the logged-in/dashboard menu
            LoggedInMenu loggedInMenu = new LoggedInMenu();
            loggedInMenu.dashboard(user); 
        } else {
            System.out.println("Failed to create profile. Please try again.");
        }
    }
}


