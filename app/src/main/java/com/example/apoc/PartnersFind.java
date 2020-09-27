package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.location.LocationInfo;
import com.example.apoc.types.User;
import com.example.apoc.types.UserDisplay;

import java.util.ArrayList;

public class PartnersFind extends AppCompatActivity {

    public static final String USER = "user";
    public static final int MAX_DIST = 50000;

    private User user;
    private ArrayList<User> otherUsers;
    private int distance;
    private UsersDB udb;
    private RelativeLayout usersLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners_find);
        usersLayout = findViewById(R.id.users_layout);
        user = (User) getIntent().getSerializableExtra(USER);

        showUsers();

        otherUsers = new ArrayList<>();
        udb = new UsersDB();
        showUsers();
//        udb.getAllItems();
//        udb.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
//            @Override
//            public void onGetAll() {
//                updateUsers();
//            }
//
//            @Override
//            public void onGetSpecific() {
//
//            }
//        });
//
    }

    private void updateUsers() {
        otherUsers.clear();
        ArrayList<DBItem> allUsers = new ArrayList<>(udb.getItems().values());
        for (DBItem temp : allUsers) {
            if (!user.getId().equals(temp.getId()) &&
                    (getDistance(user.getLocationInfo(), ((User) temp).getLocationInfo()) < distance)) {
                otherUsers.add((User) temp);
            }
        }
        showUsers();
    }



    private void showUsers(){
        UserDisplay display = new UserDisplay(user,this);
        display.setParams(100,500);
        display.addView(usersLayout);//        usersLayout.addView(child,params);

    }

    private void configSeekbar(){
        ((SeekBar) findViewById(R.id.distance_seekbar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance = (progress / 100) * MAX_DIST;
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