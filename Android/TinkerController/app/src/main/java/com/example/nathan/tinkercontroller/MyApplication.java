package com.example.nathan.tinkercontroller;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;

import com.jaredrummler.cyanea.Cyanea;

public class MyApplication extends Application {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!

    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!

        Cyanea.init(this, getResources());
        checkFirstRun();
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            Log.d("myTag", "Normal");
            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {
            defaultTheme();
            Log.d("myTag", "First Time");
            // TODO This is a new install (or the user cleared the shared preferences)

        } else if (currentVersionCode > savedVersionCode) {
            // TODO This is an upgrade
            Log.d("myTag", "Upgraded");

        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    private void defaultTheme() {
        Cyanea.getInstance().edit()
                .baseTheme(Cyanea.BaseTheme.LIGHT)
                .subMenuIconColor(Color.parseColor("#FF5DC21E"))
                .menuIconColor(Color.parseColor("#FF5DC21E"))
                .primary(Color.parseColor("#FF107C10")).primaryDark(Color.parseColor("#FF0D690D")).primaryLight(Color.parseColor("#FF338F33"))
                .accent(Color.parseColor("#FF5DC21E")).accentDark(Color.parseColor("#FF4FA419")).accentLight(Color.parseColor("#FF75CB3F"))
                .backgroundResource(R.color.cyanea_background_light).backgroundDarkResource(R.color.cyanea_background_light_darker).backgroundLightResource(R.color.cyanea_background_light_lighter);

    }
}