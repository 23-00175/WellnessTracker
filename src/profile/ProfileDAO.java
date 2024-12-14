package profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import main.WellnessDB;
import utils.Utility;

public class ProfileDAO {
    private Connection connection;
    private Scanner scanner;

    public ProfileDAO() {
        this.connection = WellnessDB.getConnection();
        this.scanner = new Scanner(System.in);
    }

    // Create a new profile
    public boolean createProfile(Profile profile) {
        String sql = "INSERT INTO profile (User_ID, First_Name, Last_Name, Age, Gender, Weight, Height) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            Utility.pauseClearScreen(scanner, 1);

            stmt.setInt(1, profile.getUser_id());
            stmt.setString(2, profile.getFirst_name());
            stmt.setString(3, profile.getLast_name());
            stmt.setInt(4, profile.getAge());
            stmt.setString(5, profile.getGender());
            stmt.setDouble(6, profile.getWeight());
            stmt.setDouble(7, profile.getHeight());

            int rowsInserted = stmt.executeUpdate();

            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error creating profile: " + e.getMessage());
            return false;
        }
    }

    // Retrieve a profile by User_ID
    public Profile getProfile(int userId) {
        String sql = "SELECT * FROM profile WHERE User_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Profile(
                            rs.getInt("User_id"),
                            rs.getString("First_Name"),
                            rs.getString("Last_Name"),
                            rs.getInt("Age"),
                            rs.getString("Gender"),
                            rs.getDouble("Weight"),
                            rs.getDouble("Height"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving profile: " + e.getMessage());
        }
        return null;
    }

    // Update profile
    public boolean updateProfile(Profile profile, int userId) {
        String sql = "UPDATE profile SET First_Name = ?, Last_Name = ?, Age = ?, Gender = ?, Weight = ?, Height = ? WHERE User_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, profile.getFirst_name());
            stmt.setString(2, profile.getLast_name());
            stmt.setInt(3, profile.getAge());
            stmt.setString(4, profile.getGender());
            stmt.setDouble(5, profile.getWeight());
            stmt.setDouble(6, profile.getHeight());
            stmt.setInt(7, userId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating profile: " + e.getMessage());
            return false;
        }
    }

    // Delete a profile
    public boolean deleteProfile(int userId) {
        String sql = "DELETE FROM profile WHERE User_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting profile: " + e.getMessage());
            return false;
        }
    }
}
