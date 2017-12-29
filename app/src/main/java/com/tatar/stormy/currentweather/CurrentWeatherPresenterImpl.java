package com.tatar.stormy.currentweather;

import com.tatar.stormy.location.LocationCallback;
import com.tatar.stormy.location.LocationService;
import com.tatar.stormy.service.WeatherForecastTask;

public class CurrentWeatherPresenterImpl implements CurrentWeatherContract.CurrentWeatherPresenter, LocationCallback {

    private CurrentWeatherContract.CurrentWeatherView currentWeatherView;
    private CurrentWeatherContract.Navigator navigator;
    private WeatherForecastTask weatherForecastTask;
    private LocationService locationService;

    public CurrentWeatherPresenterImpl(CurrentWeatherContract.CurrentWeatherView currentWeatherView, CurrentWeatherContract.Navigator navigator) {
        this.currentWeatherView = currentWeatherView;
        this.navigator = navigator;
        locationService = new LocationService(currentWeatherView.getContext(), this);
    }

    @Override
    public void connectToLocationService() {
        locationService.connect();
    }

    @Override
    public void disconnectFromLocationService() {
        locationService.disconnect();
    }

    @Override
    public void getLastLocation() {
        locationService.getLastLocation();
    }

    @Override
    public void cancelCurrentWeatherTask() {
        if (weatherForecastTask != null && weatherForecastTask.getStatus() == WeatherForecastTask.TASK_STATUS_RUNNING) {
            weatherForecastTask.cancel(true);
        }
    }

    @Override
    public void openHourlyWeatherActivity() {
        navigator.openHourlyWeatherActivity();
    }

    @Override
    public void openDailyWeatherActivity() {
        navigator.openDailyWeatherActivity();
    }

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        Double[] locationParams = {latitude, longitude};
        weatherForecastTask = new WeatherForecastTask(currentWeatherView); // TODO memory leak is possible here, check it later
        weatherForecastTask.execute(locationParams);
    }
}
