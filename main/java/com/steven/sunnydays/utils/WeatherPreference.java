package com.steven.sunnydays.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Steven on 2018-01-17.
 */

public class WeatherPreference {

    public static final String COORD_LAT = "coord_lat";
    public static final String COORD_LONG = "coord_long";
    public static final String USETEMP = "usetemp";



    public static void setLocation(Context context, double lat, double lon) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        long ltemp = Double.doubleToRawLongBits(lat);
        editor.putLong(COORD_LAT, ltemp);
        editor.putLong(COORD_LONG, Double.doubleToRawLongBits(lon));
        editor.apply();
    }

    public static void setCityName(Context context, String cityname, int population) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("cityname", cityname);
        editor.putInt("population", population);
        editor.apply();
    }

    public static String getCityName(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String cityname = sp.getString("cityname", "Sunny");
        return cityname;
    }

    public static int getPopulation(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int population = sp.getInt("population", 10000);
        return population;
    }

    public static Double getLatitude(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long aLong = sp.getLong(COORD_LAT, 42);
        Double value = Double.longBitsToDouble(aLong);
        return value;
    }
    public static Double getLongtitude(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long aLong = sp.getLong(COORD_LONG, -142);
        Double value = Double.longBitsToDouble(aLong);
        return value;
    }


    public void useTestServer(Context context, Boolean b) {
        SharedPreferences useTest = PreferenceManager.getDefaultSharedPreferences(context);
        useTest.edit().putBoolean(USETEMP, b);
    }

    public static boolean isUseTemp(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(USETEMP, false);
    }

    public static boolean isLocationAvailable(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        boolean spContainLatitude = sp.contains(COORD_LAT);
        boolean spContainLongitude = sp.contains(COORD_LONG);

        return spContainLatitude && spContainLongitude;
    }

}
