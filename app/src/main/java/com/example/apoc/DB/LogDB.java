package com.example.apoc.DB;

import androidx.annotation.NonNull;

import com.example.apoc.types.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LogDB extends DBWrapper {
    protected static String CONTENT = "content";
    protected static String WRITER = "writer";
    protected static String DATE = "date";

    public LogDB() {
        super();
        docName = "log";
    }

    @Override
    public void updateItem(DBItem updateItem) {
        Message item = (Message) updateItem;
        Map<String, Object> newItem = new HashMap<>();
        newItem.put(ID, item.getId());
        newItem.put(WRITER, item.getWriter());
        newItem.put(CONTENT, item.getContent());
        newItem.put(DATE, item.getDate().getTime());

        db.collection(docName).document(String.valueOf(item.getId())).set(newItem);
    }

    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        Date date = new Date();
        date.setTime((long) item.get(DATE));
        return new Message((String) item.get(CONTENT), (String) item.get(WRITER), date);
    }

    public void loadMessagesByUser(final String userId) {
//        items.clear();
        db.collection(docName).whereEqualTo(WRITER, userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> item = document.getData();
                                Message temp = (Message) parseItem(item);
                                items.put(temp.getId(), temp);
                            }

                            notifyGetSpecific();
                        }
                    }
                });
    }

    public ArrayList<Message> getMessagesByUser(String userId) {
        ArrayList<Message> messages = new ArrayList<>();
        for (DBItem msg : items.values()) {
            Message tempMessage = (Message) msg;
            if (tempMessage.getWriter().equals(userId)) {
                messages.add(tempMessage);
            }
        }
        return messages;
    }
}
