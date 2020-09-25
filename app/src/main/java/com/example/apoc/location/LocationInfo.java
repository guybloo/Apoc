package com.example.apoc.location;

import java.io.Serializable;

public class LocationInfo implements Serializable {
    private double longitude;
    private double latitude;
    private float accuracy;

    public LocationInfo(){
        longitude = 0;
        latitude = 0;
        accuracy = 100;
    }

    public void setParams(double longi, double lati, float accu) {
        longitude = longi;
        latitude = lati;
        accuracy = accu;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
