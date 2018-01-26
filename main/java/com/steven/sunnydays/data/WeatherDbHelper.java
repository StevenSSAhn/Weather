package com.steven.sunnydays.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Steven on 2018-01-16.
 */
public class WeatherDbHelper extends SQLiteOpenHelper {

    public static String dbname = "weather.db";
    public static int Ver = 3;
    public WeatherDbHelper(Context context) {
        super(context, dbname, null, Ver);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE =
                "CREATE TABLE "+ Contract.TableEntry.TABLE_NAME+ " ("+
                Contract.TableEntry._ID             +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Contract.TableEntry.COL_DATE        +" INTEGER NOT NULL, "+
                Contract.TableEntry.COL_WEATHER_ID  +" INTEGER NOT NULL, "+
                Contract.TableEntry.COL_MIN         +" REAL NOT NULL, "+
                Contract.TableEntry.COL_MAX         +" REAL NOT NULL, "+
                Contract.TableEntry.COL_HUMIDITY    +" REAL NOT NULL, "+
                Contract.TableEntry.COL_PRESSURE    +" REAL NOT NULL, "+
                Contract.TableEntry.COL_WIND_SPEED  +" REAL NOT NULL, "+
                Contract.TableEntry.COL_DEGREES     +" REAL NOT NULL, "+
                " UNIQUE (" + Contract.TableEntry.COL_DATE + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.TableEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
