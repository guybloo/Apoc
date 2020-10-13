package com.postpc.apoc.Displays;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;

import com.postpc.apoc.Enums.Fears;
import com.postpc.apoc.Enums.Skills;
import com.postpc.apoc.R;
import com.postpc.apoc.types.User;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

/**
 * displays fears and skills
 */
public class GridDisplay {
    private final int ICON_SIZE = 50;
    private Context context;
    private User user;
    private GridLayout fearsLayout;
    private GridLayout skillsLayout;

    /**
     * constructor
     * @param context the activity
     * @param user user to show
     * @param fears layout to show fears
     * @param skills layout to show skills
     * @param isEdit boolean if to show buttons of textViews
     * @param columns number of cols in the grid
     */
    public GridDisplay(Context context, User user, GridLayout fears, GridLayout skills, boolean isEdit, int columns){
        this.context = context;
        this.user = user;
        this.fearsLayout = fears;
        this.skillsLayout = skills;
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

    /**
     * create views - only the specific fears and skills
     */
    private void createViews(){
        for(final Fears fear : user.getFears()){
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

    /**
     * gets specific fear icon by its enum
     * @param fear the specific fear
     * @return the icon
     */
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

    /**
     * gets specific skill icon from enum
     * @param skill the specific skill
     * @return icon
     */
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

    /**
     * creates buttons of all fears and skills for editing
     */
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
