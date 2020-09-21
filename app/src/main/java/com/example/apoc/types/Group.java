package com.example.apoc.types;

import com.example.apoc.DB.DBItem;

import java.util.ArrayList;

public class Group implements DBItem {

    // todo we need to save the name of the leader? other things about the group like fears?
    private String groupName;
    private String leader;
    private ArrayList<User> groupies;
    private ArrayList<Fears> fears;

    public Group(String name, String leader, ArrayList<User> newGroupies, ArrayList<Fears> fears)
    {
        this.groupName = name;
        this.leader = leader;
        this.groupies = newGroupies;
        this.fears = fears;
    }

    public void addMember (User newUser)
    {
        groupies.add(newUser);
    }
    public void removeMember (User user)
    {
        groupies.remove(user);
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLeader() {
        return leader;
    }

    public ArrayList<User> getGroupies() {
        return groupies;
    }

    public int getNumOfMembers() {
        return groupies.size();
    }

    public ArrayList<Fears> getFears() {
        return fears;
    }

    @Override
    public String getId() {
        return leader;
    }
}
