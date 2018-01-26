package com.steven.sunnydays;

import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.steven.sunnydays.data.Contract;
import com.steven.sunnydays.utils.WeatherPreference;
import com.steven.sunnydays.utils.WeatherUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ForecastDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_DETAIL = 337;

    private Uri mUri;

    private TextView mDate;
    private TextView mDescription;
    private TextView mHigh;
    private TextView mLow;
    private TextView mHumidity;
    private TextView mWind;
    private TextView mPressure;
    private ImageView mFan;
    private ImageView mDetailIcon;
    private TextView mCityName;
    private TextView mPopulation;
//    private ImageView mlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_detail);

        mUri = getIntent().getData();

        mDate = (TextView) findViewById(R.id.date);
        mDescription = (TextView) findViewById(R.id.weather_description);
        mHigh = (TextView) findViewById(R.id.high_temperature);
        mLow = (TextView) findViewById(R.id.low_temperature);
        mHumidity = (TextView) findViewById(R.id.humidity);
        mWind = (TextView) findViewById(R.id.wind);
        mPressure = (TextView) findViewById(R.id.pressure);
        mFan = (ImageView) findViewById(R.id.windimage);
        mDetailIcon = (ImageView) findViewById((R.id.detailIcon));
        mCityName = (TextView) findViewById(R.id.cityname);
        mPopulation = (TextView) findViewById(R.id.population);
//        mlayout = (ImageView) findViewById(R.id.detailviewbackground);
//        mlayout.setImageResource(R.drawable.rain);
        getSupportLoaderManager().initLoader(ID_DETAIL, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        switch (loaderId) {
            case ID_DETAIL:
                return new CursorLoader(this,
                        mUri,
                        Contract.TableEntry.WEATHER_DETAIL_PROJECTION, // 가져올 컬럼들
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader is not ready " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int INDEX_DATE = 0;
        int INDEX_MAX_TEMP = 1;
        int INDEX_MIN_TEMP = 2;
        int INDEX_HUMIDITY = 3;
        int INDEX_PRESSURE = 4;
        int INDEX_WIND_SPEED = 5;
        int INDEX_DEGREES = 6;
        int INDEX_CONDITION_ID = 7;

        if ( cursor == null || !cursor.moveToFirst())
            return;

        long date = cursor.getLong(0);
        String dateString = DateUtils.formatDateTime(this, date, 26);
        mDate.setText(dateString);

        // Weather Description
        int weatherId = cursor.getInt(INDEX_CONDITION_ID);
        double low = cursor.getDouble(INDEX_MIN_TEMP);

        //OpenWeather Value correction
        if (weatherId == 600 || weatherId == 601 || weatherId == 602) {
            if (low > 32) {
                weatherId -= 100;
            }
        }

        String description = WeatherUtil.getStringForWeatherCondition(this, weatherId);
        mDescription.setText(description);

        int weatherImageId = WeatherUtil.getLargeImage(weatherId);
        mDetailIcon.setImageResource(weatherImageId);

        // Temp
        double high = cursor.getDouble(INDEX_MAX_TEMP);
        String highString = WeatherUtil.formatTemperature(this, high);
        mHigh.setText(highString);

//        double low = cursor.getDouble(INDEX_MIN_TEMP);
        String lowString = WeatherUtil.formatTemperature(this, low);
        mLow.setText(lowString);

        float humidity = cursor.getFloat(INDEX_HUMIDITY);
        String humidityString = getString(R.string.format_humidity, humidity);
        mHumidity.setText(humidityString);

        float windSpeed = cursor.getFloat(INDEX_WIND_SPEED);

        GlideDrawableImageViewTarget Target = new GlideDrawableImageViewTarget(mFan);
        if (windSpeed >= 0 && windSpeed <= 2) {
            Glide.with(this).load(R.raw.fannormal).into(Target);
        } else if (windSpeed > 2 && windSpeed <= 3.9) {
            Glide.with(this).load(R.raw.fanfast).into(Target);
        } else if (windSpeed > 3.9) {
            Glide.with(this).load(R.raw.fan).into(Target);
        }

        float windDirection = cursor.getFloat(INDEX_DEGREES);
        String windString = WeatherUtil.getFormattedWind(this, windSpeed, windDirection);
        mWind.setText(windString);


        float pressure = cursor.getFloat(INDEX_PRESSURE);
        String pressureString = getString(R.string.format_pressure, pressure);
        mPressure.setText(pressureString);

        String cityName = WeatherPreference.getCityName(this);
        mCityName.setText(cityName);
        int population = WeatherPreference.getPopulation(this);

        mPopulation.setText(String.valueOf(population));

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}