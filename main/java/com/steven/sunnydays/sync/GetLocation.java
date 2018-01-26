package com.steven.sunnydays.sync;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import com.steven.sunnydays.utils.WeatherPreference;
import com.steven.sunnydays.utils.WeatherSync;

/**
 * Created by Steven on 2018-01-22.
 */

public class GetLocation {

    private static final String TAG =GetLocation.class.getSimpleName() ;

    private static LocationManager locationManager;
    private boolean mGetWeather;
    private static Context mContext;


    public static void requestlocation(Context context) {
        mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1, mLocationListener);

    }


    private static final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            double mlatitude = location.getLatitude();
            double mlongitude = location.getLongitude();
            //위치정보 모니터링 제거
            locationManager.removeUpdates(this);
            WeatherPreference.setLocation(mContext, mlatitude, mlongitude);
//            WeatherSync weatherSync = new WeatherSync();
//            weatherSync.Intialize(MainActivity.this,mlatitude,mlongitude);
            Log.e(TAG, "onLocationChanged");

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {        }
        @Override
        public void onProviderEnabled(String provider) {        }
        @Override
        public void onProviderDisabled(String provider) {        }
    };
}