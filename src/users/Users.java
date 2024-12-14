package users;
import profile.Profile;

public class Users {

    //attributes
    private int user_id;
    private String username;
    private String password;
    private Profile profile;

    //constructor
    public Users(String username, String password) {
        this.username = username;
        this.password = password;
        this.profile = null;
    }

    public Users(int user_id, String username, String password) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.profile = null;
    }

    //getter methods

    public int getUser_id() {
        return user_id;
    }

    public Profile getProfile() {
        return profile;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    //setter methods

    public void setId(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}

