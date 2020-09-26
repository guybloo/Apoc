package com.example.apoc.DB;

import android.os.Messenger;

import com.example.apoc.types.Fears;
import com.example.apoc.types.HelpMethods;
import com.example.apoc.types.Item;
import com.example.apoc.types.Message;
import com.google.rpc.Help;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.apoc.types.HelpMethods.fromGson;
import static com.example.apoc.types.HelpMethods.toGson;

public class LogDB extends DBWrapper {
    protected static String CONTENT = "content";
    protected static String WRITER = "writer";
    protected static String DATE = "date";

    public LogDB(){
        super();
        docName = "log";
    }
    @Override
    public void updateItem(DBItem updateItem) {
        Message item = (Message)updateItem;
        Map<String, Object> newItem = new HashMap<>();
        newItem.put(ID, item.getId());
        newItem.put(WRITER, item.getWriter());
        newItem.put(CONTENT, item.getContent());
        newItem.put(DATE, toGson(item.getDate()));

        db.collection(docName).document(String.valueOf(item.getId())).set(newItem);
    }

    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        return new Message((String) item.get(CONTENT),(String) item.get(WRITER),fromGson((String) item.get(DATE), Date.class));
    }
}
