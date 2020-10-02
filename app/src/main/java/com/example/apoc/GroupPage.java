package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.GroupsDB;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.types.Group;
import com.example.apoc.types.GroupUserDisplay;
import com.example.apoc.types.Log;
import com.example.apoc.types.Message;
import com.example.apoc.types.User;

import java.util.ArrayList;

public class GroupPage extends AppCompatActivity {

    public static String USER = "user";
    private User user;
    private Group group;
    private UsersDB usersDB;
    private GroupsDB groupsDB;
    private Log log;
    private ArrayList<GroupUserDisplay> userDisplays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        log = new Log();
        user = (User) getIntent().getSerializableExtra(USER);
        userDisplays = new ArrayList<>();
        usersDB = new UsersDB();
        usersDB.getAllItems();
        usersDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
            @Override
            public void onGetAll() {
                groupsDB = new GroupsDB();
                groupsDB.getGroupByUser(user.getId());
                groupsDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                    @Override
                    public void onGetAll() {

                    }

                    @Override
                    public void onGetSpecific() {
                        group = (Group) groupsDB.getItems().get(user.getId());
                        displayUsers(5);
                        displayLog();
                    }
                });

            }

            @Override
            public void onGetSpecific() {

            }
        });
    }

    private void displayUsers(int columns){
        GridLayout grid = findViewById(R.id.groupies_info);
        grid.setColumnCount(columns);
        for(String id : group.getGroupies()){
            log.loadByUserId(id);
            if(!id.equals(user.getId())) {
                User groupie = (User) usersDB.getItemById(id);
                final GroupUserDisplay userDisplay = new GroupUserDisplay(user, groupie, group, this);
                userDisplays.add(userDisplay);
                userDisplay.addView(grid);
                userDisplay.setOnDeleteListener(new GroupUserDisplay.onDeleteListener() {
                    @Override
                    public void onDelete(User user) {
                        group.removeMember(user);
                        userDisplay.removeView();
                        groupsDB.updateItem(group);
                    }
                });
            }
        }
    }

    private void displayLog(){
        for(int i = 0; i < 10; i++){
            log.insert(new Message(String.valueOf(i), user.getId()));
        }
        LinearLayout layout = findViewById(R.id.group_log);
        log.sort();
        for(DBItem message: log.getMessages()) {
            TextView text = new TextView(this);
            text.setText(((Message)message).getContent() + "\n" + ((Message)message).getFormatDate() + "\n" + "____________");
            layout.addView(text);
        }
    }
}