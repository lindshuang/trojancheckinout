package cs310.trojancheckinout;

import android.app.Application;

public class MyApplication extends Application {

    private static String building_key = "";

    public static String getSomeVariable() {
        return building_key;
    }

    public static void setSomeVariable(String building_key) {
        MyApplication.building_key = building_key;
    }

}