package com.postpc.apoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.postpc.apoc.DB.DBWrapper;
import com.postpc.apoc.DB.UsersDB;
import com.postpc.apoc.types.Group;
import com.postpc.apoc.Displays.GroupUserDisplay;
import com.postpc.apoc.types.User;
import com.postpc.apoc.Enums.UserStatus;
import com.google.firebase.auth.FirebaseAuth;

/**
 * UserMenu class
 */
public class UserMenu extends AppCompatActivity {

    private final int IMAGE_SIZE = 400;
    public static String USER = "user";
    private User user;
    private UsersDB usersDB;
    private GroupUserDisplay userDisplay;

    /**
     * starts the UserMenu activity
     * @param savedInstanceState
     */
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
                User newUser = (User) usersDB.getItemById(user.getId());
                if (newUser != null) {
                    user.copyUserDetails(newUser);
                    userDisplay.updateUserDetails();
                    ((TextView) findViewById(R.id.menu_nickname)).setText(user.getNickName());
                    buttonsUpdate();
                }
            }
        });

        initializeUI();
        if (user.isUndefined()) {
            Navigation.openProfileEdit(this, user);
        }
    }

    /**
     * initialize the page of the user menu page
     */
    private void initializeUI() {
        showDetails();
        initializeNavigation(this);
        buttonsUpdate();
    }

    /**
     * initialize the ui of the user menu page - image and nickname
     */
    private void showDetails() {
        userDisplay = new GroupUserDisplay(user, user, new Group("", "", user.getFears()), this);
        userDisplay.setImageSize(IMAGE_SIZE);
        userDisplay.addView((GridLayout) findViewById(R.id.menu_user_display));

        ((TextView) findViewById(R.id.menu_nickname)).setText(user.getNickName());
    }

    /**
     * initialize the buttons of the user menu page
     * @param context
     */
    private void initializeNavigation(final Context context){
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

        ((Button) findViewById(R.id.menu_requests)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.openJoinRequests(context, user);
            }
        });

        ((Button) findViewById(R.id.menu_log_out)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }

    /**
     * sets the visibility of the buttons that related to group/lone wolf only
     */
    private void buttonsUpdate(){
        if ((user.getIsGrouped() && user.isBeta()) || user.getStatus().equals(UserStatus.loneWolf.name())) {
            findViewById(R.id.menu_partners_find).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.menu_partners_find).setVisibility(View.VISIBLE);

        }

        if (!user.getIsGrouped() || user.isLoneWolf()) {
            findViewById(R.id.menu_group).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.menu_group).setVisibility(View.VISIBLE);

        }

        if((user.getIsGrouped() && user.isBeta()) || user.getStatus().equals(UserStatus.loneWolf.name())){
            findViewById(R.id.menu_requests).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.menu_requests).setVisibility(View.VISIBLE);

        }
    }

    /**
     * loads the usersDB
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        usersDB.loadItemByIdFromDB(user.getId());
    }
}