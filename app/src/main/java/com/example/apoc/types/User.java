package com.example.apoc.types;

import com.example.apoc.DB.DBItem;
import com.example.apoc.location.LocationInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements DBItem, Serializable {


    private String nickName;
    private String email;
    private String phone;
    private String status;
    private String imageUrl;
    private boolean isGrouped;

    private LocationInfo locationInfo;

    private ArrayList<Skills> skills;
    private ArrayList<Fears> fears;
    private ArrayList<ItemCount> items;

    public User(String newNickName, String newEmail, String newPhone, String newStatus, String imageUrl, LocationInfo location, ArrayList<Skills> skills, ArrayList<Fears> fear, boolean isGrouped) {
        this.nickName = newNickName;
        this.email = newEmail;
        this.phone = newPhone;
        this.status = newStatus;
        this.locationInfo = location;
        this.skills = skills;
        this.fears = fear;
        items = new ArrayList<>();
        this.imageUrl = imageUrl;
        this.isGrouped = isGrouped;
    }

    public User(String newEmail) {
        this.nickName = "";
        this.email = newEmail;
        this.phone = "";
        imageUrl = "";
        this.status = UserStatus.undefined.name();
        this.locationInfo = new LocationInfo();
        this.skills = new ArrayList<>();
        this.fears = new ArrayList<>();
        items = new ArrayList<>();
        isGrouped = false;
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

    public String getId() {
        return email;
    }

    public boolean getIsGrouped(){
        return isGrouped;
    }

    public void setIsGrouped(boolean isGrouped){
        this.isGrouped = isGrouped;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public ArrayList<Skills> getSkills() {
        return skills;
    }

    public ArrayList<Fears> getFears() {
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

    public void setSkills(ArrayList skills) {
        this.skills = skills;
    }


    public ItemCount getItemFromPosition(int position) {
        return items.get(position);
    }

    public void addItem(ItemCount item) {
        items.add(item);
    }

    public void addItemsList(ArrayList<ItemCount> itemList) {
        items.addAll(itemList);
    }

    public void removeFear(Fears fear) {

        fears.remove(fear);
    }

    public void addFear(Fears fear) {
        if (!fears.contains(fear)) {
            fears.add(fear);
        }
    }

    public void addSkill(Skills skill) {
        if (!skills.contains(skill)) {
            skills.add(skill);
        }
    }

    public void removeSkill(Skills skill) {

        fears.remove(skill);
    }

    public void copyUserDetails(User other){
        this.nickName = other.getNickName();
        this.phone = other.getPhone();
        this.imageUrl = other.getImageUrl();
        this.fears = other.getFears();
        this.skills = other.getSkills();
        this.items = other.getItems();
        this.locationInfo = other.locationInfo;
    }
}

