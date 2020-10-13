package com.postpc.apoc.DB;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * wraps db communication
 */
public class DBWrapper {

    // db name
    protected String docName;
    protected static String ID = "id";
    protected FirebaseFirestore db;

    //items from db
    protected HashMap<String, DBItem> items;
    protected OnDataChangeListener listener;

    /**
     * constructor
     */
    public DBWrapper() {
        db = FirebaseFirestore.getInstance();
        items = new HashMap<>();
    }

    /**
     * class events
     */
    public interface OnDataChangeListener {
        void onGetAll();

        void onGetSpecific();
    }

    /**
     * set events listener
     *
     * @param eventListener listener
     */
    public void setDataChangeListener(OnDataChangeListener eventListener) {
        listener = eventListener;
    }

    /**
     * notify event
     */
    protected void notifyGetAll() {
        if (listener != null) {
            listener.onGetAll();
        }
    }

    /**
     * notify event
     */
    protected void notifyGetSpecific() {
        if (listener != null) {
            listener.onGetSpecific();
        }
    }

    /**
     * update specific item field
     *
     * @param id    item id
     * @param field the field to update
     * @param value the new value
     */
    public void updateField(String id, String field, Object value) {
        HashMap<String, Object> newfield = new HashMap<>();
        newfield.put(field, value);
        db.collection(docName).document(id).update(newfield);
    }

    /**
     * get specific item by is from db
     *
     * @param id the specific id
     */
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

    /**
     * get already loaded item by id
     *
     * @param id the specific id
     * @return the item
     */
    public DBItem getItemById(String id) {
        return items.get(id);
    }

    /**
     * adds item to the db and items collection
     *
     * @param item the new item
     */
    public void addItem(DBItem item) {
        items.put(item.getId(), item);
        updateItem(item);
    }

    /**
     * will be overloaded - update an item
     *
     * @param item the item
     */
    public void updateItem(DBItem item) {
    }

    /**
     * get all loaded items from db
     *
     * @return
     */
    public Map<String, DBItem> getItems() {
        return items;
    }

    /**
     * removes item from db and collection
     *
     * @param id
     */
    public void removeItem(String id) {
        db.collection(docName).document(id).delete();
        items.remove(id);
    }

    /**
     * will be overloaded - parse item from db into object
     *
     * @param item
     * @return
     */
    protected DBItem parseItem(Map<String, Object> item) {
//        new TODO((String) item.get(CONTENT),
//                (Boolean) item.get(DONE), (String) item.get(ID),
//                (String) item.get(CREATE), (String) item.get(EDIT)));
        return null;
    }

    /**
     * gets all items from db
     */
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
