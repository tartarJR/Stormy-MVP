package com.tatar.stormy.location;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

/**
 * Created by musta on 11/21/2017.
 */

public class LocationProvider implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = LocationProvider.class.getSimpleName();

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private Context context;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationProviderCallback locationProviderCallback;

    public LocationProvider(Context context, LocationProviderCallback callback) {

        // TODO check if Google Play Services available
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationProviderCallback = callback;

        // TODO refactor constants
        // Create the LocationRequest object
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        this.context = context;
    }

    public void connect() {
        Log.d(TAG, "connect: hit");
        googleApiClient.connect();
    }

    public void disconnect() {
        Log.d(TAG, "disconnect: hit");
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected: hit");
        getLastLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: hit");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution() && context instanceof Activity) {
            try {
                Activity activity = (Activity) context;
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            /*
             * Thrown if Google Play services canceled the original
             * PendingIntent
             */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                Log.e(TAG, "Error onConnectionFailed: " + e);
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.d(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: hit");
        updateLocation(location);
    }

    public void getLastLocation() {
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } else {
                updateLocation(location);
            }
        } catch (SecurityException ex) {
            Log.e(TAG, "SecurityException caught" + ex);
        }
    }

    private void updateLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String address = getCompleteAddress(location);
        locationProviderCallback.onLocationReceived(latitude, longitude, address);
    }

    // TODO check if Geocoder present
    private String getCompleteAddress(Location location) {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    if (!returnedAddress.getAddressLine(i).isEmpty() || !returnedAddress.getAddressLine(i).equals(null)) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i));
                    }
                    if (i != returnedAddress.getMaxAddressLineIndex()) {
                        strReturnedAddress.append(", ");
                    }
                }

                address = strReturnedAddress.toString();
                Log.w(TAG, strReturnedAddress.toString());
            } else {
                address = "Unknown Location";
                Log.d(TAG, "No Address returned.");
            }
        } catch (Exception e) {
            address = "Unknown Location";
            Log.e(TAG, "Failed to get adddress: " + e);
        }

        return address;
    }
}
