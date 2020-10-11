package com.example.apoc.types;

import com.example.apoc.DB.DBItem;
import com.example.apoc.Enums.Fears;
import com.example.apoc.Enums.Skills;
import com.example.apoc.Enums.UserStatus;
import com.example.apoc.location.LocationInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * class represents user db item
 */
public class User implements DBItem, Serializable {


    private String DEFAULT_IMAGE = "https://firebasestorage.googleapis.com/v0/b/apoc-4f783.appspot.com/o/images%2Fimage%3A143509?alt=media&token=1016c18b-ea95-460d-8eff-1ccad6eb579d";
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

    /**
     * constructor
     * @param newNickName nickname
     * @param newEmail email
     * @param newPhone phone
     * @param newStatus status
     * @param imageUrl image
     * @param location location
     * @param skills skills
     * @param fear fears
     * @param isGrouped is grouped
     */
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

    /**
     * empty constructor - only emal
     * @param newEmail email
     */
    public User(String newEmail) {
        this.nickName = "";
        this.email = newEmail;
        this.phone = "";
        this.imageUrl = DEFAULT_IMAGE;
        this.status = UserStatus.undefined.name();
        this.locationInfo = new LocationInfo();
        this.skills = new ArrayList<>();
        this.fears = new ArrayList<>();
        items = new ArrayList<>();
        this.isGrouped = false;
    }

    /**
     * gets the nickname
     * @return
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * gets the email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * gets the phone
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     * gets status
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * gets id
     * @return
     */
    public String getId() {
        return email;
    }

    /**
     * gets if is in group
     * @return
     */
    public boolean getIsGrouped(){
        return isGrouped;
    }

    /**
     * set if in group
     * @param isGrouped
     */
    public void setIsGrouped(boolean isGrouped){
        this.isGrouped = isGrouped;
    }

    /**
     * gets the location info
     * @return
     */
    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    /**
     * get the skills
     * @return
     */
    public ArrayList<Skills> getSkills() {
        return skills;
    }

    /**
     * gets the fears
     * @return
     */
    public ArrayList<Fears> getFears() {
        return fears;
    }

    /**
     * set the user items
     * @param items
     */
    public void setItems(ArrayList<ItemCount> items) {
        this.items.clear();
        this.items = items;
    }

    /**
     * gets the user items
     * @return
     */
    public ArrayList<ItemCount> getItems() {
        return items;
    }

    /**
     * gets the image url
     * @return
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * sets the image url
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * sets the status
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * sets the phone
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * sets the location info
     * @param locationInfo
     */
    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    /**
     * sets the nickname
     * @param nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * sets the fears
     * @param fears
     */
    public void setFears(ArrayList<Fears> fears) {
        this.fears = fears;
    }

    /**
     * sets the skills
     * @param skills
     */
    public void setSkills(ArrayList skills) {
        this.skills = skills;
    }

    /**
     * gets item from position
     * @param position
     * @return
     */
    public ItemCount getItemFromPosition(int position) {
        return items.get(position);
    }

    /**
     * adds an item
     * @param item
     */
    public void addItem(ItemCount item) {
        items.add(item);
    }

    /**
     * adds item list
     * @param itemList
     */
    public void addItemsList(ArrayList<ItemCount> itemList) {
        items.addAll(itemList);
    }

    /**
     * removes a fear
     * @param fear
     */
    public void removeFear(Fears fear) {

        fears.remove(fear);
    }

    /**
     * adds a fear
     * @param fear
     */
    public void addFear(Fears fear) {
        if (!fears.contains(fear)) {
            fears.add(fear);
        }
    }

    /**
     * adds a skill
     * @param skill
     */
    public void addSkill(Skills skill) {
        if (!skills.contains(skill)) {
            skills.add(skill);
        }
    }

    /**
     * removes a skill
     * @param skill
     */
    public void removeSkill(Skills skill) {

        fears.remove(skill);
    }

    /**
     * copies user details
     * @param other
     */
    public void copyUserDetails(User other){
        this.nickName = other.nickName;
        this.phone = other.phone;
        this.imageUrl = other.imageUrl;
        this.fears = other.fears;
        this.skills = other.skills;
        this.items = other.items;
        this.status = other.status;
        this.locationInfo = other.locationInfo;
        this.isGrouped = other.isGrouped;
    }

    /**
     * returns if the user is alpha
     * @return
     */
    public boolean isAlpha(){
        return status.equals(UserStatus.alpha.name());
    }
    /**
     * returns if the user is beta
     * @return
     */
    public boolean isBeta(){
        return status.equals(UserStatus.beta.name());
    }
    /**
     * returns if the user is lone wolf
     * @return
     */
    public boolean isLoneWolf(){
        return status.equals(UserStatus.loneWolf.name());
    }
    /**
     * returns if the user is undefined
     * @return
     */
    public boolean isUndefined(){
        return status.equals(UserStatus.undefined.name());
    }
}

