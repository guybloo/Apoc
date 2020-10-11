package com.example.apoc.Displays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apoc.R;
import com.example.apoc.Storage.ImagesDB;
import com.example.apoc.types.Group;
import com.example.apoc.types.User;

/**
 * shows user details in group
 */
public class GroupUserDisplay {
    // user is the one who sees this page, other user is another group member to show
    private User user;
    private User otherUser;
    private Group group;
    private RelativeLayout.LayoutParams params;
    private View view;
    private GridLayout parent;
    private Context context;
    private AlertDialog.Builder openDetails;
    private AlertDialog dialog;
    private onDeleteListener listener;

    /**
     * constructor
     * @param user the one who sees this page
     * @param otherUser the user to show
     * @param group group item
     * @param context the activity
     */
    public GroupUserDisplay( User user, User otherUser, Group group,  final Context context){
        this.group = group;
        this.context = context;
        this.otherUser = otherUser;
        this.user = user;
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view = ((Activity)context).getLayoutInflater().inflate(R.layout.user_display, null);

        updateUserDetails();

        ImageView image = view.findViewById(R.id.user_display_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    /**
     * sets the images size
     * @param size the wanted size
     */
    public void setImageSize(int size){
        ImageView image = view.findViewById(R.id.user_display_image);

       image.getLayoutParams().height = size;
       image.getLayoutParams().width = size;
    }

    /**
     * event interface
     */
    public interface onDeleteListener {
        void onDelete(User user);
    }

    /**
     * sets the listener
     * @param listener
     */
    public void setOnDeleteListener(GroupUserDisplay.onDeleteListener listener) {
        this.listener = listener;
    }

    /**
     * gets the view
     * @return the specific view layout
     */
    public View getView() {
        return view;
    }

    /**
     * gets other user
     * @return the user
     */
    public User getOtherUser() {
        return otherUser;
    }

    /**
     * sets margin params
     * @param left from left
     * @param top from top
     */
    public void setParams(int left, int top) {
        this.params.leftMargin = left;
        this.params.topMargin = top;
    }

    /**
     * gets the layout params
     * @return
     */
    public RelativeLayout.LayoutParams getParams() {
        return params;
    }

    /**
     * adds the view to the layout
     * @param layout
     */
    public void addView(GridLayout layout){
        if(parent == null) {
            parent = layout;
            parent.addView(view, params);
        }
    }

    /**
     * remove this view from the layout
     */
    public void removeView(){
        ((ViewGroup) parent).removeView(view);
        parent = null;
    }

    /**
     * updates the user details in the view
     */
    public void updateUserDetails()
    {
        ImagesDB.showCircleImage(otherUser.getImageUrl(),(ImageView)view.findViewById(R.id.user_display_image),context);
        openDetails = new AlertDialog.Builder(context);
        View detailsView = ((Activity)context).getLayoutInflater().inflate(R.layout.request_view, null);

        GridLayout fearsLayout = detailsView.findViewById(R.id.request_fears);
        GridLayout skillsLayout = detailsView.findViewById(R.id.request_skills);
        GridDisplay gridDisplay = new GridDisplay(context,user, fearsLayout, skillsLayout, false, 6);

        detailsView.findViewById(R.id.request_approve).setVisibility(View.GONE);
        if(!user.getId().equals(group.getId())){
            detailsView.findViewById(R.id.request_delete).setVisibility(View.GONE);
        }
        else{
            ((Button)detailsView.findViewById(R.id.request_delete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDelete(otherUser);
                        dialog.dismiss();
                    }
                }
            });
        }

        ((TextView)detailsView.findViewById(R.id.request_nick_name)).setText(otherUser.getNickName());
        ((TextView)detailsView.findViewById(R.id.request_phone)).setText(otherUser.getPhone());
        ((TextView)detailsView.findViewById(R.id.request_email)).setText(otherUser.getEmail());

        ImagesDB.showCircleImage(otherUser.getImageUrl(),(ImageView)detailsView.findViewById(R.id.request_image),context);
//        openDetails.setMessage("Are You Sure to delete?");
        openDetails.setView(detailsView);
        dialog = openDetails.create();
    }
}
