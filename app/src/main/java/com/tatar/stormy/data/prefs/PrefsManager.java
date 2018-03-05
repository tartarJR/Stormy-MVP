package com.tatar.stormy.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by musta on 3/5/2018.
 */

public class PrefsManager {

    private static final String PREF_NAME = "com.tatar.stormy.preferences";
    private static final String PREF_KEY_DAILY_WEATHER = "PREF_KEY_DAILY_WEATHER_";
    private static final String PREF_KEY_HOURLY_WEATHER = "PREF_KEY_HOURLY_WEATHER_";

    private final SharedPreferences prefs;

    public PrefsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
