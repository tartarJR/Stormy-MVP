package com.tatar.stormy.weatherforecast;

import android.os.AsyncTask;

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
    public void fetchWeatherForecastData(double latitude, double longitude) {
        weatherForecastInteractor = new WeatherForecastInteractor(latitude, longitude, view); // TODO move this from here to constructor
        weatherForecastInteractor.execute();
    }
}
