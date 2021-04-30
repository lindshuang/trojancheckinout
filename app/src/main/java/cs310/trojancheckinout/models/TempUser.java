package cs310.trojancheckinout.models;

import java.util.ArrayList;
import java.util.List;

public class TempUser {

    private String studentID;
    private String current_qr;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String occupation;
    private String profilePicture;
    private List<String> histories;
    private boolean checked_in;
    private boolean kicked_out;
    private boolean is_deleted;


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

//    public List<History> getHistories() {
//        return histories;
//    }
//
//    public void setHistories(List<History> histories) {
//        this.histories = histories;
//    }

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

        public boolean isIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(boolean is_deleted) {
            this.is_deleted = is_deleted;
        }

        public boolean isKicked_out() {
            return kicked_out;
        }

        public void setKicked_out(boolean kicked_out) {
            this.kicked_out = kicked_out;
        }


    }
