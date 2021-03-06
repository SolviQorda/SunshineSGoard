package com.example.android.sunshine.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by sorengoard on 19/08/16.
 */
public class WeatherContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
                public static final String CONTENT_AUTHORITY = "com.example.android.sunshine.app";

                // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
                // the content provider.
                public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        // Possible paths (appended to base content URI for possible URI's)
                // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
                // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
                // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
                // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
                public static final String PATH_WEATHER = "weather";
                public static final String PATH_LOCATION = "location";



    //    To make it easy to query for the exact date, we normalise all dates that
//    go into the database to the start of the Julian day at UTC
    public static long normalizeDate(long startDate) {
        //normalise the start date to the beginning of the UTC day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }
    /*
        Inner class that defines the table contents of the location table
        This is where to add the strings
     */
    public static final class LocationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String TABLE_NAME = "location";

        //location setting
        public static final String COLUMN_LOCATION_SETTING = "location_setting";
        //city name
        public static final String COLUMN_CITY_NAME = "city_name";
        //Longitude and latitude
        public static final String COLUMN_LONGITUDE = "coord_long";
        public static final String COLUMN_LATITUDE = "coord_lat";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }



    //Inner class that defines the table contents of the weather table
    public static final class WeatherEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
        public static final String TABLE_NAME = "weather";

        //Columun with the foreign key into the location table
        public static final String COLUMN_LOC_KEY = "location_id";
        //Date, sotred as long in ms since the eopch
        public static final String COLUMN_DATE = "date";
        //Weather Id as returned by the API, to id the icon to be used
        public static final String COLUMN_WEATHER_ID = "weather_id";
        //Short description of the weather
        public static final String COLUMN_SHORT_DESC = "short_desc";
        //Min and max temps for the day, stored as floats
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        //HUmidity as a float representing percentage
        public static final String COLUMN_HUMIDITY = "humidity";

        //Pressure
        public static final String COLUMN_PRESSURE = "pressure";

        //Windspeed a float representing mph
        public static final String COLUMN_WINDSPEED = "wind";
        //Degrees - meterological 0 is North 180 South
        public static final String COLUMN_DEGREES = "degrees";

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
                    }

        /*
Student: Fill in this buildWeatherLocation function */
        public static Uri buildWeatherLocation(String locationSetting) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting).build();

    }

        public static Uri buildWeatherLocationWithStartDate(String locationSetting, long startDate) {
        long normalizedDate = normalizeDate(startDate);
        return CONTENT_URI.buildUpon().appendPath(locationSetting)
                        .appendQueryParameter(COLUMN_DATE, Long.toString(normalizedDate)).build();
    }

        public static Uri buildWeatherLocationWithDate(String locationSetting, long date) {
        return CONTENT_URI.buildUpon().appendPath(locationSetting)
                        .appendPath(Long.toString(normalizeDate(date))).build();
    }

        public static String getLocationSettingFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

        public static long getDateFromUri(Uri uri) {
        return Long.parseLong(uri.getPathSegments().get(2));
    }

        public static long getStartDateFromUri(Uri uri) {
        String dateString = uri.getQueryParameter(COLUMN_DATE);
        if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
        else
            return 0;
    }

    }
}












