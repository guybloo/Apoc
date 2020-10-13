package com.postpc.apoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.postpc.apoc.DB.DBItem;
import com.postpc.apoc.DB.DBWrapper;
import com.postpc.apoc.DB.GroupsDB;
import com.postpc.apoc.DB.UsersDB;
import com.postpc.apoc.location.LocationInfo;
import com.postpc.apoc.location.LocationTracker;
import com.postpc.apoc.types.Group;
import com.postpc.apoc.Displays.GroupUserDisplay;
import com.postpc.apoc.types.Log;
import com.postpc.apoc.types.Message;
import com.postpc.apoc.types.User;

import java.util.ArrayList;

/**
 * groupPage class
 */
public class GroupPage extends AppCompatActivity {

    private final int IMAGE_SIZE = 175;
    public static String USER = "user";
    private String MESSAGE_UPDATED = "Your message has been updated";
    private String SOS_LOG = "%s called SOS";
    private String SMS_SENT = "SMS sent to %s";
    private String PAGE_TITLE = "%s's Group";
    private User user;
    private Group group;
    private UsersDB usersDB;
    private GroupsDB groupsDB;
    private Log log;
    private ArrayList<GroupUserDisplay> userDisplays;
    private ArrayList<User> groupies;

    private String SOS_MESSAGE = "%s is in danger! this is his location: \n " +
            "https://www.google.com/maps/search/?api=1&query=%s,%s \nThis message Was sent from APOC app";

    /**
     * create the gtoup page and define all the buttons in it
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

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
                        if (group == null) {
                            finish();
                        }
                        String leaderName = ((User) usersDB.getItemById(group.getId())).getNickName();
                        ((TextView) findViewById(R.id.group_name)).setText(String.format(PAGE_TITLE, leaderName));

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
                                        displayMessage(message, (LinearLayout) findViewById(R.id.group_log));

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
                setUserUngrouped();
                finish();
            }
        });
        findViewById(R.id.group_sos_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sosSend(context);
                Message message = new Message(String.format(SOS_LOG, user.getId()), user.getId());
                log.insert(message);
            }
        });
    }


    /**
     * define user as one without a group and update the db
     */
    private void setUserUngrouped() {
        user.setIsGrouped(false);
        usersDB.updateField(user.getId(), UsersDB.IS_GROUPED, false);
    }


    /**
     * creates the grid of images of group members on the top op the page
     *
     * @param columns the number of users to display on one line
     */
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
                        user.setIsGrouped(false);
                        usersDB.updateField(user.getId(), UsersDB.IS_GROUPED, false);
                        userDisplay.removeView();
                        groupsDB.updateItem(group);
                    }
                });
            }
        }
    }

    /**
     * creates the log messages on the group page
     */
    private void displayLog() {
        LinearLayout layout = findViewById(R.id.group_log);
        log.sort(true);
        for (DBItem message : log.getMessages()) {
            displayMessage((Message) message, layout);
        }
    }

    /**
     * create each message of the member ib the group for the log
     *
     * @param message the message from the db
     * @param layout  the group log layout
     */
    private void displayMessage(Message message, LinearLayout layout) {
        TextView text = new TextView(this);
        text.setText(message.getContent() + "\n" + message.getFormatDate());
        Typeface face = ResourcesCompat.getFont(this, R.font.smallpixel);
        text.setTypeface(face);

        layout.addView(text, 0);
        View saparator = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3);
        params.setMargins(0, 50, 0, 50);
        saparator.setLayoutParams(params);
        saparator.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        layout.addView(saparator, 1);
    }

    /**
     * sends sms for all the members of the group
     *
     * @param context
     */
    private void sosSend(final Context context) {
        final LocationTracker location = new LocationTracker(context);
        location.startTracking();
        location.setLocationUpdateListener(new LocationTracker.OnLocationUpdateListener() {
            @Override
            public void onLocationUpdate() {
                if (location.getInfo().getAccuracy() <= LocationTracker.ACCURACY) {
                    LocationInfo newLocation = location.getInfo();
                    location.stopTracking();
                    String message = String.format(SOS_MESSAGE, user.getEmail(), newLocation.getLatitude(), newLocation.getLongitude());
                    for (User groupie : groupies) {

                        // Get the default instance of the SmsManager

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + groupie.getPhone()));
                        intent.putExtra("sms_body", message);
                        startActivity(intent);


                        Toast.makeText(context, String.format(SMS_SENT, groupie.getPhone()), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    /**
     * destroy the event
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}