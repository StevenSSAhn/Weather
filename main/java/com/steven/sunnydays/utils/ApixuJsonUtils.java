package com.steven.sunnydays.utils;

import android.content.ContentValues;
import android.content.Context;

import com.steven.sunnydays.data.Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public final class ApixuJsonUtils {


    public static ContentValues[] getWeatherContentValuesFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        JSONObject rootJson = new JSONObject(forecastJsonStr);
        JSONObject forecastJson = rootJson.getJSONObject("forecast");
        JSONArray jsonWeatherArray = forecastJson.getJSONArray("forecastday");

        JSONObject locationJson = rootJson.getJSONObject("location");

        String cityName = locationJson.getString("name");
        double cityLatitude = locationJson.getDouble("lat");
        double cityLongitude = locationJson.getDouble("lon");

        ContentValues[] weatherContentValues = new ContentValues[jsonWeatherArray.length()];


        for (int i = 0; i < jsonWeatherArray.length(); i++) {

            long dateTimeMillis;
            double pressure;
            int humidity;
            double windSpeed;
            double windDirection;
            double high;
            double low;
            int code;

            JSONObject dayForecast = jsonWeatherArray.getJSONObject(i);
            dateTimeMillis = dayForecast.getLong("dt") * 1000;

            JSONObject day = dayForecast.getJSONObject("day");
            JSONObject condition= day.getJSONObject("condition");
            code = condition.getInt("code");

            humidity = day.getInt("avghumidity");
            pressure = day.getDouble("totalprecip_in");
            windSpeed = day.getDouble("maxwind_mph");
            windDirection = day.getDouble("maxwind_kph");

            high = day.getInt("maxtemp_f");
            low = day.getInt("mintemp_f");

            ContentValues weatherValues = new ContentValues();
            weatherValues.put(Contract.TableEntry.COL_DATE, dateTimeMillis);
            weatherValues.put(Contract.TableEntry.COL_HUMIDITY, humidity);
            weatherValues.put(Contract.TableEntry.COL_PRESSURE, pressure);
            weatherValues.put(Contract.TableEntry.COL_WIND_SPEED, windSpeed);
            weatherValues.put(Contract.TableEntry.COL_DEGREES, windDirection);
            weatherValues.put(Contract.TableEntry.COL_MAX, high);
            weatherValues.put(Contract.TableEntry.COL_MIN, low);
            weatherValues.put(Contract.TableEntry.COL_WEATHER_ID, code);

            weatherContentValues[i] = weatherValues;
        }
        return weatherContentValues;
    }
}