package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.GroupsDB;
import com.example.apoc.DB.ItemsDB;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.Storage.ImagesDB;
import com.example.apoc.types.Group;
import com.example.apoc.types.Item;
import com.example.apoc.types.ItemAdapter;
import com.example.apoc.types.ItemCount;
import com.example.apoc.types.User;

import java.util.ArrayList;

public class ItemsEdit extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    private final String PRIVATE_TITLE = "Items edit";
    private final String GROUP_TITLE = "Group items";
    public static final String USERS = "users";
    public static final String IS_GROUP = "is_group";

    private ItemAdapter adapter;
    private User user;
    private Group group;
    private Boolean isGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_edit);
        Intent intent = getIntent();
        isGroup = intent.getBooleanExtra(IS_GROUP,true);

        if (isGroup){
            ((TextView) findViewById(R.id.items_edit_title)).setText(GROUP_TITLE);
            group = (Group)intent.getSerializableExtra(USERS);
        }
        else {
            ((TextView) findViewById(R.id.items_edit_title)).setText(PRIVATE_TITLE);
            user =(User)intent.getSerializableExtra(USERS);

        }

        ((Button)findViewById(R.id.items_edit_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItems();
            }
        });
        recyclerViewConfig();
    }

    private void recyclerViewConfig() {
        RecyclerView recyclerView = findViewById(R.id.items_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(isGroup) {
            final ItemsDB idb = new ItemsDB();
            idb.getItemsByFears(group.getFears());
            idb.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                @Override
                public void onGetAll() {

                }

                @Override
                public void onGetSpecific() {
                    ArrayList<ItemCount> items = new ArrayList<>();

                    for(String key: idb.getItems().keySet()){
                        ItemCount tempItem = new ItemCount(key);
                        for(User user: group.getGroupies()){

                        }
                    }

                }
            });
        }else {
            adapter = new ItemAdapter(user.getItems(), isGroup);
        }
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemIncrease(int position) {
        user.getItemFromPosition(position).increase();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDecrease(int position) {
        ItemCount item = user.getItemFromPosition(position);
        if(item.getAmount()<=0){
           item.setAmount(0);
        }
        else{
            item.decrease();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private void saveItems(){
        UsersDB udb = new UsersDB();
        udb.updateItem(user);

        finish();
    }
}