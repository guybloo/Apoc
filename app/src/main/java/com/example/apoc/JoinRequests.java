package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.GroupsDB;
import com.example.apoc.DB.ItemsDB;
import com.example.apoc.DB.RequestsDB;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.types.Group;
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
    private RequestsDB requestsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_requsets);
        user = (User)getIntent().getSerializableExtra(USER);
        requestsDB = new RequestsDB();

        recyclerViewConfig();
    }

    private void recyclerViewConfig() {
        final RecyclerView recyclerView = findViewById(R.id.requests_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RequestAdapter.OnItemClickListener listener = this;
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
                            // todo connect between users and requests to that we can delete it
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
        final User reqUser = adapter.getUserByPosition(position);
        final GroupsDB groupsDB = new GroupsDB();
        groupsDB.getGroupByUser(user.getId());
        groupsDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
            @Override
            public void onGetAll() {

            }

            @Override
            public void onGetSpecific() {
                Group group = (Group)groupsDB.getItems().get(user.getId());
                assert group != null;
                group.addMember(reqUser);
            }
        });
    }

    @Override
    public void onRequestDelete(int position) {
        final User reqUser = adapter.getUserByPosition(position);
        requestsDB.removeItem((new JoinRequest(reqUser.getId(),user.getId(),false)).getId());
    }
}