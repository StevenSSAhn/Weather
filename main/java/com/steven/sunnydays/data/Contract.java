package com.steven.sunnydays.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.steven.sunnydays.utils.WeatherUtil;

/**
 * Created by Steven on 2018-01-16.
 */

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.steven.sunnydays";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class TableEntry implements BaseColumns{

        public static final String TABLE_NAME = "weather";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("weather").build();

        public static final String COL_DATE = "date";
        public static final String COL_WEATHER_ID = "weather_id";
        public static final String COL_MIN = "min";
        public static final String COL_MAX = "max";
        public static final String COL_HUMIDITY = "humidity";
        public static final String COL_PRESSURE = "pressure";
        public static final String COL_WIND_SPEED = "wind";
        public static final String COL_DEGREES = "degrees";

        public static final String[] WEATHER_PROJECTION = {
            COL_DATE,COL_MAX,COL_MIN,COL_WEATHER_ID
        };

        public static final String[] WEATHER_DETAIL_PROJECTION = {
                COL_DATE,COL_MAX,COL_MIN,COL_HUMIDITY,COL_PRESSURE,COL_WIND_SPEED,COL_DEGREES,COL_WEATHER_ID
        };

        public static String getSqlSelectForTodayOnwards() {
            long normalizedUtcNow = WeatherUtil.normalizeDate(System.currentTimeMillis());
            return COL_DATE + " >= " + normalizedUtcNow;
        }

    }

}
