package com.steven.sunnydays.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.steven.sunnydays.R;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Steven on 2018-01-16.
 */

public class WeatherUtil {

    private static final String LOG_TAG = WeatherUtil.class.getSimpleName();

    /* Milliseconds in a day */
    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static long normalizeDate(long date) {
        long daysSinceEpoch = TimeUnit.MILLISECONDS.toDays(date);
        long millisFromEpochToTodayAtMidnightUtc = daysSinceEpoch * DAY_IN_MILLIS;
        return millisFromEpochToTodayAtMidnightUtc;
    }

    public static String getStringForWeatherCondition(Context context, int weatherId) {
        int stringId;
        if (weatherId >= 200 && weatherId <= 232) {
            stringId = R.string.condition_2xx;
        } else if (weatherId >= 300 && weatherId <= 321) {
            stringId = R.string.condition_3xx;
        } else switch (weatherId) {
            case 500:
                stringId = R.string.condition_500;
                break;
            case 501:
                stringId = R.string.condition_501;
                break;
            case 502:
                stringId = R.string.condition_502;
                break;
            case 503:
                stringId = R.string.condition_503;
                break;
            case 504:
                stringId = R.string.condition_504;
                break;
            case 511:
                stringId = R.string.condition_511;
                break;
            case 520:
                stringId = R.string.condition_520;
                break;
            case 531:
                stringId = R.string.condition_531;
                break;
            case 600:
                stringId = R.string.condition_600;
                break;
            case 601:
                stringId = R.string.condition_601;
                break;
            case 602:
                stringId = R.string.condition_602;
                break;
            case 611:
                stringId = R.string.condition_611;
                break;
            case 612:
                stringId = R.string.condition_612;
                break;
            case 615:
                stringId = R.string.condition_615;
                break;
            case 616:
                stringId = R.string.condition_616;
                break;
            case 620:
                stringId = R.string.condition_620;
                break;
            case 621:
                stringId = R.string.condition_621;
                break;
            case 622:
                stringId = R.string.condition_622;
                break;
            case 701:
                stringId = R.string.condition_701;
                break;
            case 711:
                stringId = R.string.condition_711;
                break;
            case 721:
                stringId = R.string.condition_721;
                break;
            case 731:
                stringId = R.string.condition_731;
                break;
            case 741:
                stringId = R.string.condition_741;
                break;
            case 751:
                stringId = R.string.condition_751;
                break;
            case 761:
                stringId = R.string.condition_761;
                break;
            case 762:
                stringId = R.string.condition_762;
                break;
            case 771:
                stringId = R.string.condition_771;
                break;
            case 781:
                stringId = R.string.condition_781;
                break;
            case 800:
                stringId = R.string.condition_800;
                break;
            case 801:
                stringId = R.string.condition_801;
                break;
            case 802:
                stringId = R.string.condition_802;
                break;
            case 803:
                stringId = R.string.condition_803;
                break;
            case 804:
                stringId = R.string.condition_804;
                break;
            case 900:
                stringId = R.string.condition_900;
                break;
            case 901:
                stringId = R.string.condition_901;
                break;
            case 902:
                stringId = R.string.condition_902;
                break;
            case 903:
                stringId = R.string.condition_903;
                break;
            case 904:
                stringId = R.string.condition_904;
                break;
            case 905:
                stringId = R.string.condition_905;
                break;
            case 906:
                stringId = R.string.condition_906;
                break;
            case 951:
                stringId = R.string.condition_951;
                break;
            case 952:
                stringId = R.string.condition_952;
                break;
            case 953:
                stringId = R.string.condition_953;
                break;
            case 954:
                stringId = R.string.condition_954;
                break;
            case 955:
                stringId = R.string.condition_955;
                break;
            case 956:
                stringId = R.string.condition_956;
                break;
            case 957:
                stringId = R.string.condition_957;
                break;
            case 958:
                stringId = R.string.condition_958;
                break;
            case 959:
                stringId = R.string.condition_959;
                break;
            case 960:
                stringId = R.string.condition_960;
                break;
            case 961:
                stringId = R.string.condition_961;
                break;
            case 962:
                stringId = R.string.condition_962;
                break;
            default:
                return context.getString(R.string.condition_unknown, weatherId);
        }

        return context.getString(stringId);
    }

    public static int getSmallImage(int weatherId) {

        /*
         * Based on weather code data for Open Weather Map.
         */
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_strom_vec;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_rain_vec;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain_vec_big;
        } else if (weatherId == 511) {
            return R.drawable.ic_rain_vec;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain_vec;
        } else if (weatherId >= 600 && weatherId <= 622) {
//            return R.drawable.ic_snow;
            return R.drawable.aasnow2;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.aa_fog;
        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
            return R.drawable.ic_strom_vec;
        } else if (weatherId == 800) {
            return R.drawable.ic_clears;
        } else if (weatherId == 801) {
            return R.drawable.aalightcloud;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        } else if (weatherId >= 900 && weatherId <= 906) {
            return R.drawable.ic_strom_vec;
        } else if (weatherId >= 958 && weatherId <= 962) {
            return R.drawable.ic_strom_vec;
        } else if (weatherId >= 951 && weatherId <= 957) {
            return R.drawable.ic_clears;
        }

        Log.e(LOG_TAG, "Unknown Weather: " + weatherId);
        return R.drawable.ic_storm;
    }


    public static int getLargeImage(int weatherId) {

        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_strom_vec;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            if(weatherId==500)return R.drawable.aalightrain;
            else if (weatherId == 501) {
                return R.drawable.aamoderaterain;
            } else {
                return R.drawable.ic_rain_vec_big;
            }
        } else if (weatherId == 511) {
            return R.drawable.aasnow2;
        } else if (weatherId >= 520 && weatherId <= 531) {

                return R.drawable.ic_rain_vec;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.aasnow2;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.aa_fog;
        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
            return R.drawable.ic_stroms;
        } else if (weatherId == 800) {
            return R.drawable.ic_clears;
        } else if (weatherId == 801) {
            return R.drawable.aalightcloud;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloud3;
        } else if (weatherId >= 900 && weatherId <= 906) {
            return R.drawable.ic_stroms;
        } else if (weatherId >= 958 && weatherId <= 962) {
            return R.drawable.ic_stroms;
        } else if (weatherId >= 951 && weatherId <= 957) {
            return R.drawable.ic_clears;
        }

        Log.e(LOG_TAG, "Unknown Weather: " + weatherId);
        return R.drawable.art_storm;
    }

    public static String formatTemperature(Context context, double temperature) {
//        if (!WeatherPreference.isMetric(context)) {
//            temperature = celsiusToFahrenheit(temperature);
//        }

        int temperatureFormatResourceId = R.string.format_temperature;

        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(temperatureFormatResourceId), temperature);
    }

    public static String getFormattedWind(Context context, float windSpeed, float degrees) {
        int windFormat = R.string.format_wind_kmh;

        String direction = "Unknown";

        direction = (degrees >= 337.5 || degrees < 22.5) ? "N" : (degrees >= 22.5 && degrees < 67.5) ? "NE" :
            (degrees >= 67.5 && degrees < 112.5) ? "E" : (degrees >= 112.5 && degrees < 157.5) ? "SE" :
            (degrees >= 157.5 && degrees < 202.5) ? "S": (degrees >= 202.5 && degrees < 247.5) ? "SW" :
            (degrees >= 247.5 && degrees < 292.5) ? "W": (degrees >= 292.5 && degrees < 337.5) ? "NW" : "Unknown";

        return String.format(context.getString(windFormat), windSpeed, direction);
    }


}
