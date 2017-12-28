package com.tatar.stormy.weatherforecast;

import com.tatar.stormy.location.LocationCallback;
import com.tatar.stormy.location.LocationService;

public class CurrentWeatherPresenter implements CurrentWeatherContract.Presenter, LocationCallback {

    private CurrentWeatherContract.View view;
    private CurrentWeatherTask currentWeatherTask;
    private LocationService locationService;

    public CurrentWeatherPresenter(CurrentWeatherContract.View view) {
        this.view = view;
        locationService = new LocationService(view.getContext(), this);
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
        currentWeatherTask = new CurrentWeatherTask(view); // TODO memory leak is possible here, check it later
        currentWeatherTask.execute(locationParams);
    }
}
