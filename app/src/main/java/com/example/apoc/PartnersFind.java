package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.location.LocationInfo;
import com.example.apoc.types.HelpMethods;
import com.example.apoc.types.User;
import com.example.apoc.types.RequestUserDisplay;

import java.util.ArrayList;
import java.util.Random;

public class PartnersFind extends AppCompatActivity {

    public static final String USER = "user";
    public static final int MAX_DIST = 50000;
    public static final int CENTER = 50;

    private User user;
    private ArrayList<RequestUserDisplay> requestUserDisplays;
    private double distance;
    private UsersDB udb;
    private RelativeLayout usersLayout;
    private SeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners_find);
        seekbar = findViewById(R.id.distance_seekbar);
        seekbar.setEnabled(false);
        usersLayout = findViewById(R.id.users_layout);
        user = (User) getIntent().getSerializableExtra(USER);

        requestUserDisplays = new ArrayList<>();
        udb = new UsersDB();
        udb.getAllItems();
        udb.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
            @Override
            public void onGetAll() {
                seekbar.setEnabled(true);
            }

            @Override
            public void onGetSpecific() {

            }
        });
        configSeekbar();
    }

    private void updateUsers() {
        for(RequestUserDisplay display : requestUserDisplays){
            display.removeView();
        }
        requestUserDisplays.clear();
        ArrayList<DBItem> allUsers = new ArrayList<>(udb.getItems().values());
        for (DBItem temp : allUsers) {
            float userDistance = getDistance(user.getLocationInfo(), ((User) temp).getLocationInfo());
            if (!user.getId().equals(temp.getId()) && userDistance < distance && !((User)temp).getIsGrouped()) {
                requestUserDisplays.add(new RequestUserDisplay((User) temp, user, userDistance, this));
            }
        }
        showUsers();
    }


    private void showUsers() {
        for (RequestUserDisplay display : requestUserDisplays) {
            double random = ((new Random()).nextDouble() * 2 * Math.PI);
            int radius = (int) (display.getDistance() / distance * 70) + 15;
            int x = HelpMethods.getWidth(getXPos(radius,random,CENTER), usersLayout.getWidth()) - (display.getView().getWidth() / 2);
            int y =  HelpMethods.getHeight(getYPos(radius,random,CENTER), usersLayout.getHeight())- (display.getView().getHeight() / 2);
            display.setParams(x,y);
            display.addView(usersLayout);

        }
    }

    private int getXPos(int radius, double theta, int center) {
        return (int) (radius * Math.cos(theta) + center);
    }

    private int getYPos(int radius, double theta, int center) {
        return (int) (radius * Math.sin(theta) + center);
    }

    private void configSeekbar() {
       seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance = ((double)progress / 100.0) * MAX_DIST;
                updateUsers();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    // base is te main user. dest all the others
    private float getDistance(LocationInfo base, LocationInfo dest) {
        float[] res = new float[1];
        if (user != null && dest != null) {
            Location.distanceBetween(base.getLatitude(), base.getLongitude(),
                    dest.getLatitude(), dest.getLongitude(), res);
        }
        return res[0];
    }
}