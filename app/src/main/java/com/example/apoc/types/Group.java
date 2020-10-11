package com.example.apoc.types;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.GroupsDB;
import com.example.apoc.DB.LogDB;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.Enums.Fears;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements DBItem, Serializable {
    private String ADD_LOG = "%s has join to the group";
    private String REMOVE_LOG = "%s has left the group";


    private String groupName;
    private String leader;
    private ArrayList<String> groupies;
    private ArrayList<Fears> fears;

    public Group(String name, String leader, ArrayList<String> newGroupies, ArrayList<Fears> fears)
    {
        this.groupName = name;
        this.leader = leader;
        this.groupies = newGroupies;
        this.fears = fears;
    }
    public Group(String name, String leader, ArrayList<Fears> fears)
    {
        this.groupName = name;
        this.leader = leader;
        this.groupies = new ArrayList<>();
        this.fears = fears;

    }

    public void addMember (User newUser)
    {
        groupies.add(newUser.getId());
//        newUser.setIsGrouped(true);
        updateUserAndGroup(newUser);
        addLog(newUser,ADD_LOG);
    }
    public void removeMember (User user)
    {
        groupies.remove(user.getId());
//        user.setIsGrouped(false);
        updateUserAndGroup(user);
        addLog(user,REMOVE_LOG);
    }
    private void updateUserAndGroup(User user){
        (new GroupsDB()).updateItem(this);
//        (new UsersDB()).updateItem(user);
    }

    private void addLog(User user, String message){
        (new LogDB()).addItem(new Message(String.format(message,user.getId()),getId()));
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

    public ArrayList<String> getGroupies() {
        return groupies;
    }

    public int getNumOfMembers() {
        return groupies.size();
    }

    public ArrayList<Fears> getFears() {
        return fears;
    }

    public void setFears(ArrayList<Fears> fears) {
        this.fears = fears;
    }

    @Override
    public String getId() {
        return leader;
    }
}
