package com.example.apoc.types;

import com.example.apoc.DB.DBItem;
import com.example.apoc.location.LocationInfo;

import java.util.ArrayList;

public class User implements DBItem
{
    // todo those 3 need to be in the main activity
    static final String ALPHA = "alpha";
    static final String BETA = "beta";
    static final String LONE_WOLF = "lone_wolf";

    private String nickName;
    private String email;
    private String phone;
    private String status;

    private LocationInfo locationInfo;

    private ArrayList abilities;
    private ArrayList fears;

    public User(String newNickName, String newEmail , String newPhone, String newStatus, LocationInfo location, ArrayList abili, ArrayList fear)
    {
        this.nickName = newNickName;
        this.email = newEmail;
        this.phone = newPhone;
        this.status = newStatus;
        this.locationInfo = location;
        this.abilities = abili;
        this.fears = fear;
    }


    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public String getId(){
        return email;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public ArrayList getAbilities() {
        return abilities;
    }

    public ArrayList getFears() {
        return fears;
    }
}

