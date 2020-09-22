package com.example.apoc.types;

import com.example.apoc.DB.DBItem;
import com.example.apoc.location.LocationInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User implements DBItem, Serializable
{


    private String nickName;
    private String email;
    private String phone;
    private String status;

    private LocationInfo locationInfo;

    private ArrayList abilities;
    private ArrayList<Fears> fears;
    private Map<String, Double> items;

    public User(String newNickName, String newEmail , String newPhone, String newStatus, LocationInfo location, ArrayList abili, ArrayList<Fears> fear)
    {
        this.nickName = newNickName;
        this.email = newEmail;
        this.phone = newPhone;
        this.status = newStatus;
        this.locationInfo = location;
        this.abilities = abili;
        this.fears = fear;
        items = new HashMap<>();
    }

    public User(String newEmail)
    {
        this.nickName = "";
        this.email = newEmail;
        this.phone = "";
        this.status = "";
        this.locationInfo = null;
        this.abilities = new ArrayList();
        this.fears = new ArrayList<>();
        items = new HashMap<>();
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

    public void setItems(Map<String, Double> items) {
        this.items = items;
    }

    public Map<String, Double> getItems() {
        return items;
    }
}

