package cs310.trojancheckinout.models;

import java.util.ArrayList;
import java.util.List;

public class User {


    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String occupation;
    private String studentID;
    private String profilePicture;
    private List<History> histories;
    private boolean checked_in;

    private String current_qr;

    public User(){
        firstName = "";
        lastName = "";
        email = "";
        password = "";
        studentID = "";
        occupation = "";
        histories = new ArrayList<>();
        current_qr = "";
    }
    public User(String firstName, String lastName, String email, String password, boolean checked_in) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.checked_in = checked_in;
        this.profilePicture = "https://st.depositphotos.com/1779253/5140/v/600/depositphotos_51405259-stock-illustration-male-avatar-profile-picture-use.jpg";
        this.current_qr = "";
    }
    // For Student
    public User(String fN, String lN,String eml,String pswd, String stuID, String major){
        firstName = fN;
        lastName = lN;
        email = eml;
        password = pswd;
        studentID = stuID;
        occupation = major;
        histories = new ArrayList<>();
        checked_in = false;
        profilePicture = "https://st.depositphotos.com/1779253/5140/v/600/depositphotos_51405259-stock-illustration-male-avatar-profile-picture-use.jpg";
        current_qr = "";
    }
    // For Manager
    public User(String fN, String lN,String eml,String pswd){
        firstName = fN;
        lastName = lN;
        email = eml;
        password = pswd;
        studentID = "";
        occupation = "Manager";
        histories = new ArrayList<>();
        checked_in = false;
        profilePicture = "https://st.depositphotos.com/1779253/5140/v/600/depositphotos_51405259-stock-illustration-male-avatar-profile-picture-use.jpg";
        current_qr = "";
    }

    public User(String firstName, String lastName, String email, String password, boolean checked_in, String occupation, String studentID, String profilePicture, String current_qr) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.checked_in = checked_in;
        this.occupation = occupation;
        this.studentID = studentID;
        this.profilePicture = profilePicture;
        this.current_qr = current_qr;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    public boolean isChecked_in() {
        return checked_in;
    }

    public void setChecked_in(boolean checked_in) {
        this.checked_in = checked_in;
    }

    public String getCurrent_qr() {
        return current_qr;
    }

    public void setCurrent_qr(String current_qr) {
        this.current_qr = current_qr;
    }

}