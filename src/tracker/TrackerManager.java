package tracker;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import users.Users;
import utils.Utility;

public class TrackerManager {

    Scanner scanner = new Scanner(System.in);
    // Meal Tracker Methods
    
    public void showTracker(Users user) {
        System.out.println("Trackers");
        System.out.println("1. Meal Tracker");
        System.out.println("2. Workout Tracker");
        System.out.println("3. Sleep Tracker");
        System.out.println("4. Return to Dashboard");
        System.out.print("Enter choice (1-4): ");
        try {
            int choice = scanner.nextInt();

            Utility.validateInt(choice, "choice");
            Utility.clearScreen(0);

            switch (choice) {
                case 1:
                    showMealTracker(user);  // Meal tracker
                    Utility.pauseClearScreen(scanner, 1);
                    break;
                case 2:
                    showWorkoutTracker(user);  // Workout tracker
                    break;
                case 3:
                    showSleepTracker(user);  // Sleep tracker
                    break;
                case 4:
                    Utility.clearScreen(0);
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a choice from 1-4.");
                    Utility.clearScreen(1);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number from 1-4.");
            Utility.clearScreen(1);
        }
    }

    public void showMealTracker(Users user) {
        boolean exitMenu = false;  // Flag to control menu flow
        
        while (!exitMenu) {
            System.out.println("Meal Tracker");
            System.out.println("1. View Meals");
            System.out.println("2. Add Meal");
            System.out.println("3. Delete Meal");
            System.out.println("4. Return to Previous Menu");
            System.out.print("Enter choice (1-4): ");
            try {
                int choice = scanner.nextInt();
                Utility.validateInt(choice, "choice");
                scanner.nextLine();  // Consume newline character
        
                switch (choice) {
                    case 1:
                        Utility.clearScreen(0);
                        viewMeals(user);
                        break;
                    case 2:
                        addMeal(user);
                        break;
                    case 3:
                        deleteMeal(user);
                        break;
                    case 4:
                        Utility.clearScreen(0);
                        showTracker(user);
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        Utility.clearScreen(1);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number from 1-4.");
                Utility.clearScreen(1);
            }
        }
    }
    

    // View Meals
    private boolean viewMeals(Users user) {
        Utility.clearScreen(0);
        LocalDate today = LocalDate.now();
        MealTracker mealTracker = new MealTracker(user.getUser_id(), today, null, null);
    
        try {
            String mealDetails = mealTracker.getDetails(); // Get meal details from the database
            if (mealDetails.equals("No meal data found.")) {
                System.out.println("No meal data found.");
                Utility.pauseClearScreen(scanner, 0);
                return false; // No meals to display
            }
            System.out.println("MEAL RECORDS");
            System.out.println(mealDetails);
            return true; // Meals exist
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false; // Error, assume no meals
        }
    }
    

    // Add Meal
    private void addMeal(Users user) {
        Utility.clearScreen(0);
        LocalDate today = LocalDate.now();
        MealTracker mealTracker = new MealTracker(user.getUser_id(), today, null, null);

        String mealType = getMealType(); // Call to get the meal type (breakfast, lunch, etc.)
        if (mealType == null) {
            System.out.println("No meal type selected, returning...");
            return;
        }

        System.out.print("Enter meal description: ");
        String mealDescription = scanner.nextLine();

        mealTracker.setMealType(mealType);
        mealTracker.setMealDescription(mealDescription);

        try {
                    // Debugging: Check the values being set
            Utility.clearScreen(0);
            System.out.println("\nAdding Meal Data:");
            System.out.println("Meal Type: " + mealTracker.getMealType());
            System.out.println("Meal Description: " + mealTracker.getMealDescription());
    
            mealTracker.insertToDatabase();  // Add the meal to the database
            System.out.println("Meal added successfully!");
            Utility.clearScreen(1);
            showMealTracker(user);
        } catch (Exception e) {
            System.out.println("An error occurred while adding the meal: " + e.getMessage());
        }
    }

    // Delete Meal
    private void deleteMeal(Users user) {
        // View meals first and check if there are any
        if (!viewMeals(user)) {
            // If no meals exist, exit the method
            return;
        }
    
        System.out.print("Enter the ID of the meal to delete: ");
        int mealId = scanner.nextInt(); // User enters the meal ID to delete
        scanner.nextLine(); // Consume newline character
    
        System.out.print("Are you sure you want to delete meal with ID " + mealId + "? (y/n): ");
        String confirmation = scanner.nextLine();
    
        if (confirmation.equalsIgnoreCase("y")) {
            LocalDate today = LocalDate.now();
            MealTracker mealTracker = new MealTracker(user.getUser_id(), today, null, null);
            mealTracker.setMealId(mealId); // Set the selected mealId for deletion
    
            try {
                mealTracker.deleteFromDatabase(); // Delete the meal from the database
            } catch (Exception e) {
                System.out.println("An error occurred while deleting the meal: " + e.getMessage());
            } finally {
                Utility.clearScreen(1);
            }
        } else {
            System.out.println("Meal deletion cancelled.");
            Utility.clearScreen(1);
        }
    }
    

    // Workout Tracker Methods
    public void showWorkoutTracker(Users user) {
        boolean exit = false;
    
        while (!exit) {
            System.out.println("Workout Tracker");
            System.out.println("1. View Workouts");
            System.out.println("2. Add Workout");
            System.out.println("3. Delete Workout");
            System.out.println("4. Return to Previous Menu");
            System.out.print("Enter choice (1-4): ");
            try{
                int choice = scanner.nextInt();
                Utility.validateInt(choice, "choice");
                switch (choice) {
                    case 1:
                        Utility.clearScreen(0);
                        viewWorkouts(user);
                        break;
                    case 2:
                        addWorkout(user);
                        break;
                    case 3:
                        deleteWorkout(user);
                        break;
                    case 4:
                        Utility.clearScreen(0);
                        showTracker(user);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        Utility.clearScreen(1);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number from 1-4.");
                Utility.clearScreen(1);
            } 
        }
    }
    private boolean viewWorkouts(Users user) {
        Utility.clearScreen(0);
        LocalDate today = LocalDate.now();
        WorkoutTracker workoutTracker = new WorkoutTracker(user.getUser_id(), today, "", 0.0);
    
        try {
            String workoutDetails = workoutTracker.getDetails(); // Get workout details from the database
            if (workoutDetails.equals("No workout data found.")) {
                System.out.println("No workout data found.");
                Utility.pauseClearScreen(scanner, 0);
                return false; // No workouts to display
            }
            System.out.println("WORKOUT RECORDS");
            System.out.println(workoutDetails);
            return true; // Workouts exist
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false; // Error, assume no workouts
        }
    }
    

    // Add Workout
    private void addWorkout(Users user) {
        Utility.clearScreen(0);
        scanner.nextLine();
        LocalDate today = LocalDate.now();
        WorkoutTracker workoutTracker = new WorkoutTracker(user.getUser_id(), today, "", 0.0);

        System.out.print("Enter workout type: ");
        String workoutType = scanner.nextLine();

        System.out.print("Enter workout duration (in minutes): ");
        double workoutDuration = scanner.nextDouble();

        workoutTracker.setWorkoutType(workoutType);
        workoutTracker.setWorkoutDuration(workoutDuration);

        try {
            // Debugging: Check the values being set
            Utility.clearScreen(0);
            System.out.println("\nAdding Workout Data:");
            System.out.println("Workout Type: " + workoutTracker.getWorkoutType());
            System.out.println("Workout Duration: " + workoutTracker.getWorkoutDuration() + " minutes");
        
            workoutTracker.insertToDatabase();  // Add workout to the database
            System.out.println("Workout added successfully!");
            Utility.clearScreen(1);
            showWorkoutTracker(user);
        } catch (Exception e) {
            System.out.println("An error occurred while adding the workout: " + e.getMessage());
        }
    }

    // Delete Workout
    private void deleteWorkout(Users user) {
        if (!viewWorkouts(user)) { // Call viewWorkouts before proceeding
            return; // Exit if no workouts are available
        }
    
        System.out.print("Enter the ID of the workout to delete: ");
        int workoutId = scanner.nextInt();
        scanner.nextLine();
    
        System.out.print("Are you sure you want to delete workout with ID " + workoutId + "? (y/n): ");
        String confirmation = scanner.nextLine();
    
        if (confirmation.equalsIgnoreCase("y")) {
            LocalDate today = LocalDate.now();
            WorkoutTracker workoutTracker = new WorkoutTracker(user.getUser_id(), today, "", 0.0);
            workoutTracker.setWorkoutId(workoutId);
    
            try {
                workoutTracker.deleteFromDatabase();
                Utility.clearScreen(1);
                showWorkoutTracker(user);
            } catch (Exception e) {
                System.out.println("An error occurred while deleting the workout: " + e.getMessage());
            } finally {
                Utility.clearScreen(1);
            }
        } else {
            System.out.println("Workout deletion cancelled.");
            Utility.clearScreen(1);
        }
    }
    
    
    // Sleep Tracker Methods
    public void showSleepTracker(Users user) {
        boolean exit = false;
        while (!exit){
            System.out.println("Sleep Tracker");
            System.out.println("1. View Sleep Records");
            System.out.println("2. Add Sleep Record");
            System.out.println("3. Delete Sleep Record");
            System.out.println("4. Return to Previous Menu");
            System.out.print("Enter choice (1-4): ");
            try {    
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        Utility.clearScreen(0);
                        viewSleep(user);
                        break;
                    case 2:
                        addSleep(user);
                        break;
                    case 3:
                        deleteSleep(user);
                        break;
                    case 4:
                        Utility.clearScreen(0);
                        showTracker(user);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        Utility.clearScreen(1);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number from 1-4.");
                Utility.clearScreen(1);
            }}
    }

    // View Sleep Records
    private boolean viewSleep(Users user) {
        Utility.clearScreen(0);
        LocalDate today = LocalDate.now();
        SleepTracker sleepTracker = new SleepTracker(user.getUser_id(), today, 0.0, null);
    
        try {
            String sleepDetails = sleepTracker.getDetails(); // Get sleep details from the database
            if (sleepDetails.equals("No sleep data found.")) {
                System.out.println("No sleep data found.");
                Utility.pauseClearScreen(scanner, 0);
                return false; // No sleep data to display
            }
            System.out.println("SLEEP RECORDS");
            System.out.println(sleepDetails);
            return true; // Sleep data exists
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false; // Error occurred, assume no sleep data
        }
    }
    

    // Add Sleep Record
    private void addSleep(Users user) {
        Utility.clearScreen(0);
        // Consume the leftover newline character
        scanner.nextLine();
    
        // Get today's date
        LocalDate today = LocalDate.now();
        SleepTracker sleepTracker = new SleepTracker(user.getUser_id(), today, 0.0, null);
    
        // Step 1: Get the sleep duration (in minutes)
        System.out.print("Enter sleep duration (in minutes): ");
        double sleepDuration = scanner.nextDouble();
        
        // Validate that the sleep duration is positive
        if (sleepDuration <= 0) {
            System.out.println("Error: Sleep duration must be positive.");
            return;  // Exit if invalid
        }
    
        // Step 2: Prompt the user to choose sleep quality
        System.out.println("Sleep quality: ");
        System.out.println("1. BAD");
        System.out.println("2. DECENT");
        System.out.println("3. GOOD");
        System.out.print("Choose a Sleep Quality: ");
        
        int sleepQualityChoice = scanner.nextInt();
        
        // Validate the user's choice
        SleepTracker.SleepQuality sleepQuality = null;
        switch (sleepQualityChoice) {
            case 1:
                sleepQuality = SleepTracker.SleepQuality.BAD;
                break;
            case 2:
                sleepQuality = SleepTracker.SleepQuality.DECENT;
                break;
            case 3:
                sleepQuality = SleepTracker.SleepQuality.GOOD;
                break;
            default:
                System.out.println("Invalid choice. Please select from 1-3 only.");
                Utility.clearScreen(1);
                return;  // Exit if invalid choice
        }
    
        // Step 3: Set the sleep duration and quality in the sleep tracker object
        sleepTracker.setSleepDuration(sleepDuration);
        sleepTracker.setSleepQuality(sleepQuality);
    
        // Debugging: Check the values being set
        Utility.clearScreen(0);
        System.out.println("\nAdding Sleep Data:");
        System.out.println("Sleep Duration: " + sleepTracker.getSleepDuration() + " minutes");
        System.out.println("Sleep Quality: " + sleepTracker.getSleepQuality());
    
        // Step 4: Insert the sleep data into the database
        try {
            sleepTracker.insertToDatabase();  // Add sleep data to the database
            System.out.println("Sleep data added successfully!");
            Utility.pauseClearScreen(scanner, 1);
            showSleepTracker(user);
        } catch (Exception e) {
            System.out.println("An error occurred while adding the sleep data: " + e.getMessage());
        }
    }
    
    

    // Delete Sleep Record
    private void deleteSleep(Users user) {
        if (!viewSleep(user)) { // Check if sleep data exists before proceeding
            return; // Exit if no sleep data is available
        }
    
        System.out.print("Enter the ID of the sleep record to delete: ");
        int sleepId = scanner.nextInt();
        scanner.nextLine();
    
        System.out.print("Are you sure you want to delete sleep record with ID " + sleepId + "? (y/n): ");
        String confirmation = scanner.nextLine();
    
        if (confirmation.equalsIgnoreCase("y")) {
            LocalDate today = LocalDate.now();
            SleepTracker sleepTracker = new SleepTracker(user.getUser_id(), today, 0.0, null);
            sleepTracker.setSleepId(sleepId);
    
            try {
                sleepTracker.deleteFromDatabase();
            } catch (Exception e) {
                System.out.println("An error occurred while deleting the sleep record: " + e.getMessage());
            } finally {
                Utility.clearScreen(1);
            }
        } else {
            System.out.println("Sleep record deletion cancelled.");
            Utility.clearScreen(1);
        }
    }
    

    // Helper Method to Get Meal Type
    private String getMealType() {
        String mealType = null;
        while (mealType == null) {
            System.out.println("Meal Types");
            System.out.println("1. Breakfast");
            System.out.println("2. Lunch");
            System.out.println("3. Dinner");
            System.out.println("4. Extra");
            System.out.print("Choose a Meal Type: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": 
                    mealType = "Breakfast"; 
                    break;
                case "2": 
                    mealType = "Lunch"; 
                    break;
                case "3": 
                    mealType = "Dinner"; 
                    break;
                case "4": 
                    mealType = "Extra"; 
                    break;
                default: 
                    System.out.println("Invalid choice. Please try again.");
                    Utility.clearScreen(1);
                    continue;
            }
        }
        return mealType;
    }
}
