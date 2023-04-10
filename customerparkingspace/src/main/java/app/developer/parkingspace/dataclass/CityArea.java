package app.developer.parkingspace.dataclass;

import java.util.ArrayList;

public class CityArea {
    String cityName =null,areaName=null,areaUrl=null,description=null,uid=null;
    ArrayList<String> cities=null;

    public ArrayList<String> getCities() {
        return cities;
    }

    public CityArea(String uid, ArrayList<String> cities) {
        this.uid = uid;
        this.cities = cities;
    }

    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }

    public CityArea(ArrayList<String> cities) {
        this.cities = cities;
    }

    public CityArea(){}

    public CityArea(String cityName, String areaName, String areaUrl, String description, String uid) {
        this.cityName = cityName;
        this.areaName = areaName;
        this.areaUrl = areaUrl;
        this.description = description;
        this.uid = uid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaUrl() {
        return areaUrl;
    }

    public void setAreaUrl(String areaUrl) {
        this.areaUrl = areaUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
