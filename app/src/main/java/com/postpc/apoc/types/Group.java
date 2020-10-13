package com.postpc.apoc.types;

import com.postpc.apoc.DB.DBItem;
import com.postpc.apoc.DB.GroupsDB;
import com.postpc.apoc.DB.LogDB;
import com.postpc.apoc.Enums.Fears;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * group class
 */
public class Group implements DBItem, Serializable {
    private String ADD_LOG = "%s has join to the group";
    private String REMOVE_LOG = "%s has left the group";

    private String groupName;
    private String leader;
    private ArrayList<String> groupies;
    private ArrayList<Fears> fears;

    /**
     * constructor
     * @param name group name
     * @param leader leader id
     * @param newGroupies the groupies
     * @param fears the group fears
     */
    public Group(String name, String leader, ArrayList<String> newGroupies, ArrayList<Fears> fears)
    {
        this.groupName = name;
        this.leader = leader;
        this.groupies = newGroupies;
        this.fears = fears;
    }

    /**
     * constructor - new group
     * @param name groups name
     * @param leader leaders id
     * @param fears the fears
     */
    public Group(String name, String leader, ArrayList<Fears> fears)
    {
        this.groupName = name;
        this.leader = leader;
        this.groupies = new ArrayList<>();
        this.fears = fears;

    }

    /**
     * adds new member to group
     * @param newUser the user
     */
    public void addMember (User newUser)
    {
        groupies.add(newUser.getId());
        updateGroup(newUser);
        addLog(newUser,ADD_LOG);
    }

    /**
     * removes a member from group
     * @param user the user
     */
    public void removeMember (User user)
    {
        groupies.remove(user.getId());
        updateGroup(user);
        addLog(user,REMOVE_LOG);
    }

    /**
     * updates group in db
     * @param user
     */
    private void updateGroup(User user){
        (new GroupsDB()).updateItem(this);
    }

    /**
     * adds log in the db
     * @param user
     * @param message
     */
    private void addLog(User user, String message){
        (new LogDB()).addItem(new Message(String.format(message,user.getId()),getId()));
    }

    /**
     * gets the group name
     * @return
     */
    public String getGroupName()
    {
        return groupName;
    }

    /**
     * sets the group name
     * @param groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * gets group leader id
     * @return
     */
    public String getLeader() {
        return leader;
    }

    /**
     * gets group members
     * @return
     */
    public ArrayList<String> getGroupies() {
        return groupies;
    }

    /**
     * gets the num of members
     * @return
     */
    public int getNumOfMembers() {
        return groupies.size();
    }

    /**
     * gets the fears
     * @return
     */
    public ArrayList<Fears> getFears() {
        return fears;
    }

    /**
     *     sets the fears
     */
    public void setFears(ArrayList<Fears> fears) {
        this.fears = fears;
    }

    /**
     * gets group id
     * @return
     */
    @Override
    public String getId() {
        return leader;
    }
}
