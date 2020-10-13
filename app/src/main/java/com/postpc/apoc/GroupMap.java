package com.postpc.apoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.postpc.apoc.types.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;


/**
 * groupMap class
 */
public class GroupMap extends FragmentActivity implements OnMapReadyCallback {

    private final int PIN_SIZE = 100;
    public static String GROUPIES = "groupies";
    public static String USER = "user";
    private GoogleMap mMap;
    private ArrayList<User> groupies;
    private User user;

    /**
     * kick start google map
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        groupies = (ArrayList<User>)getIntent().getSerializableExtra(GROUPIES);
        user = (User)getIntent().getSerializableExtra(USER);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style));

        for(User groupie:groupies){
            if(user.getId().equals(groupie.getId())){
                try {
                    addPosition(groupie, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    addPosition(groupie, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * creates the images of the group members on the map
     * @param user the user to display
     * @param setCamera
     * @throws IOException
     */
    private void addPosition(User user, final boolean setCamera) throws IOException {
        final LatLng position = new LatLng(user.getLocationInfo().getLatitude(), user.getLocationInfo().getLongitude());
        final MarkerOptions options = new MarkerOptions().position(position).title(user.getNickName());

        Glide.with(this)
                .asBitmap()
                .load(user.getImageUrl()).apply(RequestOptions.circleCropTransform())
                .into(new CustomTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap bitmap = Bitmap.createScaledBitmap(resource,PIN_SIZE,PIN_SIZE,true);
                        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        mMap.addMarker(options);
                        if(setCamera){
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                        }

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}