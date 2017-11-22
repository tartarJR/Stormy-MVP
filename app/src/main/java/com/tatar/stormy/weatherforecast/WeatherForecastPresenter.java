package com.tatar.stormy.weatherforecast;

import android.os.AsyncTask;

import com.tatar.stormy.model.WeatherForecast;
import com.tatar.stormy.networking.WeatherForecastService;

/**
 * Created by musta on 11/22/2017.
 */

public class WeatherForecastPresenter extends AsyncTask<Void, Void, WeatherForecast> {

    private double latitude;
    private double longitude;
    private String address;

    private WeatherForecastService weatherForecastService;
    private WeatherForecastView weatherForecastView;

    public WeatherForecastPresenter(WeatherForecastView weatherForecastView, double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;

        this.weatherForecastView = weatherForecastView;
        weatherForecastService = new WeatherForecastService();
    }

    @Override
    protected void onPreExecute() {
        weatherForecastView.toggleRefresh();
    }

    @Override
    protected WeatherForecast doInBackground(Void... voids) {
        return weatherForecastService.getCurrentWeather(latitude, longitude);
    }

    @Override
    protected void onPostExecute(WeatherForecast weatherForecast) {
        weatherForecastView.toggleRefresh();

        if (weatherForecast != null) {
            weatherForecastView.updateUi(weatherForecast, address);
        } else {
            weatherForecastView.displayErrorDialog();
        }
    }
}
