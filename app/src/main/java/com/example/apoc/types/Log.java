package com.example.apoc.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * class represents a logger
 */
public class Log implements Serializable {
    ArrayList<Message> messages;

    /**
     * constructor
     */
    public Log() {
        messages = new ArrayList<>();
    }

    /**
     * insert a new message
     *
     * @param msg the message
     * @return true is succeeded, false otherwise
     */
    public boolean insert(Message msg) {
        return messages.add(msg);
    }

    /**
     * remove a message
     *
     * @param msg
     * @return
     */
    public boolean remove(Message msg) {
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
}
