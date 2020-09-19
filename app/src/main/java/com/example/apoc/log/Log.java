package com.example.apoc.log;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Log {
    ArrayList<Message> messages;

    public Log() {
        messages = new ArrayList<>();
    }

    public boolean insert(Message msg) {
        return messages.add(msg);
    }

    public boolean remove(Message msg) {
        return messages.remove(msg);
    }

    public void sort() {
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }
}
