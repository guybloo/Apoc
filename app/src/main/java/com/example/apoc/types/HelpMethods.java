package com.example.apoc.types;

import android.content.res.Resources;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * some help methods
 */
public class HelpMethods {

    /**
     * converts object to json string
     * @param object to convert
     * @return the json string
     */
    public static String toGson(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    /**
     * converts list of objects to json
     * @param list to convert
     * @return the json string
     */
    public static String ListToGson(ArrayList list) {
        Gson gson = new Gson();
        ArrayList<String> newList = new ArrayList<>();
        for (Object o : list) {
            newList.add(gson.toJson(o));
        }
        return gson.toJson(newList);
    }

    /**
     * converts json string to object
     * @param line the json string
     * @param cls object type
     * @param <T> type
     * @return parsed object
     */
    public static <T> T fromGson(String line, final Class<T> cls) {
        Gson gson = new Gson();
        return gson.fromJson(line, cls);
    }

    /**
     * converts json string to objects list
     * @param line json string
     * @param cls object type
     * @param <T> type
     * @return parsed list
     */
    public static <T> ArrayList<T> ListFromGson(String line, final Class<T> cls) {
        Gson gson = new Gson();
        ArrayList<String> list = gson.fromJson(line, ArrayList.class);
        ArrayList<T> newList = new ArrayList<>();
        for (String o : list) {
            newList.add(gson.fromJson(o, cls));
        }
        return newList;
    }
}
