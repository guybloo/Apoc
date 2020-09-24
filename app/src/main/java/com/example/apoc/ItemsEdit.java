package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.types.ItemAdapter;
import com.example.apoc.types.ItemCount;
import com.example.apoc.types.User;

import java.util.ArrayList;

public class ItemsEdit extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    private ItemAdapter adapter;
    private User user;
    private Boolean isGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_edit);

        // get input params

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

        UsersDB udb = new UsersDB();
        udb.updateItem(user);

    }
}