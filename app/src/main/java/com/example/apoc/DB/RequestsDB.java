package com.example.apoc.DB;

import androidx.annotation.NonNull;

import com.example.apoc.types.JoinRequest;
import com.example.apoc.types.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RequestsDB extends DBWrapper {
    protected static String APPLIER = "applier";
    protected static String RECIPIENT = "recipient";
    protected static String GROUP_JOIN = "is_group_join";

    public RequestsDB(){
        super();
        docName = "requests";
    }
    @Override
    public void updateItem(DBItem updateItem) {
        JoinRequest item = (JoinRequest) updateItem;
        Map<String, Object> newItem = new HashMap<>();
        newItem.put(ID, item.getId());
        newItem.put(APPLIER, item.getApplier());
        newItem.put(RECIPIENT,item.getRecipient());
        newItem.put(GROUP_JOIN, item.isGroupJoin());

        db.collection(docName).document(String.valueOf(item.getId())).set(newItem);
    }

    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        return new JoinRequest(
                (String)item.get(APPLIER),
                (String)item.get(RECIPIENT),
                (Boolean)item.get(GROUP_JOIN));
    }

    public void getItemsByRecipient(final User user){
        items.clear();
        db.collection(docName).whereEqualTo(RECIPIENT,user.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> item = document.getData();
                                JoinRequest tempItem = (JoinRequest) parseItem(item);

                                items.put(tempItem.getId(),tempItem);
                            }

                            notifyGetSpecific();
                        }
                    }
                });
    }
}
