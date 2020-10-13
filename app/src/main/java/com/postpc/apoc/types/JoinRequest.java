package com.postpc.apoc.types;

import com.postpc.apoc.DB.DBItem;

import java.io.Serializable;

/**
 * join request item for db
 */
public class JoinRequest implements DBItem, Serializable {
    private String applier;
    private String recipient;
    private boolean groupJoin; // true if joining to a group. false if building a new group

    /**
     * constructor
     * @param applier the user who sent it
     * @param recipient the user recieved it
     * @param groupJoin is it for group join?
     */
    public JoinRequest(String  applier, String recipient, boolean groupJoin){
        this.applier = applier;
        this.recipient = recipient;
        this.groupJoin = groupJoin;
    }

    /**
     * gets the applier
     * @return
     */
    public String getApplier() {
        return applier;
    }

    /**
     * gets the recipient
     * @return
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * gets if it's a group join
     * @return
     */
    public boolean isGroupJoin() {
        return groupJoin;
    }

    /**
     * gets request id
     * @return
     */
    @Override
    public String getId() {
        return applier + "_" + recipient;
    }
}
