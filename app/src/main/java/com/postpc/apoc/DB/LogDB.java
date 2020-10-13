package com.postpc.apoc.DB;

import androidx.annotation.NonNull;

import com.postpc.apoc.types.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * logs db
 */
public class LogDB extends DBWrapper {
    protected static String CONTENT = "content";
    protected static String WRITER = "writer";
    protected static String DATE = "date";

    /**
     * constructor
     */
    public LogDB() {
        super();
        docName = "log";
    }

    /**
     * updates log item
     * @param updateItem the item
     */
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

    /**
     * parse log item from the db
     * @param item
     * @return
     */
    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        Date date = new Date();
        date.setTime((long) item.get(DATE));
        return new Message((String) item.get(CONTENT), (String) item.get(WRITER), date);
    }

    /**
     * loads log messages by user id
     * @param userId the id
     */
    public void loadMessagesByUser(final String userId) {
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

}
