package com.steven.sunnydays;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.steven.sunnydays.data.Contract;
import com.steven.sunnydays.utils.WeatherPreference;
import com.steven.sunnydays.utils.WeatherSync;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, WeatherAdapter.WeatherAdapterOnClickHandler {

    private static final int ID_WEATHER_LOADER = 44;
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private WeatherAdapter mAdapter;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvRecycleListView);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.Loading);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new WeatherAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        // Loading Progress bar
//        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        getLoaderManager().initLoader(ID_WEATHER_LOADER, null, this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            WeatherSync weatherSync = new WeatherSync();
            weatherSync.Intialize(MainActivity.this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        switch (id) {
            case ID_WEATHER_LOADER:
                Uri uri = Contract.TableEntry.CONTENT_URI;
                String[] projection = Contract.TableEntry.WEATHER_PROJECTION; // 가져올 컬럼들
                String selection = Contract.TableEntry.getSqlSelectForTodayOnwards(); // 가져올 데이터 필터링 SQL의 WHERE 와 유사
                String sortOrder = Contract.TableEntry.COL_DATE + " ASC"; // Sort order: Ascending by date

                Loader<Cursor> cursor = new CursorLoader(
                        this,
                        uri,
                        projection,
                        selection,
                        null,
                        sortOrder);
                return cursor;
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (cursor.getCount() != 0) {
            // Loading Progress bar
            mRecyclerView.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void openMap() {

        String locationString = "geo:" + WeatherPreference.getLatitude(this).toString() +
                WeatherPreference.getLatitude(this).toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(locationString));
        startActivity(intent);

    }

    @Override
    public void onClick(long date) {

        Uri selectedUri = Contract.TableEntry.CONTENT_URI.buildUpon().appendPath(Long.toString(date)).build();
        Intent intent = new Intent(this, ForecastDetailActivity.class);
        intent.setData(selectedUri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.weather_sync) {
            WeatherSync weatherSync = new WeatherSync();
            weatherSync.Intialize(MainActivity.this);
            return true;
        } else if (itemId == R.id.open_map) {
            openMap();
            return true;
        }
        return true;
    }
}
