package com.steven.sunnydays.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;
import com.steven.sunnydays.sync.WeatherJobService;
import com.steven.sunnydays.sync.WeatherSyncService;

/**
 * Created by Steven on 2018-01-16.
 */

public class WeatherSync {

    private static final String TAG = WeatherSync.class.getSimpleName();
    //    private static boolean firstinLifetime;
    private static boolean mfirst;
    public double mlat;
    private double mlong;
    LocationManager locationManager;

    //    public void Intialize(Context context, double mlatitude, double mlongitude){
    public void Intialize(Context context) {

        doBookScheduleJobtoSync(context);
        requestlocation(context);
//        startSyncService(context);
    }

    private void doBookScheduleJobtoSync(Context context) {

        //Firebase Job Dispatch 3 hours ~ 4 hours sync
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        Job syncJob = firebaseJobDispatcher.newJobBuilder()
                .setService(WeatherJobService.class)
                .setTag("sunnydays").setConstraints(Constraint.ON_ANY_NETWORK)
                .setRecurring(true)
                .setTrigger((Trigger.executionWindow(3 * 60 * 60, 4 * 60 * 60)))
//                .setTrigger((Trigger.executionWindow(30, 40)))
                .setReplaceCurrent(true)
                .build();
        firebaseJobDispatcher.schedule(syncJob);

    }

    private void requestlocation(final Context context) {


        Thread requstlocation = new Thread(new Runnable() {
            @Override
            public void run() {

                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, mLocationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1, mLocationListener);
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60 * 60 * 1000, 1, mLocationListener);
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60 * 60 * 1000, 1, mLocationListener);
                }

            }

            private final LocationListener mLocationListener = new LocationListener() {
                private boolean first_in_Lifetime;

                @Override
                public void onLocationChanged(Location location) {

                    double mlatitude = location.getLatitude();
                    double mlongitude = location.getLongitude();
                    //Stop Monitoring
                    locationManager.removeUpdates(this);
                    WeatherPreference.setLocation(context, mlatitude, mlongitude);
                    if (!mfirst) {
                        mfirst = true;
                        startSyncService(context);
                    }
                    Log.e(TAG, "onLocationChanged");
                }
                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) { }
                @Override
                public void onProviderEnabled(String s) {  }
                @Override
                public void onProviderDisabled(String s) {  }
            };
        });

        requstlocation.run();

    }

    private void startSyncService(Context context) {

        Intent intent = new Intent(context, WeatherSyncService.class);
//        intent.putExtra("lat",mlat);
//        intent.putExtra("long", mlong);
        context.startService(intent);
    }


}
