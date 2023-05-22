package com.ab.hicareservices.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;



public class AppUtils {


    public class LocationConstants {
        public static final int SUCCESS_RESULT = 0;

        public static final int FAILURE_RESULT = 1;

        public static final String PACKAGE_NAME = "com.sample.sishin.maplocation";

        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

        public static final String LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA";
        public static final String LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY";
        public static final String LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET";


    }


    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String getDays(int day) {
        String days = "";
        switch (day) {
            case 1:
                days = "MON";
                break;

            case 2:
                days = "TUE";
                break;

            case 3:
                days = "WED";
                break;

            case 4:
                days = "THU";
                break;

            case 5:
                days = "FRI";
                break;

            case 6:
                days = "SAT";
                break;

            case 7:
                days = "SUN";
                break;

            default:
                days = "NA";
                break;
        }

        return days;
    }



    public static String getMonths(int month) {
        String months = "";
        switch (month) {
            case 1:
                months = "January";
                break;

            case 2:
                months = "February";
                break;

            case 3:
                months = "March";
                break;

            case 4:
                months = "April";
                break;

            case 5:
                months = "May";
                break;

            case 6:
                months = "June";
                break;

            case 7:
                months = "July";
                break;

            case 8:
                months= "August";
                break;

            case 9:
                months = "September";
                break;

            case 10:
                months = "October";
                break;

            case 11:
                months = "November";
                break;

            case 12:
                months = "December";
                break;

            default:
                months = "NA";
                break;
        }

        return months;
    }




}
