package com.example.apoc.types;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.apoc.GroupPage;
import com.example.apoc.ItemsEdit;
import com.example.apoc.JoinRequests;
import com.example.apoc.PartnersFind;
import com.example.apoc.ProfileEdit;
import com.example.apoc.Registration;

public class Navigation {

    public static final int REGISTERATION_CODE = 1;
    public static final int PROFILE_EDIT_CODE = 2;
    public static final int ITEMS_EDIT_CODE = 3;
    public static final int PARTNERS_FIND_EDIT_CODE = 4;
    public static final int JOIN_REQUESTS_EDIT_CODE = 5;
    public static final int GROUP_PAGE_EDIT_CODE = 6;

    public static void openRegistration(Context context) {
        Intent RegisterIntent = new Intent(context, Registration.class);
        ((Activity) context).startActivityForResult(RegisterIntent, REGISTERATION_CODE);
    }

    public static void openProfileEdit(Context context, User user) {
        Intent intent = new Intent(context, ProfileEdit.class);
        intent.putExtra(ProfileEdit.USER_DATA, user);
        ((Activity) context).startActivityForResult(intent, PROFILE_EDIT_CODE);
    }

    public static void openItemsEdit(Context context, boolean isGroup, User user, Group group) {
        Intent intent = new Intent(context, ItemsEdit.class);
        intent.putExtra(ItemsEdit.USERS, isGroup ? group : user);
        intent.putExtra(ItemsEdit.IS_GROUP, isGroup);
        ((Activity) context).startActivityForResult(intent, ITEMS_EDIT_CODE);
    }

    public static void openPartnersFind(Context context, User user){
        Intent intent = new Intent(context, PartnersFind.class);
        intent.putExtra(PartnersFind.USER, user);
        ((Activity) context).startActivityForResult(intent, PARTNERS_FIND_EDIT_CODE);
    }

    public static void openJoinRequests(Context context, User user){
        Intent intent = new Intent(context, JoinRequests.class);
        intent.putExtra(JoinRequests.USER, user);
        ((Activity) context).startActivityForResult(intent, JOIN_REQUESTS_EDIT_CODE);
    }
    public static void openGroupPage(Context context, User user){
        Intent intent = new Intent(context, GroupPage.class);
        intent.putExtra(GroupPage.USER, user);
        ((Activity) context).startActivityForResult(intent, GROUP_PAGE_EDIT_CODE);
    }
}
