package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import utils.Utility;
import utils.WellnessQuotes;

public class JavatoDB_Backup {
    // JDBC URL, username, and password for MySQL database
    private static final String DB_url = "jdbc:mysql://localhost:3306/"; // Default MySQL server URL (no database specified)
    private static final String DB_name = "WellnessDB";
    private static String DB_username = "root";  // Your MySQL username
    private static String DB_password = "a4s6d8z9123";  // Your MySQL password
    static Scanner scanner = new Scanner(System.in);

    public static Connection getConnection() {
        Utility.clearScreen(1);
        System.out.println("Attempting to connect to database...");
        try {
            Connection conn = attemptConnection(DB_url + DB_name, DB_username, DB_password);
            System.out.println("Database connection successful!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Error: Unable to connect to the database. Exiting...");
            return null;
        }
    }

    private static Connection attemptConnection(String fullUrl, String user, String password) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(fullUrl, user, password);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("USE " + DB_name);
            }
            return conn;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1045) {
                System.out.println("Error: Incorrect MySQL username or password.");
                retryConnection();
            } else if (fullUrl.equals(DB_url + DB_name)) {
                System.out.println("Database not found. Proceeding to database creation...");
                Utility.pauseScreen(1);
                createDatabaseAndTables(user, password);
            } else {
                throw e;
            }
        }
        return DriverManager.getConnection(fullUrl, user, password);
    }

    private static void createDatabaseAndTables(String user, String password) {
        try (Connection conn = DriverManager.getConnection(DB_url, user, password);
            Statement stmt = conn.createStatement()) {

            String checkDatabaseSQL = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + DB_name + "'";
            try (var rs = stmt.executeQuery(checkDatabaseSQL)) {
                if (rs.next()) {
                    System.out.println("Database '" + DB_name + "' already exists. Proceeding to program.");
                    Utility.pauseScreen(1);
                    return;
                }
            }   

            stmt.execute("CREATE DATABASE IF NOT EXISTS " + DB_name);
            System.out.println("Database '" + DB_name + "' successfully created.");

            try (Connection dbConn = DriverManager.getConnection(DB_url + DB_name, user, password);
                Statement DBstmt = dbConn.createStatement()) {

                String UsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "User_ID INT AUTO_INCREMENT PRIMARY KEY, "
                    + "Username VARCHAR(50) NOT NULL UNIQUE, "
                    + "Password VARCHAR(50) NOT NULL)";
                DBstmt.execute(UsersTable);
                
                String ProfileTable = "CREATE TABLE IF NOT EXISTS profile ("
                    + "Profile_ID INT AUTO_INCREMENT PRIMARY KEY, "
                    + "User_ID INT, "
                    + "First_Name VARCHAR(50), "
                    + "Last_Name VARCHAR(50), "
                    + "Age INT, "
                    + "Gender VARCHAR(15), "
                    + "Weight DECIMAL(3,2), "
                    + "Height DECIMAL(3,2), "
                    + "FOREIGN KEY (User_ID) REFERENCES users(User_ID))";
                DBstmt.execute(ProfileTable);

                String MealTrackerTable = "CREATE TABLE IF NOT EXISTS meal_tracker ("
                    + "Meal_ID INT AUTO_INCREMENT PRIMARY KEY, "
                    + "User_ID INT, "
                    + "Meal_Date DATE, "
                    + "Meal_Year INT, "
                    + "Meal_Month INT, "
                    + "Meal_Week INT, "
                    + "Meal_Day VARCHAR(10), "
                    + "Meal_Type ENUM('Breakfast', 'Lunch', 'Dinner', 'Extra'), "
                    + "Meal_Description VARCHAR(255), "
                    + "Created_At DATETIME, "
                    + "FOREIGN KEY (User_ID) REFERENCES users(User_ID))"; // Ensure users table exists
                DBstmt.execute(MealTrackerTable);

                String SleepTrackerTable = "CREATE TABLE IF NOT EXISTS sleep_tracker ("
                    + "Sleep_ID INT AUTO_INCREMENT PRIMARY KEY, "
                    + "User_ID INT, "
                    + "Sleep_Date DATETIME, "
                    + "Duration DECIMAL(4,2), "
                    + "Notes VARCHAR(255), "
                    + "FOREIGN KEY (User_ID) REFERENCES users(User_ID))";
                DBstmt.execute(SleepTrackerTable);

                String WorkoutTrackerTable = "CREATE TABLE IF NOT EXISTS workout_tracker ("
                    + "Workout_ID INT AUTO_INCREMENT PRIMARY KEY, "
                    + "User_ID INT, "
                    + "Workout_Date DATETIME, "
                    + "Workout_Type VARCHAR(50), "
                    + "Duration DECIMAL(4,2), "
                    + "Notes VARCHAR(255), "
                    + "FOREIGN KEY (User_ID) REFERENCES users(User_ID))";
                DBstmt.execute(WorkoutTrackerTable);

                String QuoteTable = "CREATE TABLE IF NOT EXISTS quotes ("
                    + "Quote_ID INT AUTO_INCREMENT PRIMARY KEY, "
                    + "Quote_Text VARCHAR(1000), "
                    + "Quote_Author VARCHAR(1000)) ";
                DBstmt.execute(QuoteTable);
                
                String insertQuotes = "INSERT INTO quotes (quote_text, quote_author) VALUES (?, ?)";
                try (PreparedStatement pstmt = dbConn.prepareStatement(insertQuotes)) {
                    for (WellnessQuotes.Quote quote : WellnessQuotes.Quote.values()) {
                        pstmt.setString(1, quote.getText());
                        pstmt.setString(2, quote.getAuthor());
                        pstmt.executeUpdate();
                    }
                }
                
                System.out.println("All tables created successfully!");
                Utility.clearScreen(3);

            } catch (SQLException e) {
                System.out.println("Error while creating tables: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to create the database or tables.");
            e.printStackTrace(); // Print stack trace for better debugging
        }
    }

    public static void retryConnection() {
        while (true) {
            System.out.print("\n-------------------------------------------------------------------------------------------");
            System.out.println("\n\033[3mNote: Please enter the right credentials of your MySQL in order to connect to the database.\033[0m");
            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.print("Enter MySQL Username (default: root): ");
            String username = scanner.nextLine().trim();
            if (!username.isEmpty()) DB_username = username;

            System.out.print("Enter MySQL Password: ");
            String password = scanner.nextLine().trim();
            DB_password = password;

            try {
                attemptConnection(DB_url + DB_name, DB_username, DB_password);
                System.out.println("Connection successful!");
                break;
            } catch (SQLException e) {
                System.out.println("Connection failed. Please try again.");
            }
        }
    }
}
