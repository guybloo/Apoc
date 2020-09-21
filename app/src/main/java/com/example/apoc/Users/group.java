package com.example.final_pro_rest;


import java.util.ArrayList;

public class group {

    // todo we need to save the name of the leader? other things about the group like fears?
    private String groupName;
    private ArrayList<user> groupies;
    private Integer numOfMembers;

    public group(String newGroup)
    {

        this.groupName = newGroup;
    }

    public void addMember (user newUser)
    {
        groupies.add(newUser);
        numOfMembers += 1;
    }

    public String getGroupName()
    {

        return groupName;
    }

    public ArrayList<user> getGroupies() {
        return groupies;
    }

    public Integer getNumOfMembers() {
        return numOfMembers;
    }
}
