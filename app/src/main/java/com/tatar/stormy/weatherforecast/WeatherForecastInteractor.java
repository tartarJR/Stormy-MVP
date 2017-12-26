package com.tatar.stormy.weatherforecast;

import android.os.AsyncTask;

import com.tatar.stormy.model.WeatherForecast;
import com.tatar.stormy.service.WeatherForecastService;

/**
 * Created by mobile on 26.12.2017.
 */

public class WeatherForecastInteractor extends AsyncTask<Void, Void, WeatherForecast> {

    private double latitude;
    private double longitude;
    private String address;

    private WeatherForecastContract.View view;
    private WeatherForecastService weatherForecastService;

    public WeatherForecastInteractor(double latitude, double longitude, String address, WeatherForecastContract.View view){
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;

        this.view = view;
        weatherForecastService = new WeatherForecastService();
    }

    @Override
    protected void onPreExecute() {
        view.toggleRefresh();
    }

    @Override
    protected WeatherForecast doInBackground(Void... voids) {
        return weatherForecastService.getCurrentWeather(latitude, longitude);
    }

    @Override
    protected void onPostExecute(WeatherForecast weatherForecast) {
        view.toggleRefresh();

        if (weatherForecast != null) {
            view.updateUi(weatherForecast, address);
        } else {
            view.displayErrorDialog();
        }
    }

}
