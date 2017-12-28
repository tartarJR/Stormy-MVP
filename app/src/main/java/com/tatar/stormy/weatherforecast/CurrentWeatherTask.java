package com.tatar.stormy.weatherforecast;

import android.os.AsyncTask;

import com.tatar.stormy.R;
import com.tatar.stormy.location.AddressFinder;
import com.tatar.stormy.model.CurrrentWeather;
import com.tatar.stormy.service.CurrentWeatherResponse;
import com.tatar.stormy.service.WeatherForecastService;
import com.tatar.stormy.util.NetworkUtils;

public class CurrentWeatherTask extends AsyncTask<Double, Void, CurrentWeatherResponse> {

    public static final Status TASK_STATUS_RUNNING = AsyncTask.Status.RUNNING;

    private CurrentWeatherContract.CurrentWeatherView currentWeatherView;
    private WeatherForecastService weatherForecastService;
    private AddressFinder addressFinder;

    public CurrentWeatherTask(CurrentWeatherContract.CurrentWeatherView currentWeatherView) {
        this.currentWeatherView = currentWeatherView;
        weatherForecastService = new WeatherForecastService();
        addressFinder = new AddressFinder(currentWeatherView.getContext());
    }

    @Override
    protected void onPreExecute() {
        currentWeatherView.toggleRefresh();
    }

    @Override
    protected CurrentWeatherResponse doInBackground(Double... locationParams) {
        double latitude, longitude;
        latitude = locationParams[0];
        longitude = locationParams[1];

        CurrentWeatherResponse currentWeatherResponse = new CurrentWeatherResponse();

        CurrrentWeather currrentWeather = null;

        if (NetworkUtils.isNetworkAvailable(currentWeatherView.getContext())) {

            currrentWeather = weatherForecastService.getCurrentWeatherData(latitude, longitude);

            if (currrentWeather != null) {
                currentWeatherResponse.setMessage(CurrentWeatherResponse.SUCCESS_MSG);

                String address = addressFinder.getCompleteAddress(latitude, longitude);
                currrentWeather.setAddress(address);
            } else {
                currentWeatherResponse.setMessage(CurrentWeatherResponse.SERVICE_UNAVAILABLE_MSG);
            }

        } else {
            currentWeatherResponse.setMessage(CurrentWeatherResponse.NO_NETWORK_CONNECTION_MSG);
        }

        currentWeatherResponse.setCurrrentWeather(currrentWeather);

        return currentWeatherResponse;
    }

    @Override
    protected void onPostExecute(CurrentWeatherResponse currentWeatherResponse) {
        currentWeatherView.toggleRefresh();

        if (currentWeatherResponse.getMessage().equals(CurrentWeatherResponse.SUCCESS_MSG)) {
            currentWeatherView.displayCurrentWeatherData(currentWeatherResponse.getCurrrentWeather());
        } else if (currentWeatherResponse.getMessage().equals(CurrentWeatherResponse.SERVICE_UNAVAILABLE_MSG)) {
            currentWeatherView.displayEmptyScreenWithMsg(R.string.service_error_txt);
        } else if (currentWeatherResponse.getMessage().equals(CurrentWeatherResponse.NO_NETWORK_CONNECTION_MSG)) {
            currentWeatherView.displayEmptyScreenWithMsg(R.string.network_error_txt);
        }
    }

}
