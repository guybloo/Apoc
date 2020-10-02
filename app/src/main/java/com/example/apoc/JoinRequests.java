package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.ItemsDB;
import com.example.apoc.DB.RequestsDB;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.types.ItemAdapter;
import com.example.apoc.types.ItemCount;
import com.example.apoc.types.JoinRequest;
import com.example.apoc.types.RequestAdapter;
import com.example.apoc.types.User;

import java.util.ArrayList;

public class JoinRequests extends AppCompatActivity implements RequestAdapter.OnItemClickListener{

    public static final String USER = "user";
    private User user;
    private RequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_requsets);
        user = (User)getIntent().getSerializableExtra(USER);

        recyclerViewConfig();
    }

    private void recyclerViewConfig() {
        final RecyclerView recyclerView = findViewById(R.id.requests_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RequestAdapter.OnItemClickListener listener = this;
        final RequestsDB requestsDB = new RequestsDB();
        requestsDB.getItemsByRecipient(user);
        requestsDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                @Override
                public void onGetAll() {

                }

                @Override
                public void onGetSpecific() {
                    final UsersDB udb = new UsersDB();
                    udb.getAllItems();
                    udb.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                        @Override
                        public void onGetAll() {
                            ArrayList<User> list = new ArrayList<>();
                            for(DBItem item : new ArrayList<DBItem>(requestsDB.getItems().values())){
                                list.add((User) udb.getItemById(((JoinRequest) item).getApplier()));
                            }
                            adapter = new RequestAdapter(list);
                            adapter.setOnItemClickListener(listener);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onGetSpecific() {

                        }
                    });

                }
            });
    }

    @Override
    public void onRequestApprove(int position) {

    }

    @Override
    public void onRequestDelete(int position) {

    }
}