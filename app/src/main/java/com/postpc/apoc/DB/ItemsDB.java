package com.postpc.apoc.DB;

import androidx.annotation.NonNull;

import com.postpc.apoc.Enums.Fears;
import com.postpc.apoc.types.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * items db class
 */
public class ItemsDB extends DBWrapper {
    protected static String NAME = "name";

    /**
     * constructor
     */
    public ItemsDB() {
        super();
        docName = "items";
    }

    /**
     * updates items item to the db
     * @param updateItem the item
     */
    @Override
    public void updateItem(DBItem updateItem) {
        Item item = (Item) updateItem;
        Map<String, Object> newItem = new HashMap<>();
        newItem.put(ID, item.getId());
        newItem.put(NAME, item.getName());
        newItem.put(Fears.Pandemic.name(), item.getAmount(Fears.Pandemic));
        newItem.put(Fears.Flood.name(), item.getAmount(Fears.Flood));
        newItem.put(Fears.Hurricane.name(), item.getAmount(Fears.Hurricane));
        newItem.put(Fears.Radioactive.name(), item.getAmount(Fears.Radioactive));
        newItem.put(Fears.War.name(), item.getAmount(Fears.War));
        newItem.put(Fears.Zombies.name(), item.getAmount(Fears.Zombies));

        db.collection(docName).document(String.valueOf(item.getId())).set(newItem);
    }

    /**
     * parse item from the db
     * @param item
     * @return
     */
    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        Map<Fears, Double> amount = new HashMap<>();
        amount.put(Fears.Radioactive, (double) item.get(Fears.Radioactive.name()));
        amount.put(Fears.Zombies, (double)item.get(Fears.Zombies.name()));
        amount.put(Fears.War, (double)item.get(Fears.War.name()));
        amount.put(Fears.Hurricane, (double)item.get(Fears.Hurricane.name()));
        amount.put(Fears.Flood, (double)  item.get(Fears.Flood.name()));
        amount.put(Fears.Pandemic, (double) item.get(Fears.Pandemic.name()));

        return new Item((String) item.get(NAME), amount);
    }

    /**
     * gets all the items which are necessary for the specific fears
     * @param fears fears to find
     */
    public void getItemsByFears(final ArrayList<Fears> fears) {
        items.clear();
        db.collection(docName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> item = document.getData();
                                Item tempItem = (Item) parseItem(item);
                                for (Fears fear : fears) {
                                    if (tempItem.getAmount(fear) > 0) {
                                        items.put(String.valueOf(item.get(ID)), tempItem);
                                        break;
                                    }
                                }
                            }

                            notifyGetSpecific();
                        }
                    }
                });
    }
}
