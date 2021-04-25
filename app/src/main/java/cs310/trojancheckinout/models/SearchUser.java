package cs310.trojancheckinout.models;

import java.io.Serializable;
import java.util.Comparator;

public class SearchUser implements Serializable {

    public SearchUser(String lastFirstName, String email) {
        this.lastFirstName = lastFirstName;
        this.email = email;
    }

    private String lastFirstName;
    private String email;

    public String getLastFirstName() {
        return lastFirstName;
    }

    public void setLastFirstName(String lastFirstName) {
        this.lastFirstName = lastFirstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

