package profile;

import utils.Utility;

//private attributes
public class Profile {
    private int user_id;
    private String first_name, last_name;
    private int age;
    private String gender;
    private double weight;
    private double height;

    //constructor
    public Profile (int user_id, String first_name, String last_name, int age, String gender, double weight, double height) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
    }

    //getter methods

    public int getUser_id() {
        return user_id;
    }
    
    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    //setter methods
    public void setFirst_name(String first_name) {
        Utility.validateText(first_name, "First name");
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        Utility.validateText(last_name, "First name");
        this.last_name = last_name;
    }

    public void setAge(int age) {
        Utility.validateInt(age, "age");
        this.age = age;
    }

    public void setGender(String gender) {
        Utility.validateText(gender, "gender");
        this.gender = gender;
    }

    public void setWeight(double weight) {
        Utility.validateDouble(weight, "weight");
        this.weight = weight;
    }

    public void setHeight(double height) {
        Utility.validateDouble(height, "height");
        this.height = height;
    }

    @Override
    public String toString() {
        return  "Profile Information:\n" +
                "Name: " + first_name + " " + last_name + "\n" +
                "Age: " + age + "\n" +
                "Gender: " + gender + "\n" +
                "Weight: " + weight + " kg" + "\n" +
                "Height: " + height + " cm";
    }

}
