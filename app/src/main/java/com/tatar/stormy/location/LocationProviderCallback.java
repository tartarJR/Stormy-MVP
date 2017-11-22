package com.tatar.stormy.location;

/**
 * Created by musta on 11/22/2017.
 */

public interface LocationProviderCallback {
    void getWeatherForecast(double latitude, double longitude, String address);
}
