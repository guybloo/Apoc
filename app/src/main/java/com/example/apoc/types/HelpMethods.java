package com.example.apoc.types;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HelpMethods {

    public static String toGson(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    public static String ListToGson(ArrayList list) {
        Gson gson = new Gson();
        ArrayList<String> newList = new ArrayList<>();
        for (Object o : list) {
            newList.add(gson.toJson(o));
        }
        return gson.toJson(newList);
    }

    public static <T> T fromGson(String line, final Class<T> cls) {
        Gson gson = new Gson();
//        Type type = new TypeToken<T>() {}.getType();
        return gson.fromJson(line, cls);
    }

    public static <T> ArrayList<T> ListFromGson(String line, final Class<T> cls) {
        Gson gson = new Gson();
        ArrayList<String> list = gson.fromJson(line, ArrayList.class);
        ArrayList<T> newList = new ArrayList<>();
        for (String o : list) {
            newList.add(gson.fromJson(o, cls));
        }
//        Type type = new TypeToken<T>() {}.getType();
//        return gson.fromJson(newList, Arr);
        return newList;
    }

}
