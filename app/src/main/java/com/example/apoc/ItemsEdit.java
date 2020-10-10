package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.ItemsDB;
import com.example.apoc.DB.LogDB;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.types.Group;
import com.example.apoc.Adapters.ItemAdapter;
import com.example.apoc.types.ItemCount;
import com.example.apoc.types.Message;
import com.example.apoc.types.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ItemsEdit extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    private final String PRIVATE_TITLE = "Items edit";
    private final String GROUP_TITLE = "Group items";
    public static final String USERS = "users";
    public static final String IS_GROUP = "is_group";
    public static final String GROUPIES = "groupies";
    public final String CHANGED_MESSAGE = "objects has changed";

    private ItemAdapter adapter;
    private User user;
    private Group group;
    private Boolean isGroup;
    ArrayList<User> groupies;

    private HashSet<ItemCount> changed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_edit);
        Intent intent = getIntent();
        isGroup = intent.getBooleanExtra(IS_GROUP, true);
        changed = new HashSet<>();

        if (isGroup) {
            findViewById(R.id.items_edit_save).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.items_edit_title)).setText(GROUP_TITLE);
            group = (Group) intent.getSerializableExtra(USERS);
            groupies = (ArrayList<User>) intent.getSerializableExtra(GROUPIES);
        } else {
            ((TextView) findViewById(R.id.items_edit_title)).setText(PRIVATE_TITLE);
            user = (User) intent.getSerializableExtra(USERS);

        }

        ((Button) findViewById(R.id.items_edit_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItems();
            }
        });
        recyclerViewConfig();
    }

    private void recyclerViewConfig() {
        final ItemAdapter.OnItemClickListener itemClickListener = this;
        final RecyclerView recyclerView = findViewById(R.id.items_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (isGroup) {
            final ItemsDB idb = new ItemsDB();
            idb.getItemsByFears(group.getFears());
            idb.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                @Override
                public void onGetAll() {

                }

                @Override
                public void onGetSpecific() {
                    HashMap<String, ItemCount> items = new HashMap<>();

                    for (String key : idb.getItems().keySet()) {
                        ItemCount tempItem = new ItemCount(key);
                        items.put(tempItem.getName(), tempItem);
                    }
                    for (User user : groupies) {
                        for(ItemCount itemCount : user.getItems()){
                            if(items.containsKey(itemCount.getName())) {
                                items.get(itemCount.getName()).sum(itemCount.getAmount(), itemCount.getMax());
                            }
                        }
                    }
                    adapter = new ItemAdapter(new ArrayList<ItemCount>(items.values()), isGroup);
                    adapter.setOnItemClickListener(itemClickListener);
                    recyclerView.setAdapter(adapter);

                }
            });
        } else {
            adapter = new ItemAdapter(user.getItems(), isGroup);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onItemIncrease(int position) {
        user.getItemFromPosition(position).increase();
        adapter.notifyDataSetChanged();
        changed.add(user.getItemFromPosition(position));
    }

    @Override
    public void onItemDecrease(int position) {
        ItemCount item = user.getItemFromPosition(position);
        item.decrease();
        adapter.notifyDataSetChanged();
        changed.add(user.getItemFromPosition(position));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void saveItems() {
        UsersDB udb = new UsersDB();
        udb.updateItem(user);

        LogDB logDB = new LogDB();
        String message = "";
        for(ItemCount itemCount: changed){
            message += itemCount.getName() + "_";
        }
        message+= CHANGED_MESSAGE;
        logDB.addItem(new Message(message, user.getId()));

        finish();
    }
}