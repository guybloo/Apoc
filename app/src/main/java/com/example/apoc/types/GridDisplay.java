package com.example.apoc.types;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.apoc.Storage.ImagesDB;

import java.util.ArrayList;

public class GridDisplay {
    private Context context;
    private User user;
    private GridLayout gridLayout;
    private boolean isEdit;
    private int columns;


    public GridDisplay(Context context, User user, GridLayout grid, boolean isEdit, int columns){
        this.context = context;
        this.user = user;
        this.gridLayout = grid;
        this.isEdit = isEdit;
        this.columns = columns;
        grid.setColumnCount(columns);
        if(isEdit)
        {
            createButtons();
        }
        else{
            createViews();
        }
    }

    private void createViews(){
        for(final Fears fear : user.getFears()){
            ImageView imageView = new ImageView(context);
            // add logos
            gridLayout.addView(imageView);
        }

        for(final Skills skill : user.getSkills()){
            ImageView imageView = new ImageView(context);
            // add logos
            gridLayout.addView(imageView);
        }
    }
    private void createButtons(){
        for(final Fears fear : Fears.values()){
            Button btn = new Button(context);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(user.getFears().contains(fear)){
                        user.removeFear(fear);
                    }
                    else{
                        user.addFear(fear);
                    }
                }
            });
            gridLayout.addView(btn);
        }

        for(final Skills skill : Skills.values()){
            Button btn = new Button(context);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(user.getFears().contains(skill)){
                        user.removeSkill(skill);
                    }
                    else{
                        user.addSkill(skill);
                    }
                }
            });
            gridLayout.addView(btn);
        }
    }
}
