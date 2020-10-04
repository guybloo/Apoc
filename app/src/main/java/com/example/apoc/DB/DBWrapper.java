package com.example.apoc.DB;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DBWrapper {
//    protected static DBWrapper wrapper = null;

    protected String docName;
    protected static String ID = "id";

    protected FirebaseFirestore db;
    protected HashMap<String, DBItem> items;

    protected OnDataChangeListener listener;

    public DBWrapper() {
        db = FirebaseFirestore.getInstance();
        items = new HashMap<>();
//        getAllItems();
    }

    public interface OnDataChangeListener {
        void onGetAll();
        void onGetSpecific();
    }

    public void setDataChangeListener(OnDataChangeListener eventListener) {
        listener = eventListener;
    }

    protected void notifyGetAll() {
        if (listener != null) {
            listener.onGetAll();
        }
    }
    protected void notifyGetSpecific() {
        if (listener != null) {
            listener.onGetSpecific();
        }
    }

//    public static DBWrapper getInstance() {
//        if (wrapper == null) {
//            wrapper = new DBWrapper();
//        }
//        return wrapper;
//    }

    public void loadItemByIdFromDB(final String id) {
        items.clear();
        db.collection(docName).whereEqualTo(ID, id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> item = document.getData();
                                items.put(id, parseItem(item));
                            }
                            notifyGetSpecific();
                            android.util.Log.println(Log.INFO, "list size", String.valueOf(items.size()));
                        }
                    }
                });

    }
    public DBItem getItemById(String id){
        return items.get(id);
    }


    public void addItem(DBItem item) {
        items.put(item.getId(), item);
        updateItem(item);
    }

    String getIdFromPosition(int position) {
        return items.keySet().toArray()[position].toString();
    }

    public void updateItem(DBItem item) {
    }


    public Map<String, DBItem> getItems() {
        return items;
    }

    public void removeItem(String id) {
        db.collection(docName).document(id).delete();
        items.remove(id);
    }

    protected DBItem parseItem(Map<String, Object> item) {
//        new TODO((String) item.get(CONTENT),
//                (Boolean) item.get(DONE), (String) item.get(ID),
//                (String) item.get(CREATE), (String) item.get(EDIT)));
        return null;
    }

    public void getAllItems() {
        items.clear();
        db.collection(docName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> item = document.getData();
                                items.put(String.valueOf(item.get(ID)), parseItem(item));
                            }
                            notifyGetAll();
                            android.util.Log.println(Log.INFO, "list size", String.valueOf(items.size()));
                        }
                    }
                });
    }


}
