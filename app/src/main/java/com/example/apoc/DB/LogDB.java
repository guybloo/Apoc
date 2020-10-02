package com.example.apoc.DB;

import android.os.Messenger;

import androidx.annotation.NonNull;

import com.example.apoc.types.Fears;
import com.example.apoc.types.Group;
import com.example.apoc.types.HelpMethods;
import com.example.apoc.types.Item;
import com.example.apoc.types.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

    public void getMessagesByUser(final String userId){
        items.clear();
        db.collection(docName).whereEqualTo(WRITER,userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> item = document.getData();
                                Message temp = (Message) parseItem(item);
                                items.put(temp.getId(),temp);
                            }

                            notifyGetSpecific();
                        }
                    }
                });
    }
}
