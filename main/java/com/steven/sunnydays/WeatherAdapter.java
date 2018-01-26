package com.steven.sunnydays;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.steven.sunnydays.utils.WeatherPreference;
import com.steven.sunnydays.utils.WeatherUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Steven on 2018-01-13.
 */

class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewholder> {

    private static final int TODAY = 0;
    private static final int FUTURE_DAY = 1;
    private static final String TAG = WeatherAdapter.class.getSimpleName();
    private Cursor mCursor;
    private final Context mContext;

    WeatherAdapterOnClickHandler mClickHandler;

    public interface WeatherAdapterOnClickHandler {
        void onClick(long date);
    }


    WeatherAdapter(Context context, WeatherAdapterOnClickHandler handler) {
        mContext = context;
        mClickHandler = handler;
    }

    @Override
    public WeatherViewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        int id;

        if (viewType == TODAY) {
            id = R.layout.weather_list_item_today;
        } else {
            id = R.layout.weather_list_item;
        }

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(id, parent, false);

        return new WeatherViewholder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewholder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TODAY;
        } else {
            return FUTURE_DAY;
        }
    }


    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class WeatherViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconview;
        final TextView dateview;
        final TextView descriptionview;
        final TextView highTemp;
        final TextView lowTemp;
        final TextView mCityName1;

        public WeatherViewholder(View itemView) {
            super(itemView);
            iconview = (ImageView) itemView.findViewById(R.id.detailIcon);
            dateview = (TextView) itemView.findViewById(R.id.date);
            descriptionview = (TextView) itemView.findViewById(R.id.weather_description);
            highTemp = (TextView) itemView.findViewById(R.id.high_temperature);
            lowTemp = (TextView) itemView.findViewById(R.id.low_temperature);
            mCityName1 = (TextView) itemView.findViewById(R.id.cityname1);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            mCursor.moveToPosition(position);

            int INDEX_DATE = 0;
            int INDEX_MAX_TEMP = 1;
            int INDEX_MIN_TEMP = 2;
            int INDEX_CONDITION_ID = 3;

            int weatherId = mCursor.getInt(INDEX_CONDITION_ID);
            double lowInCelsius = mCursor.getDouble(INDEX_MIN_TEMP);

            //OpenWeather Value correction
            if (weatherId == 600 || weatherId == 601 || weatherId == 602) {
                if (lowInCelsius > 32) {
                    weatherId -= 100;
                }
            }

            int weatherImageId;

            if (position == 0) {
                weatherImageId = WeatherUtil.getLargeImage(weatherId);
            } else {
                weatherImageId = WeatherUtil.getSmallImage(weatherId);
            }


            GlideDrawableImageViewTarget Target = new GlideDrawableImageViewTarget(iconview);
            // 날씨가 맑을때는 Sun을 움직여 준다.
            if (weatherImageId == R.drawable.ic_clears && position != 0) {
                Glide.with(mContext).load(R.raw.sun_anim).into(Target);
            } else if (weatherImageId == R.drawable.aasnow2 && position != 0) {
                Glide.with(mContext).load(R.raw.snowmoving).into(Target);
            } else if (weatherImageId == R.drawable.ic_rain_vec && position != 0) {
                Glide.with(mContext).load(R.raw.rainmoving).into(Target);
            } else if (weatherId == 500 && position != 0) {
                Glide.with(mContext).load(R.raw.rightrain).into(Target);
            }else if (weatherId == 501 && position != 0) {
                Glide.with(mContext).load(R.raw.moderaterain).into(Target);
            }else {
                iconview.setImageResource(weatherImageId);
            }


            // Read date from the cursor */
            long dateInMillis = mCursor.getLong(INDEX_DATE);


//            String dateString = WeatherUtil.getFriendlyDateString(mContext, dateInMillis, false);

            int flags;
            if (position == 0) {
                flags = DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NO_YEAR
                        | DateUtils.FORMAT_SHOW_WEEKDAY;
            } else {
                flags = DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_NO_YEAR
                        | DateUtils.FORMAT_ABBREV_ALL
                        | DateUtils.FORMAT_SHOW_WEEKDAY;
            }
            String dateString = DateUtils.formatDateTime(mContext, dateInMillis, flags);

//            String dateString = new SimpleDateFormat("EEEE").format(dateInMillis);

//            Date date = new Date(System.currentTimeMillis());
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String getTime = sdf.format(date);
//            Log.e(TAG, "Current Time is " + getTime);
            dateview.setText(dateString);

            // Weather Description
            String description = WeatherUtil.getStringForWeatherCondition(mContext, weatherId);
            descriptionview.setText(description);

            // Max Temp
            double highInCelsius = mCursor.getDouble(INDEX_MAX_TEMP);
            String highString = WeatherUtil.formatTemperature(mContext, highInCelsius);
            highTemp.setText(highString);

            //Read low temp
//            double lowInCelsius = mCursor.getDouble(INDEX_MIN_TEMP);
            String lowString = WeatherUtil.formatTemperature(mContext, lowInCelsius);
            lowTemp.setText(lowString);

            if (position == 0) {
                //City Name
                String cityName1 = WeatherPreference.getCityName(mContext);
                mCityName1.setText(cityName1);
                mCityName1.setText(WeatherPreference.getCityName(mContext));
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            long date = mCursor.getLong(0);
            mClickHandler.onClick(date);
        }
    }
}
