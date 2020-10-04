package com.example.apoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.types.Group;
import com.example.apoc.types.GroupUserDisplay;
import com.example.apoc.types.Navigation;
import com.example.apoc.types.User;
import com.example.apoc.types.UserStatus;
import com.google.firebase.auth.FirebaseAuth;

public class UserMenu extends AppCompatActivity {

    private final int IMAGE_SIZE = 300;
    public static String USER = "user";
    private User user;
    private UsersDB usersDB;
    private GroupUserDisplay userDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        user = (User) getIntent().getSerializableExtra(USER);

        usersDB = new UsersDB();
        usersDB.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
            @Override
            public void onGetAll() {

            }

            @Override
            public void onGetSpecific() {
                User newUser = (User)usersDB.getItemById(user.getId());
                if(newUser != null){
                    user.copyUserDetails(newUser);
                    userDisplay.updateUserDetails();
                    ((TextView) findViewById(R.id.menu_nickname)).setText(user.getNickName());
                }
            }
        });

        initializeUI();
        if (user.getStatus().equals(UserStatus.undefined.name())) {
            Navigation.openProfileEdit(this, user);
        }

    }



    private void showDetails(){
        userDisplay = new GroupUserDisplay(user, user, new Group("", "", user.getFears()), this);
        userDisplay.setImageSize(IMAGE_SIZE);
        userDisplay.addView((GridLayout) findViewById(R.id.menu_user_display));

        ((TextView) findViewById(R.id.menu_nickname)).setText(user.getNickName());
    }

    private void initializeUI() {
        final Context context = this;

        showDetails();

        ((Button) findViewById(R.id.menu_edit_profile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.openProfileEdit(context, user);
            }
        });


        ((Button) findViewById(R.id.menu_equipment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.openItemsEdit(context, false, user, null, null);
            }
        });


        ((Button) findViewById(R.id.menu_partners_find)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.openPartnersFind(context, user);
            }
        });


        ((Button) findViewById(R.id.menu_group)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.openGroupPage(context, user);
            }
        });

        ((Button) findViewById(R.id.menu_log_out)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });


        if (user.getIsGrouped() || user.getStatus().equals(UserStatus.loneWolf.name())) {
            findViewById(R.id.menu_partners_find).setVisibility(View.GONE);
        }

        if (!user.getIsGrouped() || user.getStatus().equals(UserStatus.loneWolf.name())) {
            findViewById(R.id.menu_group).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        usersDB.loadItemByIdFromDB(user.getId());
    }
}