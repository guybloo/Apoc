package com.example.apoc.DB;

import com.example.apoc.location.LocationInfo;
import com.example.apoc.types.HelpMethods;
import com.example.apoc.types.ItemCount;
import com.example.apoc.types.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.example.apoc.types.HelpMethods.ListfromGson;
import static com.example.apoc.types.HelpMethods.ListFromGson;
import static com.example.apoc.types.HelpMethods.ListToGson;
import static com.example.apoc.types.HelpMethods.fromGson;
import static com.example.apoc.types.HelpMethods.toGson;

public class UsersDB extends DBWrapper {
    protected static String NICK_NAME = "nick_name";
    protected static String EMAIL = "email";
    protected static String PHONE = "phone";
    protected static String STATUS = "status";
    protected static String IMAGE = "image_url";
    protected static String LOCATION = "location";
    protected static String ABILITIES = "abilities";
    protected static String FEARS = "fears";
    protected static String ITEMS = "items";

    public UsersDB(){
        super();
        docName = "users";
    }

    @Override
    public void updateItem(DBItem item) {
        User user = (User)item;
        Map<String, Object> newItem = new HashMap<>();
        newItem.put(ID, user.getId());
        newItem.put(NICK_NAME, user.getNickName());
        newItem.put(EMAIL, user.getEmail());
        newItem.put(PHONE, user.getPhone());
        newItem.put(STATUS, user.getStatus());
        newItem.put(IMAGE, user.getImageUrl());

        newItem.put(LOCATION, toGson(user.getLocationInfo()));
        newItem.put(ABILITIES, toGson(user.getAbilities()));
        newItem.put(FEARS, toGson(user.getFears()));
        newItem.put(ITEMS, ListToGson(user.getItems()));

        db.collection(docName).document(String.valueOf(item.getId())).set(newItem);
    }

    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        User user =  new User((String) item.get(NICK_NAME),
                (String) item.get(EMAIL),
                (String) item.get(PHONE),
                (String) item.get(STATUS),
                (String) item.get(IMAGE),
                fromGson((String) item.get(LOCATION),LocationInfo.class),
                fromGson((String) item.get(ABILITIES),ArrayList.class),
                fromGson((String) item.get(FEARS),ArrayList.class));

        user.addItemsList(ListFromGson((String) item.get(ITEMS),ItemCount.class));
        return user;
    }
}
