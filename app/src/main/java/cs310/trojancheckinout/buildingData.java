package cs310.trojancheckinout;

public class buildingData {
    private static String buildingCode;
    private static String currCapacity;
    private static String buildingName;

    public static String getBuildingCode() {return buildingCode;}
    public static void setBuildingCode(String buildingCode) {
        buildingData.buildingCode= buildingCode;}

    public static String getCurrCapacity() {return currCapacity;}
    public static void setCurrCapacity(String currCapacity) {
        buildingData.currCapacity= currCapacity;}

    public static String getBuildingName() {return buildingName;}
    public static void setBuildingName(String buildingName) {
        buildingData.buildingName= buildingName;}
}