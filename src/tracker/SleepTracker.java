package tracker;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SleepTracker extends TrackerDB {
    private double sleepDuration;
    private SleepQuality sleepQuality;
    private int sleepId;

    public enum SleepQuality {
        BAD, DECENT, GOOD;
    }

    // Constructor
    public SleepTracker(int userId, LocalDate activityDate, double sleepDuration, SleepQuality sleepQuality) {
        super(userId, "Sleep", activityDate);
        this.sleepDuration = sleepDuration;
        this.sleepQuality = sleepQuality;
    }

    // Getters and Setters
    public int getSleepId() {
        return sleepId;
    }

    public double getSleepDuration() {
        return sleepDuration;
    }

    public SleepQuality getSleepQuality() {
        return this.sleepQuality;
    }

    public void setSleepId(int sleepId) {
        this.sleepId = sleepId;
    }
    
    public void setSleepDuration(double sleepDuration) {
        this.sleepDuration = sleepDuration;
    }

    public void setSleepQuality(SleepQuality sleepQuality) {
        if (sleepQuality == null) {
            System.out.println("Error: SleepQuality cannot be null.");
        } else {
            this.sleepQuality = sleepQuality; 
        }
    }

    @Override
    public String getDetails() {
        String fetchSleepSql = "SELECT Sleep_ID, Sleep_Duration, Sleep_Quality, Activity_Date FROM sleep_tracker "
                + "WHERE User_ID = ?";  
    
        try (PreparedStatement stmt = connection.prepareStatement(fetchSleepSql)) {
            stmt.setInt(1, getUserId()); 
            ResultSet rs = stmt.executeQuery();
    
            String sleepDetails = "";  
            boolean hasSleepData = false;
    
            while (rs.next()) {
                hasSleepData = true;
                int sleepID = rs.getInt("Sleep_ID");
                double sleepDuration = rs.getDouble("Sleep_Duration");
                String sleepQuality = rs.getString("Sleep_Quality");
                Date activityDate = rs.getDate("Activity_Date");
    
                sleepDetails += "-----------------------------------\n"
                        + "Date: " + activityDate + "\n"
                        + "Sleep ID: " + sleepID + "\n"
                        + "Duration: " + sleepDuration + " minutes\n"
                        + "Quality: " + sleepQuality + "\n"
                        + "-----------------------------------\n";
            }
    
            if (!hasSleepData) {
                return "No sleep data found.";
            }
    
            return sleepDetails;
        } catch (SQLException e) {
            return "Error fetching sleep data: " + e.getMessage();
        }
    }
    
    @Override
    public void insertToDatabase() {
        String sql = "INSERT INTO sleep_tracker (User_ID, Activity_Date, Sleep_Duration, Sleep_Quality) "
                + "VALUES (?, ?, ?, ?)"; 

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, getUserId());
            stmt.setDate(2, Date.valueOf(getActivityDate()));
            stmt.setDouble(3, sleepDuration);
            stmt.setString(4, getSleepQuality().name()); 

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        setSleepId(generatedKeys.getInt(1)); 
                        System.out.println("\nSleep data inserted successfully with Sleep ID: " + getSleepId());
                    }
                }
            } else {
                System.out.println("Failed to insert sleep data.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting sleep data: " + e.getMessage());
        }
    }

    @Override
    public void deleteFromDatabase() {
        String sql = "DELETE FROM sleep_tracker WHERE User_ID = ? AND Sleep_ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, getUserId());
            stmt.setInt(2, getSleepId()); 

            int rowsDeleted = stmt.executeUpdate();
            System.out.println(rowsDeleted > 0 ? "Sleep data deleted successfully." : "Failed to delete sleep data.");
        } catch (SQLException e) {
            System.out.println("Error deleting sleep data: " + e.getMessage());
        }
    }
}

