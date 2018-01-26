package com.steven.sunnydays.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Steven on 2018-01-16.
 */

public class WeatherContentProvider extends ContentProvider {

    public static final int CODE_WEATHER = 100;
    public static final int CODE_WEATHER_WITH_DATE = 101;
    private WeatherDbHelper mWeatherDbHelper;
    private static final UriMatcher sUriMatch = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        // This URI is content://com.steven.sunnydays/weather/
        uriMatcher.addURI(authority, Contract.TableEntry.TABLE_NAME, CODE_WEATHER);
        // This URI is content://com.steven.sunnydays/weather/# anynumber
        uriMatcher.addURI(authority, Contract.TableEntry.TABLE_NAME + "/#", CODE_WEATHER_WITH_DATE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        // Make DB helper to use with Contentprovider
        mWeatherDbHelper = new WeatherDbHelper(context);
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase database = mWeatherDbHelper.getWritableDatabase();
        database.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues value : values) {
                long _id = database.insert(Contract.TableEntry.TABLE_NAME, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsInserted;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatch.match(uri)) {
            case CODE_WEATHER_WITH_DATE: {

                String normalizedUtcDateString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{normalizedUtcDateString};
                cursor = mWeatherDbHelper.getReadableDatabase().query(
                        Contract.TableEntry.TABLE_NAME,
                        projection,
                        Contract.TableEntry.COL_DATE + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case CODE_WEATHER: {
                cursor = mWeatherDbHelper.getReadableDatabase().query(
                        Contract.TableEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;
        if (null == selection) selection = "1";

        switch (sUriMatch.match(uri)) {

            case CODE_WEATHER:
                numRowsDeleted = mWeatherDbHelper.getWritableDatabase().delete(
                        Contract.TableEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mWeatherDbHelper.close();
        super.shutdown();
    }

}
