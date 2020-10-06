package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.GroupsDB;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.SmsSender.LocalSendSmsBroadcastReceiver;
import com.example.apoc.location.LocationInfo;
import com.example.apoc.location.LocationTracker;
import com.example.apoc.types.Group;
import com.example.apoc.types.GroupUserDisplay;
import com.example.apoc.types.Log;
import com.example.apoc.types.Message;
import com.example.apoc.types.Navigation;
import com.example.apoc.types.User;

import java.util.ArrayList;

public class GroupPage extends AppCompatActivity {

    private static final String SMS_ACTION = "APOC.ACTION_SEND_SMS";
    private final int IMAGE_SIZE = 100;
    public static String USER = "user";
    private String MESSAGE_UPDATED = "Your message has been updated";
    private String SMS_SENT = "SMS sent to %s";
    private User user;
    private Group group;
    private UsersDB usersDB;
    private GroupsDB groupsDB;
    private Log log;
    private ArrayList<GroupUserDisplay> userDisplays;
    private ArrayList<User> groupies;

    private String SOS_MESSAGE = "%s is in danger! this is his location: \n https://www.google.com/maps/search/?api=1&query=%s,%s";
    private LocalSendSmsBroadcastReceiver smsReceiver;
    private int SMS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        IntentFilter filter = new IntentFilter();
        smsReceiver = new LocalSendSmsBroadcastReceiver();
        registerReceiver(smsReceiver, filter);

        final Button leave = findViewById(R.id.group_leave);
        leave.setEnabled(false);

        final EditText leaderMessage = findViewById(R.id.group_leader_message);
        leaderMessage.setVisibility(View.GONE);
        final Button leaderMessageUpdate = findViewById(R.id.group_leader_message_update);
        leaderMessageUpdate.setVisibility(View.GONE);

        final Context context = this;
        groupies = new ArrayList<>();
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
                        leave.setEnabled(true);
                        if (group.getLeader().equals(user.getId())) {
                            leave.setVisibility(View.GONE);
                            leaderMessage.setVisibility(View.VISIBLE);
                            leaderMessageUpdate.setVisibility(View.VISIBLE);
                            leaderMessageUpdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!leaderMessage.getText().toString().equals("")) {
                                        log.insert(new Message(leaderMessage.getText().toString(), user.getId()));
                                        Toast.makeText(context, MESSAGE_UPDATED, Toast.LENGTH_SHORT).show();
                                        leaderMessage.setText("");
                                    }
                                }
                            });

                        }
                        displayUsers(5);
                    }
                });

                log.setLogLoadedListener(new Log.OnLogLoadedListener() {
                    @Override
                    public void onLoaded() {
                        findViewById(R.id.group_map).setEnabled(true);
                        displayLog();
                    }
                });
            }

            @Override
            public void onGetSpecific() {

            }
        });

        findViewById(R.id.group_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.openGroupMap(context, groupies, user);
            }
        });
        findViewById(R.id.group_equipment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.openItemsEdit(context, true, user, group, groupies);
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.removeMember(user);
                finish();
            }
        });
        findViewById(R.id.group_sos_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationTracker location = new LocationTracker(context);
                location.startTracking();
                location.setLocationUpdateListener(new LocationTracker.OnLocationUpdateListener() {
                    @Override
                    public void onLocationUpdate() {
                        if (location.getInfo().getAccuracy() <= LocationTracker.ACCURACY) {
                            LocationInfo newLocation = location.getInfo();
                            location.stopTracking();
                            for (User groupie : groupies) {
                                Intent intent = new Intent();
                                intent.putExtra(LocalSendSmsBroadcastReceiver.PHONE, groupie.getPhone());
                                intent.putExtra(LocalSendSmsBroadcastReceiver.CONTENT, String.format(SOS_MESSAGE, user.getEmail(), newLocation.getLatitude(), newLocation.getLongitude()));
                                intent.setAction(SMS_ACTION);
                                sendBroadcast(intent);
                                Toast.makeText(context, String.format(SMS_SENT,groupie.getPhone()),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }

    private void displayUsers(int columns) {
        GridLayout grid = findViewById(R.id.groupies_info);
        grid.setColumnCount(columns);
        for (String id : group.getGroupies()) {
            log.loadByUserId(id);
            User groupie = (User) usersDB.getItemById(id);
            groupies.add(groupie);
            if (!groupie.getId().equals(user.getId())) {
                final GroupUserDisplay userDisplay = new GroupUserDisplay(user, groupie, group, this);
                userDisplay.setImageSize(IMAGE_SIZE);
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

    private void displayLog() {
        LinearLayout layout = findViewById(R.id.group_log);
        log.sort(false);
        for (DBItem message : log.getMessages()) {
            TextView text = new TextView(this);
            text.setText(((Message) message).getContent() + "\n" + ((Message) message).getFormatDate() + "\n" + "____________");
            layout.addView(text);
        }
    }

    private void SosSend() {
        final LocationTracker location = new LocationTracker(this);
        location.setLocationUpdateListener(new LocationTracker.OnLocationUpdateListener() {
            @Override
            public void onLocationUpdate() {
                if (location.getInfo().getAccuracy() <= LocationTracker.ACCURACY) {
                    LocationInfo newLocation = location.getInfo();
                    location.stopTracking();
                    for (User groupie : groupies) {
                        Intent intent = new Intent();
                        intent.putExtra(LocalSendSmsBroadcastReceiver.PHONE, groupie.getPhone());
                        intent.putExtra(LocalSendSmsBroadcastReceiver.CONTENT, String.format(SOS_MESSAGE, user.getEmail(), newLocation.getLatitude(), newLocation.getLongitude()));
                        intent.setAction(SMS_ACTION);
                        sendBroadcast(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }
}