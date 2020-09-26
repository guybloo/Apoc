package com.example.apoc.DB;

import com.example.apoc.types.JoinRequest;
import com.example.apoc.types.Message;
import com.google.firebase.auth.GetTokenResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestsDB extends DBWrapper {
    protected static String APPLIER = "applier";
    protected static String RECIPIENT = "recipient";
    protected static String GROUP_JOIN = "is_group_join";

    public RequestsDB(){
        super();
        docName = "requests";
    }
    @Override
    public void updateItem(DBItem updateItem) {
        JoinRequest item = (JoinRequest) updateItem;
        Map<String, Object> newItem = new HashMap<>();
        newItem.put(ID, item.getId());
        newItem.put(APPLIER, item.getApplier());
        newItem.put(RECIPIENT, item.getRecipient());
        newItem.put(GROUP_JOIN, item.isGroupJoin());

        db.collection(docName).document(String.valueOf(item.getId())).set(newItem);
    }

    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        return new JoinRequest((String) item.get(APPLIER), (String) item.get(RECIPIENT), Boolean.parseBoolean((String) item.get(GROUP_JOIN)));
    }
}
