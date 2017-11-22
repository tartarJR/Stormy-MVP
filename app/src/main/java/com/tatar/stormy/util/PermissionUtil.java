package com.tatar.stormy.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by musta on 11/20/2017.
 */

public class PermissionUtil {

    private static final String TAG = PermissionUtil.class.getSimpleName();

    private static final String PREFS_FILE_NAME = "preference_permission";
    private static final String PREFS_FIRST_TIME_KEY = "is_app_launched_first_time";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static String[] PERMISSIONS_LOCATION = {FINE_LOCATION, COARSE_LOCATION};
    public static final int REQUEST_LOCATION = 1;
    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;

    public static void askPermissions(final Activity activity) {

        Log.d(TAG, "checkPermission");

        if (isRuntimePermissionRequired()) {
            /*
                Runtime permission required,
                THE DEVICE IS RUNNING ON > 23
             */

            //check if the permission is already granted, i.e the application was launched earlier too, and the user had "allowed" the permission then.
            if (ContextCompat.checkSelfPermission(activity, FINE_LOCATION) != GRANTED || ContextCompat.checkSelfPermission(activity, COARSE_LOCATION) != GRANTED) {
                /* We don't have permission, two cases arise:
                     1. App launched first time,
                     2. App launched earlier too, and the user had denied the permission is last launch
                           2A. The user denied permission earlier WITHOUT checking "Never ask again"
                           2B. The user denied permission earlier WITH checking "Never ask again"
                */

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity, COARSE_LOCATION)) {
                    /*
                       shouldShowRequestPermissionRationale returned true
                       this means Case: 2A
                       see the flowchart, the only case when shouldShowRequestPermissionRationale returns "true", is when the application was launched earlier too and the user had "denied" the permission in last launch WITHOUT checking "never show again"
                    */
                    Log.d(TAG, "shouldShowRequestPermissionRationale returned true.");

                    new AlertDialog.Builder(activity)
                            .setTitle("Permission required")
                            .setMessage("Location is required for this application to work ! ")
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activity,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            REQUEST_LOCATION);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    activity.finish();
                                }
                            })
                            .show();
                } else {
                    /*
                         this means, either -
                         Case: 1 or Case 2B
                         See Flowchart, shouldShowRequestPermissionRationale returns false, only when app is launched first time (Case: 1) or app was launched earlier too and user HAD checked "Never show again" then (Case: 2B)
                    */
                    if (getApplicationLaunchedFirstTime(activity)) {

                        //Case: 1
                        Log.d(TAG, "Application launched first time.");

                        setApplicationLaunchedFirstTime(activity);  //  ** DON'T FORGET THIS **
                        ActivityCompat.requestPermissions(activity, PERMISSIONS_LOCATION, REQUEST_LOCATION);

                    } else {
                        //Case: 2B
                        Log.d(TAG, "Permission disabled.");

                        new AlertDialog.Builder(activity)
                                .setTitle("Permission Disabled")
                                .setMessage("Please enable the permission in \n  Settings>Uber>Permission \n and check 'location' permission")
                                .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                                    }

                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        activity.finish();
                                    }
                                })
                                .show();
                    }
                }
            }
        }
    }

    public static boolean isPermissionsGranted(Context context) {
        if (ActivityCompat.checkSelfPermission(context, FINE_LOCATION) == GRANTED && ActivityCompat.checkSelfPermission(context, COARSE_LOCATION) == GRANTED) {
            return true;
        }

        return false;
    }

    private static boolean isRuntimePermissionRequired() {
        return (Build.VERSION.SDK_INT >= 23);
    }

    // preference utility methods
    private static boolean getApplicationLaunchedFirstTime(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_FILE_NAME, activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFS_FIRST_TIME_KEY, true);
    }

    private static void setApplicationLaunchedFirstTime(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_FILE_NAME, activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_FIRST_TIME_KEY, false);
        editor.commit();
    }

}
