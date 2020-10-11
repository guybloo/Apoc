package com.example.apoc.types;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.LogDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * class represents a logger
 */
public class Log implements Serializable {
     private ArrayList<Message> messages;
    private LogDB logDB;
    private int counter;
    private OnLogLoadedListener listener;

    /**
     * constructor
     */
    public Log() {
        messages = new ArrayList<>();
        logDB = new LogDB();
        counter = 0;
        logDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
            @Override
            public void onGetAll() {

            }

            @Override
            public void onGetSpecific() {
                counter--;
                if(counter == 0){
                    for(DBItem item : logDB.getItems().values()){
                        messages.add((Message)item);
                    }
                    notifyLoaded();
                }
            }
        });
    }

    /**
     * event
     */
    public interface OnLogLoadedListener {
        void onLoaded();
    }

    /**
     * sets the event listener
     * @param eventListener
     */
    public void setLogLoadedListener(OnLogLoadedListener eventListener) {
        listener = eventListener;
    }

    /**
     * notify when loaded
     */
    protected void notifyLoaded() {
        if (listener != null) {
            listener.onLoaded();
        }
    }

    /**
     * gets messages by user id
     * @param id
     */
    public void loadByUserId(String id){
        logDB.loadMessagesByUser(id);
        counter++;
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
    public void sort(boolean ascending) {
        if(ascending) {
            Collections.sort(messages, new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }
        else {
            Collections.sort(messages, new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });
        }
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
}
