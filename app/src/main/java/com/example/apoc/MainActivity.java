package com.example.apoc;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.GroupsDB;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.location.LocationInfo;
import com.example.apoc.location.LocationTracker;
import com.example.apoc.users.Group;
import com.example.apoc.users.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LocationTracker location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = new LocationTracker(this);

        User guy = new User("gg","gv","030","ff",new LocationInfo(),null,null);
        UsersDB users = new UsersDB();
        GroupsDB groupsDB = new GroupsDB();
        groupsDB.addItem(new Group("maya's team", guy.getEmail(), null,null ));
        users.addItem(guy);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        location.onPermissionResult(requestCode, permission, grantResults);
    }

}
