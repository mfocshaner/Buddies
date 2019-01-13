package com.huji.foodtricks.buddies.Models;

import java.io.Serializable;

public class PlaceModel implements Serializable {
    private double latitude;
    private double longitude;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private String locationName;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public PlaceModel() {

    }

    public PlaceModel(double longitude, double latitude) {

        this.longitude = longitude;
        this.latitude = latitude;
    }
}
