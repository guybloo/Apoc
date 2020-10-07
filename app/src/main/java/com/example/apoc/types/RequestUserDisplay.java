package com.example.apoc.types;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apoc.DB.RequestsDB;
import com.example.apoc.R;
import com.example.apoc.Storage.ImagesDB;

public class RequestUserDisplay {
    private String SEND_REQUEST = "Send request";
    private String CLOSE = "Close";
    private String SENT = "Request sent to %s";

    private User user;
    private User caller;
    private RelativeLayout.LayoutParams params;
    private View view;
    private RelativeLayout parent;
    private Context context;
    private float distance;
    private AlertDialog.Builder openDetails;
    private AlertDialog dialog;


    public RequestUserDisplay(final User user, User caller, float distance, final Context context){
        this.context = context;
        this.user = user;
        this.caller = caller;
        this.distance = distance;
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view = ((Activity)context).getLayoutInflater().inflate(R.layout.user_display, null);

        setUserDetails();

        ImageView image = view.findViewById(R.id.user_display_image);
        ImagesDB.showCircleImage(user.getImageUrl(),image,context);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    public View getView() {
        return view;
    }

    public User getUser() {
        return user;
    }

    public void setParams(int left, int top) {
        this.params.leftMargin = left;
        this.params.topMargin = top;
    }

    public RelativeLayout.LayoutParams getParams() {
        return params;
    }

    public void addView(RelativeLayout layout){
        if(parent == null) {
            parent = layout;
            parent.addView(view, params);
        }
    }
    public void removeView(){
        ((ViewGroup) parent).removeView(view);
        parent = null;
    }

    public float getDistance() {
        return distance;
    }

    public void setUserDetails ()
    {
        final RequestsDB requestsDB = new RequestsDB();
        openDetails = new AlertDialog.Builder(context);
        View detailsView = ((Activity)context).getLayoutInflater().inflate(R.layout.user_details_display, null);

        GridLayout fearsLayout = detailsView.findViewById(R.id.user_details_fears);
        GridLayout skillsLayout = detailsView.findViewById(R.id.user_details_skills);
        GridDisplay gridDisplay = new GridDisplay(context,user, fearsLayout, skillsLayout, false, 6);

        ((TextView)detailsView.findViewById(R.id.user_details_nickname)).setText(user.getNickName());
        ImagesDB.showCircleImage(user.getImageUrl(),(ImageView)detailsView.findViewById(R.id.user_details_display_image),context);
//        openDetails.setMessage("Are You Sure to delete?");
        //todo add fears and skills
        openDetails.setView(detailsView).setPositiveButton(SEND_REQUEST, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JoinRequest joinRequest = new JoinRequest(caller.getId(), user.getId(), caller.isBeta());
                requestsDB.updateItem(joinRequest);
                Toast.makeText(context, String.format(SENT,user.getId()), Toast.LENGTH_LONG).show();

            }
        });
        openDetails.setNegativeButton(CLOSE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //perform any action
            }
        });
        dialog = openDetails.create();
    }
}
