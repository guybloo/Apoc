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
    private static final String MSG = "Location services are MANDATORY. Without permission some features won't work.";
    private int CODE = 1;


    private LocationManager locationManager;
    private Context context;
    private LocationInfo info;
    private boolean isTracking;

    /**
     * constructor for location manager
     * @param cnt application context
     */
    public LocationTracker(Context cnt) {
        context = cnt;
        info = new LocationInfo();
        isTracking = false;
//        startTracking();
    }

    /**
     * checks is there is a permission for location, if not, requests.
     * else, start tracking.
     */
    public boolean startTracking() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ask for permission
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE);
            Log.d("Error", "Can't get location permissions");
            return false;
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, Looper.getMainLooper());
//        broadCast(TRACK_START, null);
        isTracking = true;
        return true;
    }

    /**
     * stop location updates
     */
    public void stopTracking() {
        locationManager.removeUpdates(this);
//        broadCast(TRACK_STOP, null);
        isTracking = false;
    }

//    private void broadCast(String msg, LocationInfo data) {
//        Intent intent = new Intent();
//        intent.setAction(msg);
//        intent.putExtra(INTENT_DATA, data);
//        context.sendBroadcast(intent);
//    }

    /**
     * function which called evey location update, updates the location
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        if (!((location.getLongitude() == info.getLongitude()) &&
                (location.getLatitude() == info.getLatitude()) &&
                (location.getAccuracy() == info.getAccuracy()))) {
            info.setParams(location.getLongitude(), location.getLatitude(), location.getAccuracy());
//            broadCast(TRACK_UPDATE, info);
        }
    }

    /**
     * called when the permission request is back
     * @param requestCode location request code
     * @param permission location permission
     * @param grantResults results
     */
    public void onPermissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED && requestCode == CODE) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(MSG)
                        .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                ((Activity) context).finish();
                            }
                        })
                        .create().show();
            }
        } else {
            startTracking();
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

    /**
     * gets the tracking status
     * @return
     */
    public boolean isTracking() {
        return isTracking;
    }

    /**
     * gets the last location
     * @return
     */
    public LocationInfo getInfo() {
        return info;
    }

}

