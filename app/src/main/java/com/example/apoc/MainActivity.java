package com.example.apoc;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.location.LocationTracker;
import com.example.apoc.types.Navigation;
import com.example.apoc.types.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private final String ID = "id";
    private LocationTracker location;
    private final int REGISTERATION_CODE = 1;
    private final int PROFILE_EDIT_CODE = 2;
    private final int PARTNERS_FIND_EDIT_CODE = 3;
    private String userID;
    private SharedPreferences sp;
    private FirebaseUser firebaseUser;
    private User user;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        location = new LocationTracker(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        openPage();
    }

    private void openPage(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {

            final UsersDB udb = new UsersDB();
//            udb.getAllItems();
            udb.loadItemByIdFromDB(firebaseUser.getEmail());

            udb.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                @Override
                public void onGetAll() {
                }

                @Override
                public void onGetSpecific() {
                    user = (User)udb.getItemById(firebaseUser.getEmail());
                    Navigation.openMenu(context, user);

                }
            });

        } else {
            Navigation.openRegistration(context);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        openPage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        location.onPermissionResult(requestCode, permission, grantResults);
    }

    private void savePreferences() {

        SharedPreferences.Editor prefsEditor = sp.edit();
        prefsEditor.putString(ID, userID);

        prefsEditor.apply();
    }

    private void getPreferences() {

        userID = sp.getString(ID, "");
    }
}
