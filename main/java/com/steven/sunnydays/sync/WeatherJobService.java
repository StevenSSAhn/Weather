package com.steven.sunnydays.sync;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Steven on 2018-01-21.
 */

public class WeatherJobService extends JobService {
    private static final String TAG = WeatherJobService.class.getSimpleName();

    private AsyncTask<Void, Void, Void> mAsynckTask;

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters job) {

        mAsynckTask = new AsyncTask<Void, Void, Void>() {

             @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                GetWeather.getData(context);
                jobFinished(job, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
                Log.e(TAG, "FireJob" + job);
            }


        };
        mAsynckTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
