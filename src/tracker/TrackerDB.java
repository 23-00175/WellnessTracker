package tracker;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import main.WellnessDB;

public abstract class TrackerDB {
    private int userId;
    private LocalDate activityDate;
    private String trackerType;
    protected Connection connection;

    private static Scanner scanner = new Scanner(System.in);

    // Constructor
    public TrackerDB(int userId, String trackerType, LocalDate activityDate) {
        this.userId = userId;
        this.trackerType = trackerType;
        this.connection = WellnessDB.getConnection();
        this.activityDate = (activityDate != null) ? activityDate : promptForDate();
    }

    // Method to prompt for date input
    public LocalDate promptForDate() {
        while (true) {
            System.out.print("Enter the activity date (YYYY-MM-DD): ");
            String dateInput = scanner.nextLine().trim();

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(dateInput, formatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
            }
        }
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public String getTrackerType() {
        return trackerType;
    }

    // Abstract methods to be implemented by subclasses
    public abstract String getDetails();

    public abstract void insertToDatabase();

    public abstract void deleteFromDatabase();
}
