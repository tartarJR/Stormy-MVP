package com.tatar.stormy.weatherforecast;

import android.content.Context;

import com.tatar.stormy.model.WeatherForecast;

public interface WeatherForecastContract {

    interface Presenter{
        void stop();
        void fetchWeatherForecastData(double latitude, double longitude);
    }

    interface View{
        void diplayServiceError();

        void toggleRefresh();

        void displayWeatherForecastData(WeatherForecast weatherForecast);

        Context getContext();
    }

}
