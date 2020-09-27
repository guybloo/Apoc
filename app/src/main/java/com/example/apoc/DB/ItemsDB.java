package com.example.apoc.DB;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apoc.types.Fears;
import com.example.apoc.types.Group;
import com.example.apoc.types.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ItemsDB extends DBWrapper {
    protected static String NAME = "name";

    public ItemsDB(){
        super();
        docName = "items";
    }

    @Override
    public void updateItem(DBItem updateItem) {
        Item item = (Item)updateItem;
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

    @Override
    protected DBItem parseItem(Map<String, Object> item) {
        Map<Fears, Double> amount = new HashMap<>();
        amount.put(Fears.Radioactive,(Double) item.get(Fears.Radioactive.name()));
        amount.put(Fears.Zombies,(Double) item.get(Fears.Zombies.name()));
        amount.put(Fears.War,(Double) item.get(Fears.War.name()));
        amount.put(Fears.Hurricane,(Double) item.get(Fears.Hurricane.name()));
        amount.put(Fears.Flood,(Double) item.get(Fears.Flood.name()));
        amount.put(Fears.Pandemic,(Double) item.get(Fears.Pandemic.name()));

        return new Item((String) item.get(NAME),amount);
    }

    public void getItemsByFears(final ArrayList<Fears> fears){
        items.clear();
        db.collection(docName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> item = document.getData();
                                Item tempItem = (Item)parseItem(item);
                                for(Fears fear : fears){
                                    if(tempItem.getAmount(fear) > 0){
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
