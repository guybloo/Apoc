package com.postpc.apoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.postpc.apoc.DB.DBItem;
import com.postpc.apoc.DB.DBWrapper;
import com.postpc.apoc.DB.GroupsDB;
import com.postpc.apoc.DB.RequestsDB;
import com.postpc.apoc.DB.UsersDB;
import com.postpc.apoc.types.Group;
import com.postpc.apoc.types.JoinRequest;
import com.postpc.apoc.Adapters.RequestAdapter;
import com.postpc.apoc.types.User;

import java.util.ArrayList;

/**
 * JoinRequests class
 */
public class JoinRequests extends AppCompatActivity implements RequestAdapter.OnItemClickListener {

    public static final String USER = "user";
    public final String NO_REQUESTS = "You don't have any requests";
    private User user;
    private RequestAdapter adapter;
    private RequestsDB requestsDB;
    private UsersDB usersDB;
    ArrayList<User> joinRequests;

    /**
     * create the page of JoinRequests
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_requsets);
        user = (User) getIntent().getSerializableExtra(USER);
        requestsDB = new RequestsDB();

        recyclerViewConfig();
    }

    /**
     * create the recyclerView of the join requests from the current user
     */
    private void recyclerViewConfig() {
        final RecyclerView recyclerView = findViewById(R.id.requests_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RequestAdapter.OnItemClickListener listener = this;
        final Context context = this;
        requestsDB.getItemsByRecipient(user);
        requestsDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
            @Override
            public void onGetAll() {

            }

            @Override
            public void onGetSpecific() {
                usersDB = new UsersDB();
                usersDB.getAllItems();
                usersDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                    @Override
                    public void onGetAll() {
                        joinRequests = new ArrayList<>();
                        for (DBItem item : new ArrayList<DBItem>(requestsDB.getItems().values())) {
                            joinRequests.add((User) usersDB.getItemById(((JoinRequest) item).getApplier()));
                        }
                        if (joinRequests.size() == 0) {
                            Toast.makeText(context, NO_REQUESTS, Toast.LENGTH_LONG).show();
                            finish();
                        }
                        adapter = new RequestAdapter(joinRequests);
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

    /**
     * if the user approve the requests then the beta one of then joins
     * the group of the alpha and all its other requests form other groups are deleted
     * @param position the position of the requests in the recyclerView
     */
    @Override
    public void onRequestApprove(final int position) {
        final User reqUser = adapter.getUserByPosition(position);
        final GroupsDB groupsDB = new GroupsDB();
        final User alpha = user.isAlpha() ? user : reqUser;
        final User beta = user.isBeta() ? user : reqUser;

        groupsDB.getGroupByUser(alpha.getId());
        groupsDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
            @Override
            public void onGetAll() {

            }

            @Override
            public void onGetSpecific() {
                Group group = (Group) groupsDB.getItems().get(alpha.getId());
                if (group != null) {
                    group.addMember(beta);
                    updateIsGrouped(beta, true);
                    deleteRequest(reqUser, user);
                    adapter.deleteRequest(position);

                    final RequestsDB requestsDB1 = new RequestsDB();
                    requestsDB1.getItemsByApplier(beta);
                    requestsDB1.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                        @Override
                        public void onGetAll() {

                        }

                        @Override
                        public void onGetSpecific() {
                            ArrayList<DBItem> toDelete = new ArrayList<>(requestsDB1.getItems().values());
                            for (DBItem item : toDelete) {
                                requestsDB1.removeItem(((JoinRequest) item).getId());
                            }
                        }
                    });

                    if (user.isBeta()) {
                        finish();
                    }
                }
            }
        });
    }

    /**
     * update the user status - is it in group or not
     * @param user the user to be updated
     * @param value true if is in group false otherwise
     */
    private void updateIsGrouped(User user, boolean value){
        user.setIsGrouped(value);
        usersDB.updateField(user.getId(),UsersDB.IS_GROUPED,value);
    }

    /**
     * delete the requests if the receiver decline
     * @param position the position of the requests in the recyclerView
     */
    @Override
    public void onRequestDelete(int position) {
        final User reqUser = adapter.getUserByPosition(position);
        deleteRequest(reqUser, user);
    }

    /**
     * remove the requests from the recyclerView in case of delete
     * @param applier the one who sent the requests
     * @param recipient - the one who was asked
     */
    private void deleteRequest(User applier, User recipient) {
        requestsDB.removeItem((new JoinRequest(applier.getId(), recipient.getId(), false)).getId());

    }
}