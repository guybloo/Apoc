package com.example.final_pro_rest;

import java.util.ArrayList;

public class user
{
    // todo those 3 need to be in the main activity
    static final String ALPHA = "alpha";
    static final String BETA = "beta";
    static final String LONE_WOLF = "lone_wolf";
    //

    private String nickName;
    private String email;
    private String phone;
    private String status;

//    todo private LocationInfo myHomeData; // locationInfo = latitude, langitude or address?

    private ArrayList abilities;
    private ArrayList fears;

    public user(String newNickName, String newEmail ,String newPhone, String newStatus)
    {
        this.nickName = newNickName;
        this.email = newEmail;
        this.phone = newPhone;
        this.status = newStatus;
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
}

