package cs310.trojancheckinout.models;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class History {
    private String timeInDate;
    private String timeInTime;
    private String timeOutDate;
    private String timeOutTime;
    private double totalTime;
    private String buildingName;

    public History(String timeInDate, String timeInTime, String timeOutDate, String timeOutTime, double totalTime, String buildingName) {
        this.timeInDate = timeInDate;
        this.timeInTime = timeInTime;
        this.timeOutDate = timeOutDate;
        this.timeOutTime = timeOutTime;
        this.totalTime = totalTime;
        this.buildingName = buildingName;
    }

    public History(){

    }

    public String getTimeInDate() {
        return timeInDate;
    }

    public void setTimeInDate(String timeInDate) {
        this.timeInDate = timeInDate;
    }

    public String getTimeInTime() {
        return timeInTime;
    }

    public void setTimeInTime(String timeInTime) {
        this.timeInTime = timeInTime;
    }

    public String getTimeOutDate() {
        return timeOutDate;
    }

    public void setTimeOutDate(String timeOutDate) {
        this.timeOutDate = timeOutDate;
    }

    public String getTimeOutTime() {
        return timeOutTime;
    }

    public void setTimeOutTime(String timeOutTime) {
        this.timeOutTime = timeOutTime;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }


}
