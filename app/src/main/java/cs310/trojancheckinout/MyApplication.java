package cs310.trojancheckinout;

import android.app.Application;

public class MyApplication extends Application {

    private String building_key = "";

    public String getSomeVariable() {
        return building_key;
    }

    public void setSomeVariable(String building_key) {
        this.building_key = building_key;
    }

}