package com.example.apoc.types;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class HelpMethods {

    public static String toGson(Object object){
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    public static  <T> T fromGson(String line, Class<T> cls){
        Gson gson = new Gson();
        Type type = new TypeToken<T>() {}.getType();
        return gson.fromJson(line, type);
    }
}
