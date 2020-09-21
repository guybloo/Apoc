package com.example.apoc.DB;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DBWrapper {
//    protected static DBWrapper wrapper = null;

    protected static String DOC_NAME = "objects";
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
        void onDataChange();
    }

    void setDataChangeListener(OnDataChangeListener eventListener) {
        listener = eventListener;
    }

    protected void notifyChange() {
        if (listener != null) {
            listener.onDataChange();
        }
    }

//    public static DBWrapper getInstance() {
//        if (wrapper == null) {
//            wrapper = new DBWrapper();
//        }
//        return wrapper;
//    }

    public Object getItemById(String id) {
        return items.get(id);
    }

    public void addItem(DBItem item) {
        items.put(item.getId(), item);
        updateItem(item);
    }

    String getIdFromPosition(int position) {
        return items.keySet().toArray()[position].toString();
    }

    void updateItem(DBItem item) {
    }


    public Map<String, DBItem> getItems() {
        return items;
    }

    public void removeItem(String id) {
        db.collection(DOC_NAME).document(id).delete();
        items.remove(id);
    }

    protected DBItem parseItem(Map<String, Object> item) {
//        new TODO((String) item.get(CONTENT),
//                (Boolean) item.get(DONE), (String) item.get(ID),
//                (String) item.get(CREATE), (String) item.get(EDIT)));
        return null;
    }

    public void getAllItems() {
        db.collection(DOC_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> item = document.getData();
                                items.put(String.valueOf(item.get(ID)), parseItem(item));
                            }
                            notifyChange();
                            android.util.Log.println(Log.INFO, "list size", String.valueOf(items.size()));
                        }
                    }
                });
    }

    protected String toGson(Object object){
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    protected <T> T fromGson(String line, Class<T> cls){
        Gson gson = new Gson();
        Type type = new TypeToken<T>() {}.getType();
        return gson.fromJson(line, type);
    }
}
