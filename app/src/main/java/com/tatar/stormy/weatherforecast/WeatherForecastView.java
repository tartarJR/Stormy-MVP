package com.tatar.stormy.weatherforecast;

import com.tatar.stormy.model.WeatherForecast;

/**
 * Created by musta on 11/22/2017.
 */

public interface WeatherForecastView {

    void displayErrorDialog();

    void toggleRefresh();

    void updateUi(WeatherForecast weatherForecast, String address);

}
