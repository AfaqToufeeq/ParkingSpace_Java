package app.developer.adminparkingspace.dataclass;

public class ParkingSlot {

    String slotID=null;
    String slotParking=null;
    String driverName=null;
    String driverPhone=null;
    String driverEmail=null;
    String vehicleModel=null;
    String parkingTime=null;
    String parkingArea=null;
    String parkingCity=null;
    String authID=null;
    String Price=null;

    public ParkingSlot(){}

    public ParkingSlot(String slotID, String slotParking, String driverName, String driverPhone, String driverEmail, String vehicleModel, String parkingTime, String parkingArea, String parkingCity, String authID, String Price) {
        this.slotID = slotID;
        this.slotParking = slotParking;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.driverEmail = driverEmail;
        this.vehicleModel = vehicleModel;
        this.parkingTime = parkingTime;
        this.parkingArea = parkingArea;
        this.parkingCity = parkingCity;
        this.authID=authID;
        this.Price = Price;
    }

    public ParkingSlot(String slotNumbers, String areaName, String cityName, String uid) {
        slotID=slotNumbers;
        parkingArea=areaName;
        parkingCity=cityName;
        authID=uid;
    }


    public String getAuthID() {
        return authID;
    }

    public void setAuthID(String authID) {
        this.authID = authID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getSlotID() {
        return slotID;
    }

    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }

    public String getSlotParking() {
        return slotParking;
    }

    public void setSlotParking(String slotParking) {
        this.slotParking = slotParking;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(String parkingTime) {
        this.parkingTime = parkingTime;
    }

    public String getParkingArea() {
        return parkingArea;
    }

    public void setParkingArea(String parkingArea) {
        this.parkingArea = parkingArea;
    }

    public String getParkingCity() {
        return parkingCity;
    }

    public void setParkingCity(String parkingCity) {
        this.parkingCity = parkingCity;
    }
}
