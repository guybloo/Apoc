package com.example.apoc;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.ItemsDB;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.Storage.ImagesDB;
import com.example.apoc.location.LocationTracker;
import com.example.apoc.types.Item;
import com.example.apoc.types.ItemCount;
import com.example.apoc.types.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String ID = "id";
    private LocationTracker location;
    private final int REGISTERATION_CODE = 1;
    private final int PROFILE_EDIT_CODE = 2;
    private String userID;
    private SharedPreferences sp;
    private FirebaseUser firebaseUser;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = new LocationTracker(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            final UsersDB udb = new UsersDB();
//            udb.getAllItems();
            udb.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                @Override
                public void onGetAll() {

                }

                @Override
                public void onGetSpecific() {
                    user = (User) udb.getItemById(firebaseUser.getEmail());
                    Intent intent = new Intent(getApplicationContext(), ProfileEdit.class);
                    intent.putExtra(ProfileEdit.USER_DATA, user);
                    startActivityForResult(intent, PROFILE_EDIT_CODE);
                }
            });

            udb.loadItemByIdFromDB(firebaseUser.getEmail());

        } else {
            Intent RegisterIntent = new Intent(this, Registration.class);
            startActivityForResult(RegisterIntent, REGISTERATION_CODE);
        }


//        Intent intent = new Intent(this, ItemsEdit.class);
//        intent.putExtra(ItemsEdit.USERS, )
//        intent.putExtra(ProfileEdit.USER_DATA, us);
//        startActivity(intent);


//        getPreferences();
//        if(userID.equals("")){

//            Intent RegisterIntent = new Intent(this, Registration.class);
//            startActivityForResult(RegisterIntent, RegisterCode);
//        }
//        else{
//            // open menu
//        }

//        UsersDB udb = new UsersDB();
////        udb.getAllItems();
//        User us = new User("new user");
//        udb.addItem(us);
//
//        GroupsDB gdb = new GroupsDB();
//        Group g = new Group("gumaya", "mayaguy", null,null);
//        gdb.addItem(g);
//
//        Intent intent = new Intent(this, ProfileEdit.class);
//        intent.putExtra(ProfileEdit.USER_DATA, us);
//        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REGISTERATION_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    userID = data.getStringExtra(Registration.RES_EMAIL);

                    boolean isRegister = data.getBooleanExtra(Registration.RES_IS_REGISTER, true);
                    if (isRegister) {

                        //send to profile edit
                        savePreferences();
                    } else {
                        // send to main menu
                    }
                }
                break;
            }
            case (PROFILE_EDIT_CODE): {
//                ImagesDB imdb = new ImagesDB();
//                imdb.showImage(user.getImageUrl(), (ImageView) findViewById(R.id.temp_image), getApplicationContext());
                final ItemsDB itemDb = new ItemsDB();
                itemDb.getAllItems();
                itemDb.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
                    @Override
                    public void onGetAll() {
//                        for(DBItem i : itemDb.getItems().values()){
//                            user.addItem(new ItemCount(((Item)i).getName()));
//                        }
//                        user.addItemsList(new ArrayList(itemDb.getItems().values()));
                        Intent intent = new Intent(getApplicationContext(), ItemsEdit.class);
                        intent.putExtra(ItemsEdit.USERS, user);
                        intent.putExtra(ItemsEdit.IS_GROUP, false);
                        startActivity(intent);
                    }

                    @Override
                    public void onGetSpecific() {

                    }
                });

            }
        }
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
