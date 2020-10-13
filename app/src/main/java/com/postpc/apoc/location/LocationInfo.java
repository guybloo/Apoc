package com.postpc.apoc.location;

import java.io.Serializable;

/**
 * gps location information object
 */
public class LocationInfo implements Serializable {
    private double longitude;
    private double latitude;
    private float accuracy;

    /**
     * constructor
     */
    public LocationInfo(){
        longitude = 0;
        latitude = 0;
        accuracy = 100;
    }

    /**
     * sets the info params
     * @param longi longitude
     * @param lati latitude
     * @param accu accuracy
     */
    public void setParams(double longi, double lati, float accu) {
        longitude = longi;
        latitude = lati;
        accuracy = accu;
    }

    /**
     * gets the measure accuracy
     * @return
     */
    public float getAccuracy() {
        return accuracy;
    }

    /**
     * gets the latitude
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * gets the longitude
     * @return
     */
    public double getLongitude() {
        return longitude;
    }
}
