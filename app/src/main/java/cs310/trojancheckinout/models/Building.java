package cs310.trojancheckinout.models;

import java.util.List;


public class Building {

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(String maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public List<String> getOccupants() {
        return occupants;
    }

    public void setOccupants(List<String> occupants) {
        this.occupants = occupants;
    }

    public String getCurrCapacity() {
        return currCapacity;
    }

    public void setCurrCapacity(String currCapacity) {
        this.currCapacity = currCapacity;
    }

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String QRcode) {
        this.QRcode = QRcode;
    }

//    public String getBuildingCode() {
//        return buildingCode;
//    }
//
//    public void setBuildingCode(String buildingCode) {
//        this.buildingCode = buildingCode;
//    }

    private String buildingName;
    private String maxCapacity;
    private List<String> occupants;
    private String currCapacity;
    private String QRcode;
//    private String buildingCode;

    private Building() {}
    private Building(String buildingName, String currCapacity, String maxCapacity, List<String> occupants, String qrCode, String buildingCode) {
        this.buildingName = buildingName;
        this.currCapacity = currCapacity;
        this.maxCapacity = maxCapacity;
        this.occupants = occupants;
        this.QRcode = qrCode;
//        this.buildingCode = buildingCode;
    }


}
