package com.example.apoc.types;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apoc.R;
import com.example.apoc.Storage.ImagesDB;

public class UserDisplay {
    private User user;
    private RelativeLayout.LayoutParams params;
    private View view;
    private RelativeLayout parent;
    private Context context;

    public UserDisplay(final User user, final Context context){
        this.context = context;
        this.user = user;
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view = ((Activity)context).getLayoutInflater().inflate(R.layout.user_display, null);

        ImageView image = view.findViewById(R.id.user_display_image);
        ImagesDB.showImage(user.getImageUrl(),image,context);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, user.getEmail(),Toast.LENGTH_SHORT).show();
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
}
