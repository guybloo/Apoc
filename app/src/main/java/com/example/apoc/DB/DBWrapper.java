package com.example.apoc.DB;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

class DBWrapper {
    private static DBWrapper wrapper = null;

    private static String DOC_NAME = "items";
    private static String ID = "id";
    private static String CONTENT = "content";
    private static String CREATE = "creation_timestamp";
    private static String EDIT = "edit_timestamp";
    private static String DONE = "is_done";

    private FirebaseFirestore db;
    private HashMap<String, TODO> items;

    private OnDataChangeListener listener;

    public interface OnDataChangeListener {
        void onDataChange();
    }

    void setDataChangeListener(OnDataChangeListener eventListener) {
        listener = eventListener;
    }

    private void notifyChange() {
        if (listener != null) {
            listener.onDataChange();
        }
    }

    private DBWrapper() {
        db = FirebaseFirestore.getInstance();
        items = new HashMap<>();
        getAllItems();
    }


    static DBWrapper getInstance() {
        if (wrapper == null) {
            wrapper = new DBWrapper();
        }
        return wrapper;
    }

    TODO getItemById(String id) {
        return items.get(id);
    }

    void addItem(TODO item) {
        items.put(item.getId(), item);
        updateItem(item);
    }

    String getIdFromPosition(int position) {
        return items.keySet().toArray()[position].toString();
    }

    void updateItem(TODO item) {
        Map<String, Object> newItem = new HashMap<>();
        newItem.put(ID, item.getId());
        newItem.put(CONTENT, item.getDescription());
        newItem.put(CREATE, item.getCreate());
        newItem.put(EDIT, item.getEdit());
        newItem.put(DONE, item.getStatus());

        db.collection(DOC_NAME).document(String.valueOf(item.getId())).set(newItem);
    }

    Map<String, TODO> getItems() {
        return items;
    }

    void removeItem(String id) {
        db.collection(DOC_NAME).document(id).delete();
        items.remove(id);
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
                                if (!Boolean.parseBoolean(String.valueOf(item.get(DONE)))) {
                                    items.put(String.valueOf(item.get(ID)), new TODO((String) item.get(CONTENT),
                                            (Boolean) item.get(DONE), (String) item.get(ID),
                                            (String) item.get(CREATE), (String) item.get(EDIT)));

                                } else {
                                    items.put(String.valueOf(item.get(ID)), new TODO((String) item.get(CONTENT),
                                            (Boolean) item.get(DONE), (String) item.get(ID),
                                            (String) item.get(CREATE), (String) item.get(EDIT)));
                                }
                            }
                            notifyChange();
                            android.util.Log.println(Log.INFO, "TODO list size", String.valueOf(items.size()));
                        }
                    }
                });
    }
}
