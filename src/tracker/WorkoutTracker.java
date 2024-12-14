package tracker;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import utils.Utility;

public class WorkoutTracker extends TrackerDB {
    private String workoutType;
    private double workoutDuration;
    private int workoutId; // Added field for Workout ID

    // Constructor
    public WorkoutTracker(int userId, LocalDate activityDate, String workoutType, double workoutDuration) {
        super(userId, "Workout", activityDate);
        this.workoutType = workoutType;
        this.workoutDuration = workoutDuration;
    }

    // Getters and Setters
    public int getWorkoutId() {
        return workoutId;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public double getWorkoutDuration() {
        return workoutDuration;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public void setWorkoutDuration(double workoutDuration) {
        this.workoutDuration = workoutDuration;
    }

    @Override
    public String getDetails() {
        String fetchWorkoutSql = "SELECT Workout_ID, Workout_Type, Workout_Duration, Activity_Date FROM workout_tracker "
                + "WHERE User_ID = ?";  // Fetch workout data for a specific user
    
        try (PreparedStatement stmt = connection.prepareStatement(fetchWorkoutSql)) {
            stmt.setInt(1, getUserId());  // Set User_ID dynamically
            ResultSet rs = stmt.executeQuery();
    
            String workoutDetails = "";  // Start with an empty string to build the result
            boolean hasWorkouts = false;
    
            while (rs.next()) {
                hasWorkouts = true;
                int workoutID = rs.getInt("Workout_ID");
                String workoutType = rs.getString("Workout_Type");
                double workoutDuration = rs.getDouble("Workout_Duration");
                Date activityDate = rs.getDate("Activity_Date");
    
                // Concatenate workout details to the string
                workoutDetails += "-----------------------------------\n"
                        + "Date: " + activityDate + "\n"
                        + "Workout ID: " + workoutID + "\n"
                        + "Type: " + workoutType + "\n"
                        + "Duration: " + workoutDuration + " minutes\n"
                        + "-----------------------------------\n";
            }
    
            if (!hasWorkouts) {
                Utility.clearScreen(0);
                return "No workout data found.";
            }
            Utility.clearScreen(0);
            return workoutDetails;  // Return the concatenated string
        } catch (SQLException e) {
            return "Error fetching workout data: " + e.getMessage();
        }
    }
    

    @Override
    public void insertToDatabase() {
        String sql = "INSERT INTO workout_tracker (User_ID, Activity_Date, Workout_Type, Workout_Duration) "
                + "VALUES (?, ?, ?, ?)";  // No need to insert Workout_ID (auto-increment)

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, getUserId());  // Set User_ID dynamically
            stmt.setDate(2, Date.valueOf(getActivityDate()));  // Set Activity Date
            stmt.setString(3, workoutType);  // Set Workout Type
            stmt.setDouble(4, workoutDuration);  // Set Workout Duration

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the auto-generated Workout_ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        setWorkoutId(generatedKeys.getInt(1));  // Set the generated Workout_ID
                        System.out.println("Workout data inserted successfully with Workout ID: " + getWorkoutId());
                    }
                }
            } else {
                System.out.println("Failed to insert workout data.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting workout data: " + e.getMessage());
        }
    }


    @Override
    public void deleteFromDatabase() {
        String sql = "DELETE FROM workout_tracker WHERE User_ID = ? AND Workout_ID = ?";  // Delete by Workout_ID

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, getUserId());  // Set User_ID dynamically
            stmt.setInt(2, getWorkoutId());  // Set Workout_ID dynamically from the object

            int rowsDeleted = stmt.executeUpdate();  // Execute the deletion
            System.out.println(rowsDeleted > 0 ? "Workout data deleted successfully." : "Failed to delete workout data.");
        } catch (SQLException e) {
            System.out.println("Error deleting workout data: " + e.getMessage());
        }
    }
}
