package com.steven.sunnydays.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.steven.sunnydays.data.Contract;
import com.steven.sunnydays.utils.OpenWeatherJsonUtils;
import com.steven.sunnydays.utils.WeatherPreference;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * Created by Steven on 2018-01-16.
 */

public class GetWeather {

    private static final String openweatherurl = "http://api.openweathermap.org/data/2.5/forecast";
    public static final String Apiuxurl = "";
    private static final String BASE_URL_DAILY = "https://api.openweathermap.org/data/2.5/forecast/daily";
    public static final String BASE_URL_3HOURS = "https://api.openweathermap.org/data/2.5/forecast";

    public static final String BASE_URL = BASE_URL_DAILY;

    private static final String BASE_URL_CURRENT = "http://api.openweathermap.org/data/2.5/weather";
    //    private static final String BASE_URL_DAILY = "http://api.openweathermap.org/data/2.5/forecast/daily";
    private static final String format = "json";
    private static final String unitsmetric = "metric";
    private static final String units = "imperial";
    private static final int numDays = 16;
    private static final String QUERY_PARAM = "q";
    private static final String LAT_PARAM = "lat";
    private static final String LON_PARAM = "lon";
    private static final String FORMAT_PARAM = "mode";
    private static final String UNITS_PARAM = "units";
    private static final String DAYS_PARAM = "cnt";
    private static final String APPID = "APPID";
    private static final String openid = "xxxxxxxxxx";
    private static final String TAG = GetWeather.class.getSimpleName();


    //    public static void getData(WeatherSyncService weatherSyncService, Double latitude, Double longtitude) {
    public static void getData(Context weatherSyncService) {


        try {

            ContentValues[] weatherdata;
            URL requestUrl = getUri(weatherSyncService);

            String jsonWeatherResponse = getResponseFromHttpUrl(requestUrl);

                weatherdata = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(weatherSyncService, jsonWeatherResponse);

            if (weatherdata != null && weatherdata.length != 0) {
                ContentResolver contentResolver = weatherSyncService.getContentResolver();
                contentResolver.delete(Contract.TableEntry.CONTENT_URI, null, null);

                contentResolver.bulkInsert(Contract.TableEntry.CONTENT_URI, weatherdata);
                Log.e(TAG, "Weather Data Inserted");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    private static URL getUri(WeatherSyncService weatherSyncService, Double latitude, Double longtitude) {
    private static URL getUri(Context weatherSyncService) {

        if (WeatherPreference.isUseTemp(weatherSyncService)) {
            return buildUrlWithLocationQuery("Issaquah, WA 98029");

        } else {
            return buildUrlWithLatitudeLongitude(WeatherPreference.getLatitude(weatherSyncService),
                    WeatherPreference.getLongtitude(weatherSyncService));
        }
    }

    private static URL buildUrlWithLatitudeLongitude(Double latitude, Double longitude) {
        Uri weatherQueryUri;

        if (BASE_URL == BASE_URL_3HOURS) {
            weatherQueryUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(LAT_PARAM, String.valueOf(latitude))
                    .appendQueryParameter(LON_PARAM, String.valueOf(longitude))
//                .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(APPID, openid)
                    .build();
        } else if (BASE_URL == BASE_URL_DAILY) {
            weatherQueryUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(LAT_PARAM, String.valueOf(latitude))
                    .appendQueryParameter(LON_PARAM, String.valueOf(longitude))
//                .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                    .appendQueryParameter(APPID, openid)
                    .build();
        } else if (BASE_URL == Apiuxurl){
            weatherQueryUri = Uri.parse(Apiuxurl);
        }

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
//            URL weatherQueryUrl = new URL(Apiuxurl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static URL buildUrlWithLocationQuery(String locationQuery) {
        Uri weatherQueryUri = Uri.parse(BASE_URL_DAILY).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
