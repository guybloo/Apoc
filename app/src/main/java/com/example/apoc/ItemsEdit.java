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
import com.example.apoc.DB.UsersDB;
import com.example.apoc.Storage.ImagesDB;
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
    private ArrayList<User> users;
    private Boolean isGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_edit);
        Intent intent = getIntent();
        isGroup = intent.getBooleanExtra(IS_GROUP,true);

        if (isGroup){
            ((TextView) findViewById(R.id.items_edit_title)).setText(GROUP_TITLE);
            ((Button)findViewById(R.id.increase)).setVisibility(View.GONE);
            ((Button)findViewById(R.id.decrease)).setVisibility(View.GONE);
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
        adapter = new ItemAdapter(user.getItems());
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