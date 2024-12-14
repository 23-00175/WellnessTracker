package tracker;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import utils.Utility;

public class MealTracker extends TrackerDB {
    private String mealType;
    private String mealDescription;
    private int mealId;

    // Constructor
    public MealTracker(int userId, LocalDate activityDate, String mealType, String mealDescription) {
        super(userId, "Meal", activityDate);
        this.mealType = mealType;
        this.mealDescription = mealDescription;
    }

    // Getters and Setters
    public int getMealId() {
        return mealId;
    }

    public String getMealType() {
        return mealType;
    }

    public String getMealDescription() {
        return mealDescription;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public void setMealDescription(String mealDescription) {
        this.mealDescription = mealDescription;
    }

    @Override
    public String getDetails() {
        String fetchMealsSql = "SELECT Meal_ID, Meal_Type, Meal_Description, Activity_Date FROM meal_tracker "
                + "WHERE User_ID = ?";  
    
        try (PreparedStatement stmt = connection.prepareStatement(fetchMealsSql)) {
            stmt.setInt(1, getUserId()); 
            ResultSet rs = stmt.executeQuery();
    
            String mealDetails = "";  
            boolean hasMeals = false;
    
            while (rs.next()) {
                hasMeals = true;
                int mealID = rs.getInt("Meal_ID");
                String mealType = rs.getString("Meal_Type");
                String mealDescription = rs.getString("Meal_Description");
                Date activityDate = rs.getDate("Activity_Date");
    
                mealDetails += "-----------------------------------\n"
                        + "Date: " + activityDate + "\n"
                        + "Meal ID: " + mealID + "\n"
                        + "Meal Type: " + mealType + "\n"
                        + "Description: " + mealDescription + "\n"
                        + "-----------------------------------\n";
            }
    
            if (!hasMeals) {
                return "No meal data found.";
            }
    
            return mealDetails;  
        } catch (SQLException e) {
            return "Error fetching meal data: " + e.getMessage();
        }
    }
    
    @Override
    public void insertToDatabase() {
        String sql = "INSERT INTO meal_tracker (User_ID, Activity_Date, Meal_Type, Meal_Description) "
                + "VALUES (?, ?, ?, ?)"; 

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, getUserId()); 
            stmt.setDate(2, Date.valueOf(getActivityDate())); 
            stmt.setString(3, mealType); 
            stmt.setString(4, mealDescription);  

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        setMealId(generatedKeys.getInt(1)); 
                        System.out.println("\nMeal data inserted successfully with Meal ID: " + getMealId());
                        Utility.pauseScreen(1);
                    }
                }
            } else {
                System.out.println("Failed to insert meal data.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting meal data: " + e.getMessage());
        }
    }

    @Override
    public void deleteFromDatabase() {
        String sql = "DELETE FROM meal_tracker WHERE User_ID = ? AND Meal_ID = ?";  

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, getUserId());  
            stmt.setInt(2, getMealId());  

            int rowsDeleted = stmt.executeUpdate(); 
            System.out.println(rowsDeleted > 0 ? "Meal data deleted successfully." : "Failed to delete meal data.");
        } catch (SQLException e) {
            System.out.println("Error deleting meal data: " + e.getMessage());
        }
    }
}

