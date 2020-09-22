package com.example.apoc.DB;

import android.os.Messenger;

import com.example.apoc.types.Fears;
import com.example.apoc.types.Item;
import com.example.apoc.types.Message;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LogDB extends DBWrapper {
    protected static String DOC_NAME = "log";
    protected static String CONTENT = "content";
    protected static String WRITER = "writer";
    protected static String DATE = "date";

    @Override
    void updateItem(DBItem updateItem) {
        Message item = (Message)updateItem;
        Map<String, Object> newItem = new HashMap<>();
        newItem.put(ID, item.getId());
        newItem.put(WRITER, item.getWriter());
        newItem.put(CONTENT, item.getContent());
        newItem.put(DATE, toGson(item.getDate()));

        db.collection(DOC_NAME).document(String.valueOf(item.getId())).set(newItem);
    }

    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        return new Message((String) item.get(CONTENT),(String) item.get(WRITER),fromGson((String) item.get(DATE), Date.class));
    }
}
