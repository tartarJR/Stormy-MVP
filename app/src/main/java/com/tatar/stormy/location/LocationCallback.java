package com.tatar.stormy.location;

/**
 * Created by musta on 11/22/2017.
 */

public interface LocationCallback {
    void onLocationReceived(double latitude, double longitude);
}
