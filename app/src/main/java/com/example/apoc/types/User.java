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
    private String imageUrl;

    private LocationInfo locationInfo;

    private ArrayList abilities;
    private ArrayList<Fears> fears;
    private ArrayList<ItemCount> items;

    public User(String newNickName, String newEmail , String newPhone, String newStatus, String imageUrl, LocationInfo location, ArrayList abili, ArrayList<Fears> fear)
    {
        this.nickName = newNickName;
        this.email = newEmail;
        this.phone = newPhone;
        this.status = newStatus;
        this.locationInfo = location;
        this.abilities = abili;
        this.fears = fear;
        items = new ArrayList<>();
    }

    public User(String newEmail)
    {
        this.nickName = "";
        this.email = newEmail;
        this.phone = "";
        this.status = UserStatus.undefined.name();
        this.locationInfo = new LocationInfo();
        this.abilities = new ArrayList();
        this.fears = new ArrayList<>();
        items = new ArrayList<>();
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

    public void setItems(ArrayList<ItemCount> items) {
        this.items = items;
    }

    public ArrayList<ItemCount> getItems() {
        return items;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setFears(ArrayList<Fears> fears) {
        this.fears = fears;
    }

    public void setAbilities(ArrayList abilities) {
        this.abilities = abilities;
    }

    public ItemCount getItemFromPosition(int position){
        return items.get(position);
    }
}

