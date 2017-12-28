package com.tatar.stormy.weatherforecast;

import android.util.Log;

import com.tatar.stormy.location.LocationCallback;
import com.tatar.stormy.location.LocationService;

public class CurrentWeatherPresenter implements CurrentWeatherContract.Presenter, LocationCallback {

    public static final String TAG = CurrentWeatherPresenter.class.getSimpleName();

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
        Log.d(TAG, "onLocationReceived: hit");

        Double[] locationParams = {latitude, longitude};
        currentWeatherTask = new CurrentWeatherTask(view); // TODO memory leak is possible here, check it later
        currentWeatherTask.execute(locationParams);
    }
}
