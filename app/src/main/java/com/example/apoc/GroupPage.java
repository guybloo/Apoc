package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
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
    private final int IMAGE_SIZE = 175;
    public static String USER = "user";
    private String MESSAGE_UPDATED = "Your message has been updated";
    private String SOS_LOG = "%s called SOS";
    private String SMS_SENT = "SMS sent to %s";
    private User user;
    private Group group;
    private UsersDB usersDB;
    private GroupsDB groupsDB;
    private Log log;
    private ArrayList<GroupUserDisplay> userDisplays;
    private ArrayList<User> groupies;

    private String SOS_MESSAGE = "%s is in danger! this is his location: \n https://www.google.com/maps/search/?api=1&query=%s,%s \nThis message Was sent from APOC app";
    private LocalSendSmsBroadcastReceiver smsReceiver;
    private int SMS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

//        ((TextView)findViewById(R.id.group_name)).setText(groupies.);
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
                                        Message message = new Message(leaderMessage.getText().toString(), user.getId());
                                        log.insert(message);
                                        Toast.makeText(context, MESSAGE_UPDATED, Toast.LENGTH_SHORT).show();
                                        leaderMessage.setText("");
                                        displayMessage(message,(LinearLayout)findViewById(R.id.group_log));

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
                sosSend(context);
                Message message = new Message(String.format(SOS_LOG,user.getId()),user.getId());
                log.insert(message);
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
        log.sort(true);
        for (DBItem message : log.getMessages()) {
            displayMessage((Message)message, layout);
        }
    }

    private void displayMessage(Message message, LinearLayout layout){
        TextView text = new TextView(this);
        text.setText(message.getContent() + "\n" + message.getFormatDate() );

//        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,text.getTextSize()-5);


        layout.addView(text,0);
        View saparator = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,3);
        params.setMargins(0,50,0,50);
        saparator.setLayoutParams(params);
        saparator.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        layout.addView(saparator,1);
    }

    private void sosSend(final Context context) {
        final LocationTracker location = new LocationTracker(context);
        location.startTracking();
        location.setLocationUpdateListener(new LocationTracker.OnLocationUpdateListener() {
            @Override
            public void onLocationUpdate() {
                if (location.getInfo().getAccuracy() <= LocationTracker.ACCURACY) {
                    LocationInfo newLocation = location.getInfo();
                    location.stopTracking();
                    for (User groupie : groupies) {

                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                            android.util.Log.d("Error", "Can't get sms permissions");
                            return;
                        }

                        // Get the default instance of the SmsManager
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(groupie.getPhone(),
                                null,
                                String.format(SOS_MESSAGE, user.getEmail(), newLocation.getLatitude(), newLocation.getLongitude()),
                                null,
                                null);
                        Toast.makeText(context, String.format(SMS_SENT,groupie.getPhone()),Toast.LENGTH_SHORT).show();

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