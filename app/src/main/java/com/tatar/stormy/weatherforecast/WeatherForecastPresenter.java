package com.tatar.stormy.weatherforecast;

import android.os.AsyncTask;

/**
 * Created by mobile on 26.12.2017.
 */

public class WeatherForecastPresenter implements WeatherForecastContract.Presenter {

    private WeatherForecastContract.View view;
    private WeatherForecastInteractor weatherForecastInteractor;

    public WeatherForecastPresenter(WeatherForecastContract.View view) {
        this.view = view;
    }

    @Override
    public void stop() {
        if (weatherForecastInteractor != null && weatherForecastInteractor.getStatus() == AsyncTask.Status.RUNNING) {
            weatherForecastInteractor.cancel(true);
        }
    }

    @Override
    public void fetchWeatherForecastData(double latitude, double longitude, String address) {
        weatherForecastInteractor = new WeatherForecastInteractor(latitude, longitude, address, view);
        weatherForecastInteractor.execute();
    }
}
