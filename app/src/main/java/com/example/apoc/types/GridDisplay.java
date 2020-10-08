package com.example.apoc.types;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.apoc.R;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

public class GridDisplay {
    private final int ICON_SIZE = 50;
    private Context context;
    private User user;
    private GridLayout fearsLayout;
    private GridLayout skillsLayout;
    private boolean isEdit;
    private int columns;


    public GridDisplay(Context context, User user, GridLayout fears, GridLayout skills, boolean isEdit, int columns){
        this.context = context;
        this.user = user;
        this.fearsLayout = fears;
        this.skillsLayout = skills;
        this.isEdit = isEdit;
        this.columns = columns;
        fearsLayout.setColumnCount(columns);
        skills.setColumnCount(columns);
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
//            ImageView imageView = new ImageView(context);
            // add logos
            MaterialIconView icon = new MaterialIconView(context);
            icon.setIcon(getFearIcon(fear));
            icon.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
            fearsLayout.addView(icon);
        }

        for(final Skills skill : user.getSkills()){
            MaterialIconView icon = new MaterialIconView(context);
            icon.setIcon(getSkillIcon(skill));
            icon.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
            skillsLayout.addView(icon);
        }
    }

    private MaterialDrawableBuilder.IconValue getFearIcon(Fears fear){
        switch (fear){
            case War:
                return MaterialDrawableBuilder.IconValue.SWORD_CROSS;
            case Flood:
                return MaterialDrawableBuilder.IconValue.WAVES;
            case Zombies:
                return MaterialDrawableBuilder.IconValue.SKULL;
            case Pandemic:
                return MaterialDrawableBuilder.IconValue.NEEDLE;
            case Hurricane:
                return MaterialDrawableBuilder.IconValue.WEATHER_WINDY;
            case Radioactive:
                return MaterialDrawableBuilder.IconValue.RADIOACTIVE;
        }
        return MaterialDrawableBuilder.IconValue.CURSOR_DEFAULT;
    }
    private MaterialDrawableBuilder.IconValue getSkillIcon(Skills skill){
        switch (skill){
            case fire:
                return MaterialDrawableBuilder.IconValue.FIRE;

            case knife:
                return MaterialDrawableBuilder.IconValue.SWORD;

            case weapon:
                return MaterialDrawableBuilder.IconValue.PISTOL;

            case cooking:
                return MaterialDrawableBuilder.IconValue.POT_MIX;

            case fishing:
                return MaterialDrawableBuilder.IconValue.FISH;

            case hunting:
                return MaterialDrawableBuilder.IconValue.PAW;

            case firstAid:
                return MaterialDrawableBuilder.IconValue.HOSPITAL;

            case lashings:
                return MaterialDrawableBuilder.IconValue.GESTURE;

            case mapReading:
                return MaterialDrawableBuilder.IconValue.MAP;

            case agriculture:
                return MaterialDrawableBuilder.IconValue.NATURE_PEOPLE;

            case martialArts:
                return MaterialDrawableBuilder.IconValue.NINJA;

            case ediblePlants:
                return MaterialDrawableBuilder.IconValue.LEAF;

        }
        return MaterialDrawableBuilder.IconValue.CURSOR_DEFAULT;
    }

    private void createButtons(){
        final int primary = context.getResources().getColor(R.color.colorPrimary);
        final int primaryDark = context.getResources().getColor(R.color.colorPrimaryDark);
        for(final Fears fear : Fears.values()){
            final MaterialIconView icon = new MaterialIconView(context,null,R.style.IconStyle);
            icon.setIcon(getFearIcon(fear));
            icon.setSizeDp(ICON_SIZE);
            if(user.getFears().contains(fear)){
                icon.setColor(primary);
            }
            else {
                icon.setColor(primaryDark);
            }
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(user.getFears().contains(fear)){
                        user.removeFear(fear);
                        icon.setColor(primaryDark);

                    }
                    else{
                        user.addFear(fear);
                        icon.setColor(primary);

                    }
                }
            });
            fearsLayout.addView(icon);
        }

        for(final Skills skill : Skills.values()){
            final MaterialIconView icon = new MaterialIconView(context,null,R.style.IconStyle);
            icon.setIcon(getSkillIcon(skill));
            icon.setSizeDp(ICON_SIZE);
            if(user.getSkills().contains(skill)){
                icon.setColor(primary);
            }
            else {
                icon.setColor(primaryDark);
            }
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(user.getSkills().contains(skill)){
                        user.removeSkill(skill);
                        icon.setColor(primaryDark);

                    }
                    else{
                        user.addSkill(skill);
                        icon.setColor(primary);

                    }
                }
            });
            skillsLayout.addView(icon);
        }
    }
}
