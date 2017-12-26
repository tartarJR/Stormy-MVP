package com.tatar.stormy.weatherforecast;

import com.tatar.stormy.model.WeatherForecast;

public interface WeatherForecastContract {

    interface Presenter{
        void stop();
        void fetchWeatherForecastData(double latitude, double longitude, String address);
    }

    interface View{
        void displayErrorDialog();

        void toggleRefresh();

        void updateUi(WeatherForecast weatherForecast, String address);
    }

}
