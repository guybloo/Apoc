package com.example.apoc.types;

import com.example.apoc.DB.DBItem;

import java.io.Serializable;

public class JoinRequest implements DBItem, Serializable {
    private String applier;
    private String recipient;
    private boolean groupJoin; // true if joining to a group. false if building a new group

    public JoinRequest(String applier, String recipient, boolean groupJoin){
        this.applier = applier;
        this.recipient = recipient;
        this.groupJoin = groupJoin;
    }

    public String getApplier() {
        return applier;
    }

    public String getRecipient() {
        return recipient;
    }

    public boolean isGroupJoin() {
        return groupJoin;
    }

    @Override
    public String getId() {
        return applier + "_" + recipient;
    }
}
