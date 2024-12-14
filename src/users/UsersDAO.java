package users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.WellnessDB;

public class UsersDAO {
    private Connection connection;

    public UsersDAO() {
        this.connection = WellnessDB.getConnection();
    }

    public boolean hasUsers() {
        String sql = "SELECT COUNT(*) AS user_count FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int userCount = rs.getInt("user_count");
                return userCount > 0; // Returns true if at least one user exists
            }
        } catch (SQLException e) {
            System.out.println("Error checking user existence: " + e.getMessage());
        }
        return false; // Returns false if an error occurs or no users exist
    }
    // Create a new user
    public boolean createUser(Users user) {
        String sql = "INSERT INTO users (Username, Password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            
            int rowsInserted = stmt.executeUpdate();
    
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
        return false;
    }

    // Retrieve a user by username
    public Users getUser(String username) {
        String sql = "SELECT * FROM users WHERE Username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve User_ID along with Username and Password
                    return new Users(
                        rs.getInt("User_ID"),  
                        rs.getString("Username"),
                        rs.getString("Password")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }
        return null;
    }

    // Update a user's password
    public boolean updateUserPassword(String username, String newPassword) {
        String sql = "UPDATE users SET Password = ? WHERE Username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    // Delete a user
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE Username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}
