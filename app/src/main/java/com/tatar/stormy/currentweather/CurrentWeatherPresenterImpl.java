package com.tatar.stormy.currentweather;

import com.tatar.stormy.location.LocationCallback;
import com.tatar.stormy.location.LocationService;

public class CurrentWeatherPresenterImpl implements CurrentWeatherContract.CurrentWeatherPresenter, LocationCallback {

    private CurrentWeatherContract.CurrentWeatherView currentWeatherView;
    private CurrentWeatherTask currentWeatherTask;
    private LocationService locationService;

    public CurrentWeatherPresenterImpl(CurrentWeatherContract.CurrentWeatherView currentWeatherView) {
        this.currentWeatherView = currentWeatherView;
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
        if (currentWeatherTask != null && currentWeatherTask.getStatus() == CurrentWeatherTask.TASK_STATUS_RUNNING) {
            currentWeatherTask.cancel(true);
        }
    }

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        Double[] locationParams = {latitude, longitude};
        currentWeatherTask = new CurrentWeatherTask(currentWeatherView); // TODO memory leak is possible here, check it later
        currentWeatherTask.execute(locationParams);
    }
}
