package com.example.apoc;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.apoc.DB.DBItem;
import com.example.apoc.DB.DBWrapper;
import com.example.apoc.DB.UsersDB;
import com.example.apoc.location.LocationInfo;
import com.example.apoc.types.User;
import com.example.apoc.Displays.RequestUserDisplay;
import com.example.apoc.Enums.UserStatus;

import java.util.ArrayList;
import java.util.Random;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * PartnersFind class
 */
public class PartnersFind extends AppCompatActivity {

    public static final String USER = "user";
    public static final int MAX_DIST = 50000;

    private User user;
    private ArrayList<RequestUserDisplay> requestUserDisplays;
    private double distance;
    private UsersDB udb;
    private RelativeLayout usersLayout;
    private SeekBar seekbar;

    private Animation rotateAnimation;
    private ImageView radar;
    private TextView distanceText;

    /**
     * starts the PartnersFind activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners_find);
        seekbar = findViewById(R.id.distance_seekbar);
        seekbar.setEnabled(false);
        usersLayout = findViewById(R.id.users_layout);
        user = (User) getIntent().getSerializableExtra(USER);
        distanceText = findViewById(R.id.partners_find_distance);

        requestUserDisplays = new ArrayList<>();
        udb = new UsersDB();
        udb.getAllItems();
        udb.setDataChangeListener(new DBWrapper.OnDataChangeListener() {
            @Override
            public void onGetAll() {
                seekbar.setEnabled(true);
            }

            @Override
            public void onGetSpecific() {

            }
        });
        configSeekbar();

        radar = findViewById(R.id.radar);
        rotateAnimation();
    }

    /**
     * the animation of the radar
     */
    public void rotateAnimation() {
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        radar.startAnimation(rotateAnimation);
    }

    /**
     * refresh the array requestUserDisplays with users to display on the radar,
     * if the current user is alpha it will see betas,
     * if its beta it will see alphas
     */
    private void updateUsers() {
        for (RequestUserDisplay display : requestUserDisplays) {
            display.removeView();
        }
        requestUserDisplays.clear();
        ArrayList<DBItem> allUsers = new ArrayList<>(udb.getItems().values());
        String userStatus;
        if (user.isAlpha()) {
            userStatus = UserStatus.beta.name();
        } else {
            userStatus = UserStatus.alpha.name();
        }
        for (DBItem temp : allUsers) {
            User tempUser = (User) temp;
            float userDistance = getDistance(user.getLocationInfo(), tempUser.getLocationInfo());
            if (!user.getId().equals(temp.getId()) &&
                    userDistance < distance &&
                    tempUser.getStatus().equals(userStatus) &&
                    !(tempUser.getIsGrouped() && tempUser.isBeta())) {
                requestUserDisplays.add(new RequestUserDisplay((User) temp, user, userDistance, this));
            }
        }
        showUsers();
    }

    /**
     * display the users from requestUserDisplays with consideration of their distance
     * from the current user
     */
    private void showUsers() {
        for (RequestUserDisplay display : requestUserDisplays) {
            double random = ((new Random()).nextDouble() * 2 * Math.PI);
            double radius = display.getDistance() / distance;
            int xCenter = usersLayout.getWidth() / 2;
            int yCenter = usersLayout.getHeight() / 2;
            int x = getXPos( radius * xCenter, random, xCenter) - (display.getImage().getLayoutParams().width / 2);
            int y = getYPos( radius * yCenter, random, yCenter) - (display.getImage().getLayoutParams().height / 2);
            display.setParams(x, y);
            display.addView(usersLayout);
        }
    }

    /**
     * calculates a random x position on the circles around the current user
     * @param radius the relative distance of the current user from the other user to display
     *               for the xCenter
     * @param theta random number
     * @param center the xCenter of the layout
     * @return
     */
    private int getXPos(double radius, double theta, int center) {
        return (int) (radius * Math.cos(theta) + center);
    }

    /**
     * calculates a random y position on the circles around the current user
     * @param radius the relative distance of the current user from the other user to display
     *               for the yCenter
     * @param theta random number
     * @param center the yCenter of the layout
     * @return
     */
    private int getYPos(double radius, double theta, int center) {
        return (int) (radius * Math.sin(theta) + center);
    }

    /**
     * changes the distance from the current user to look for other users
     */
    private void configSeekbar() {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance = ((double) progress / 100.0) * MAX_DIST;
                updateUsers();
                String kind = "m";
                double dist = distance;
                if (distance >= 1000) {
                    dist /= 1000;
                    kind = "km";
                }
                distanceText.setText(String.format("%.2f %s", dist, kind));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * returns the calculated distance between the current user and the other user
     * @param base - main/current user
     * @param dest - other user
     * @return
     */
    private float getDistance(LocationInfo base, LocationInfo dest) {
        float[] res = new float[1];
        if (user != null && dest != null) {
            Location.distanceBetween(base.getLatitude(), base.getLongitude(),
                    dest.getLatitude(), dest.getLongitude(), res);
        }
        return res[0];
    }
}