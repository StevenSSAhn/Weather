package com.steven.sunnydays.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Steven on 2018-01-16.
 */

public class WeatherSyncService extends IntentService {

    public WeatherSyncService() {
        super("WeatherSyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GetWeather.getData(this);
    }
}
