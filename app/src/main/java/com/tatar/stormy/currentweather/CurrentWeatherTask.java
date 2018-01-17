package com.tatar.stormy.currentweather;

import android.os.AsyncTask;

import com.tatar.stormy.R;
import com.tatar.stormy.currentweather.CurrentWeatherContract;
import com.tatar.stormy.location.AddressFinder;
import com.tatar.stormy.model.Forecast;
import com.tatar.stormy.service.WeatherForecastResponse;
import com.tatar.stormy.service.WeatherForecastService;
import com.tatar.stormy.util.NetworkUtil;

public class CurrentWeatherTask extends AsyncTask<Double, Void, WeatherForecastResponse> {

    public static final Status TASK_STATUS_RUNNING = AsyncTask.Status.RUNNING;

    private CurrentWeatherContract.CurrentWeatherView currentWeatherView;
    private WeatherForecastService weatherForecastService;
    private AddressFinder addressFinder;
    private NetworkUtil networkUtil;


    public CurrentWeatherTask(CurrentWeatherContract.CurrentWeatherView currentWeatherView, AddressFinder addressFinder, NetworkUtil networkUtil) {
        this.currentWeatherView = currentWeatherView;
        this.addressFinder = addressFinder;
        this.networkUtil = networkUtil;
        weatherForecastService = new WeatherForecastService();
    }

    @Override
    protected void onPreExecute() {
        currentWeatherView.toggleRefresh();
    }

    @Override
    protected WeatherForecastResponse doInBackground(Double... locationParams) {
        double latitude, longitude;
        latitude = locationParams[0];
        longitude = locationParams[1];

        WeatherForecastResponse weatherForecastResponse = new WeatherForecastResponse();

        Forecast forecast = null;

        if (networkUtil.isNetworkAvailable()) {

            forecast = weatherForecastService.getWeatherForecastData(latitude, longitude);

            if (forecast != null) {
                weatherForecastResponse.setMessage(WeatherForecastResponse.SUCCESS_MSG);

                String address = addressFinder.getCompleteAddress(latitude, longitude);
                forecast.getCurrrentWeather().setAddress(address);
            } else {
                weatherForecastResponse.setMessage(WeatherForecastResponse.SERVICE_UNAVAILABLE_MSG);
            }

        } else {
            weatherForecastResponse.setMessage(WeatherForecastResponse.NO_NETWORK_CONNECTION_MSG);
        }

        weatherForecastResponse.setForecast(forecast);

        return weatherForecastResponse;
    }

    @Override
    protected void onPostExecute(WeatherForecastResponse weatherForecastResponse) {
        currentWeatherView.toggleRefresh();

        if (weatherForecastResponse.getMessage().equals(WeatherForecastResponse.SUCCESS_MSG)) {
            currentWeatherView.displayCurrentWeatherData(weatherForecastResponse.getForecast().getCurrrentWeather());
        } else if (weatherForecastResponse.getMessage().equals(WeatherForecastResponse.SERVICE_UNAVAILABLE_MSG)) {
            currentWeatherView.displayEmptyScreenWithMsg(R.string.service_error_txt);
        } else if (weatherForecastResponse.getMessage().equals(WeatherForecastResponse.NO_NETWORK_CONNECTION_MSG)) {
            currentWeatherView.displayEmptyScreenWithMsg(R.string.network_error_txt);
        }
    }

}
