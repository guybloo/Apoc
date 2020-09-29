package com.example.apoc.types;

import android.content.res.Resources;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    // between 0 - 100
    public static int getWidthInScreen(int width){
        return getPrecent(width, Resources.getSystem().getDisplayMetrics().widthPixels);

    }
    public static int getHeightInScreen(int height){
        return getPrecent(height, Resources.getSystem().getDisplayMetrics().heightPixels);
    }
    public static int getWidth(int width, int max){
        return getPrecent(width, max);

    }
    public static int getHeight(int height, int max){
        return getPrecent(height, max);
    }

    private static int getPrecent(int precent, int size){
        float calc = ((float)precent ) / 100;
        calc *= size;
        return (int)calc;
//        return pxToDp((int)calc);
    }
}
