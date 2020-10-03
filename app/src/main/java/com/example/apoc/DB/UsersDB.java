package com.example.apoc.DB;

import com.example.apoc.location.LocationInfo;
import com.example.apoc.types.Fears;
import com.example.apoc.types.ItemCount;
import com.example.apoc.types.Skills;
import com.example.apoc.types.User;

import java.util.HashMap;
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
    protected static String SKILLS = "skills";
    protected static String FEARS = "fears";
    protected static String ITEMS = "items";
    protected static String IS_GROUPED = "is_grouped";

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
        newItem.put(SKILLS, ListToGson(user.getSkills()));
        newItem.put(FEARS, ListToGson(user.getFears()));
        newItem.put(ITEMS, ListToGson(user.getItems()));
        newItem.put(IS_GROUPED, user.getIsGrouped());

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
                ListFromGson((String) item.get(SKILLS), Skills.class),
                ListFromGson((String) item.get(FEARS), Fears.class),
                (Boolean) item.get(IS_GROUPED));

        user.addItemsList(ListFromGson((String) item.get(ITEMS),ItemCount.class));
        return user;
    }
}
