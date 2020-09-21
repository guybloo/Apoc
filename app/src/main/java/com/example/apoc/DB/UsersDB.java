package com.example.apoc.DB;

import com.example.apoc.location.LocationInfo;
import com.example.apoc.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersDB extends DBWrapper {
    protected static String DOC_NAME = "users";
    protected static String NICK_NAME = "nick_name";
    protected static String EMAIL = "email";
    protected static String PHONE = "phone";
    protected static String STATUS = "status";
    protected static String LOCATION = "location";
    protected static String ABILITIES = "abilities";
    protected static String FEARS = "fears";

    @Override
    void updateItem(DBItem item) {
        User user = (User)item;
        Map<String, Object> newItem = new HashMap<>();
        newItem.put(ID, user.getId());
        newItem.put(NICK_NAME, user.getNickName());
        newItem.put(EMAIL, user.getEmail());
        newItem.put(PHONE, user.getPhone());
        newItem.put(STATUS, user.getStatus());

        newItem.put(LOCATION, toGson(user.getStatus()));
        newItem.put(ABILITIES, toGson(user.getStatus()));
        newItem.put(FEARS, toGson(user.getStatus()));

        db.collection(DOC_NAME).document(String.valueOf(item.getId())).set(newItem);
    }

    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        return new User((String) item.get(NICK_NAME),
                (String) item.get(EMAIL),
                (String) item.get(PHONE),
                (String) item.get(STATUS),
                fromGson((String) item.get(LOCATION),LocationInfo.class),
                fromGson((String) item.get(ABILITIES),ArrayList.class),
                fromGson((String) item.get(FEARS),ArrayList.class));
    }
}
