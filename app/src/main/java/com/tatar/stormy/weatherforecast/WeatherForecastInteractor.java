package com.tatar.stormy.weatherforecast;

import android.os.AsyncTask;

import com.tatar.stormy.location.AddressFinder;
import com.tatar.stormy.model.WeatherForecast;
import com.tatar.stormy.service.WeatherForecastService;

/**
 * Created by mobile on 26.12.2017.
 */

public class WeatherForecastInteractor extends AsyncTask<Void, Void, WeatherForecast> {

    private double latitude;
    private double longitude;

    private WeatherForecastContract.View view;
    private WeatherForecastService weatherForecastService;
    private AddressFinder addressFinder;

    public WeatherForecastInteractor(double latitude, double longitude, WeatherForecastContract.View view){
        this.latitude = latitude;
        this.longitude = longitude;

        this.view = view;
        weatherForecastService = new WeatherForecastService();
        addressFinder = new AddressFinder(view.getContext());
    }

    @Override
    protected void onPreExecute() {
        view.toggleRefresh();
    }

    @Override
    protected WeatherForecast doInBackground(Void... voids) {
        WeatherForecast weatherForecast = weatherForecastService.getCurrentWeatherForecastData(latitude, longitude);

        if (weatherForecast != null) {
            String address = addressFinder.getCompleteAddress(latitude, longitude);
            weatherForecast.setAddress(address);
        }

        return weatherForecast;
    }

    @Override
    protected void onPostExecute(WeatherForecast weatherForecast) {
        view.toggleRefresh();

        if (weatherForecast != null) {
            view.displayWeatherForecastData(weatherForecast);
        } else {
            view.diplayServiceError();
        }
    }

}
