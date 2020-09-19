package com.example.apoc.location;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


public class LocationTracker implements LocationListener {
    static final String TRACK_STOP = "com.example.im_home.stop";
    static final String TRACK_START = "com.example.im_home.start";
    static final String TRACK_UPDATE = "com.example.im_home.update";
    static final String INTENT_DATA = "data";
    private static final String MSG = "Location services are MANDATORY for the app to work. It's a location based app.";
    private int CODE = 1;


    private LocationManager locationManager;
    private Context context;
    private LocationInfo info;
    private boolean isTracking;

    public LocationTracker(Context cnt) {
        context = cnt;
        info = new LocationInfo();
        isTracking = false;

        if (!(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE);
        }
    }

    public void startTracking() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Error", "Can't get location permissions");
            return;
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, Looper.getMainLooper());
        broadCast(TRACK_START, null);
        isTracking = true;
    }

    public void stopTracking() {
        locationManager.removeUpdates(this);
        broadCast(TRACK_STOP, null);
        isTracking = false;
    }

    private void broadCast(String msg, LocationInfo data) {
        Intent intent = new Intent();
        intent.setAction(msg);
        intent.putExtra(INTENT_DATA, data);
        context.sendBroadcast(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!((location.getLongitude() == info.getLongitude()) &&
                (location.getLatitude() == info.getLatitude()) &&
                (location.getAccuracy() == info.getAccuracy()))) {
            info.setParams(location.getLongitude(), location.getLatitude(), location.getAccuracy());
            broadCast(TRACK_UPDATE, info);
        }
    }

    public void onPermissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED && requestCode == CODE) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(MSG)
                        .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .create().show();

            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public boolean isTracking() {
        return isTracking;
    }

    public LocationInfo getInfo() {
        return info;
    }

}

