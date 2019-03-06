package com.rhinhospital.rnd.rhiot.util;

import android.util.Log;

public class RhinLog {
    static final private String LOG_TAG = "Rhin";

    public static void print(String message) {
        Log.d(LOG_TAG, message);
    }

    public static void warning(String message) {
        Log.w(LOG_TAG, message);
    }

    public static void error(String message) {
        Log.e(LOG_TAG, message);
    }

    public static void info(String message) {
        Log.i(LOG_TAG, message);
    }
}
