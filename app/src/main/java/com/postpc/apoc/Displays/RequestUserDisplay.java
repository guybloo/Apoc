package com.postpc.apoc.Displays;

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
import android.widget.Toast;

import com.postpc.apoc.DB.RequestsDB;
import com.postpc.apoc.R;
import com.postpc.apoc.Storage.ImagesDB;
import com.postpc.apoc.types.JoinRequest;
import com.postpc.apoc.types.User;

/**
 * shows request user details
 */
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
    private ImageView image;

    /**
     * constructor
     * @param user the user to show
     * @param caller the current user
     * @param distance the distance between then
     * @param context the activity
     */
    public RequestUserDisplay(final User user, User caller, float distance, final Context context){
        this.context = context;
        this.user = user;
        this.caller = caller;
        this.distance = distance;
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view = ((Activity)context).getLayoutInflater().inflate(R.layout.user_display, null);

        setUserDetails();

        image = view.findViewById(R.id.user_display_image);
        ImagesDB.showCircleImage(user.getImageUrl(),image,context);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    /**
     * return the current details view
     * @return the view
     */
    public View getView() {
        return view;
    }

    /**
     * gets the user imageview
     * @return
     */
    public ImageView getImage() {
        return image;
    }

    /**
     * gets the current user
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * sets the layout margins params
     * @param left from left
     * @param top from top
     */
    public void setParams(int left, int top) {
        this.params.leftMargin = left;
        this.params.topMargin = top;
    }

    /**
     * gets the layout params
     * @return params
     */
    public RelativeLayout.LayoutParams getParams() {
        return params;
    }

    /**
     * adds the view to the input layout
     * @param layout the layout to add the view to
     */
    public void addView(RelativeLayout layout){
        if(parent == null) {
            parent = layout;
            parent.addView(view, params);
        }
    }

    /**
     * removed the user details from the view
     */
    public void removeView(){
        ((ViewGroup) parent).removeView(view);
        parent = null;
    }

    /**
     * gets the ditance from the current user
     * @return
     */
    public float getDistance() {
        return distance;
    }

    /**
     * sets the user details in the view
     */
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
        ((Button)detailsView.findViewById(R.id.user_details_request)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinRequest joinRequest = new JoinRequest(caller.getId(), user.getId(), caller.isBeta());
                requestsDB.updateItem(joinRequest);
                Toast.makeText(context, String.format(SENT,user.getId()), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        ((Button)detailsView.findViewById(R.id.user_details_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        openDetails.setView(detailsView);
        dialog = openDetails.create();
    }
}
