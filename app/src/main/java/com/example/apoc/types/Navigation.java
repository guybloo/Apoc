package com.example.apoc.types;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.apoc.GroupMap;
import com.example.apoc.GroupPage;
import com.example.apoc.ItemsEdit;
import com.example.apoc.JoinRequests;
import com.example.apoc.PartnersFind;
import com.example.apoc.ProfileEdit;
import com.example.apoc.Registration;
import com.example.apoc.UserMenu;

import java.util.ArrayList;

public class Navigation {

    public static final int REGISTERATION_CODE = 1;
    public static final int PROFILE_EDIT_CODE = 2;
    public static final int ITEMS_CODE = 3;
    public static final int PARTNERS_FIND_CODE = 4;
    public static final int JOIN_REQUESTS_CODE = 5;
    public static final int GROUP_PAGE_CODE = 6;
    public static final int GROUP_MAP_CODE = 7;
    public static final int MENU_CODE = 8;

    public static void openRegistration(Context context) {
        Intent RegisterIntent = new Intent(context, Registration.class);
        ((Activity) context).startActivityForResult(RegisterIntent, REGISTERATION_CODE);
    }

    public static void openProfileEdit(Context context, User user) {
        Intent intent = new Intent(context, ProfileEdit.class);
        intent.putExtra(ProfileEdit.USER_DATA, user);
        ((Activity) context).startActivityForResult(intent, PROFILE_EDIT_CODE);
    }

    public static void openItemsEdit(Context context, boolean isGroup, User user, Group group, ArrayList<User> groupies) {
        Intent intent = new Intent(context, ItemsEdit.class);
        intent.putExtra(ItemsEdit.USERS, isGroup ? group : user);
        intent.putExtra(ItemsEdit.IS_GROUP, isGroup);
        intent.putExtra(ItemsEdit.GROUPIES, groupies);
        ((Activity) context).startActivityForResult(intent, ITEMS_CODE);
    }

    public static void openPartnersFind(Context context, User user){
        Intent intent = new Intent(context, PartnersFind.class);
        intent.putExtra(PartnersFind.USER, user);
        ((Activity) context).startActivityForResult(intent, PARTNERS_FIND_CODE);
    }

    public static void openJoinRequests(Context context, User user){
        Intent intent = new Intent(context, JoinRequests.class);
        intent.putExtra(JoinRequests.USER, user);
        ((Activity) context).startActivityForResult(intent, JOIN_REQUESTS_CODE);
    }
    public static void openGroupPage(Context context, User user){
        Intent intent = new Intent(context, GroupPage.class);
        intent.putExtra(GroupPage.USER, user);
        ((Activity) context).startActivityForResult(intent, GROUP_PAGE_CODE);
    }
    public static void openMenu(Context context, User user){
        Intent intent = new Intent(context, UserMenu.class);
        intent.putExtra(UserMenu.USER, user);
        ((Activity) context).startActivityForResult(intent, MENU_CODE);
    }

    public static void openGroupMap(Context context, ArrayList<User> users, User user){
        Intent intent = new Intent(context, GroupMap.class);
        intent.putExtra(GroupMap.GROUPIES, users);
        intent.putExtra(GroupMap.USER, user);
        ((Activity) context).startActivityForResult(intent, GROUP_MAP_CODE);
    }
}
