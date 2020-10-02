package com.example.apoc.types;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.LogDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * class represents a logger
 */
public class Log implements Serializable {
     private ArrayList<Message> messages;
    private LogDB logDB;

    /**
     * constructor
     */
    public Log() {
        messages = new ArrayList<>();
        logDB = new LogDB();
        logDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
            @Override
            public void onGetAll() {

            }

            @Override
            public void onGetSpecific() {
                for(DBItem item : logDB.getItems().values()){
                    messages.add((Message)item);
                }
            }
        });
    }

    public void loadByUserId(String id){
        logDB.getMessagesByUser(id);
    }

    /**
     * insert a new message
     *
     * @param msg the message
     * @return true is succeeded, false otherwise
     */
    public boolean insert(Message msg) {
        logDB.addItem(msg);
        return messages.add(msg);
    }

    /**
     * remove a message
     *
     * @param msg
     * @return
     */
    public boolean remove(Message msg) {
        logDB.removeItem(msg.getId());
        return messages.remove(msg);
    }

    /**
     * sort the messages by date
     */
    public void sort() {
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
}
