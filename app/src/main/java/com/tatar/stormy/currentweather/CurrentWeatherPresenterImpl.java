package com.tatar.stormy.currentweather;

import com.tatar.stormy.location.AddressFinder;
import com.tatar.stormy.location.LocationService;
import com.tatar.stormy.util.NetworkUtil;

public class CurrentWeatherPresenterImpl implements CurrentWeatherContract.CurrentWeatherPresenter {

    private CurrentWeatherContract.CurrentWeatherView currentWeatherView;
    private CurrentWeatherContract.Navigator navigator;
    private CurrentWeatherTask currentWeatherTask;
    private AddressFinder addressFinder;
    private LocationService locationService;
    private NetworkUtil networkUtil;

    public CurrentWeatherPresenterImpl(CurrentWeatherContract.CurrentWeatherView currentWeatherView, CurrentWeatherContract.Navigator navigator, LocationService locationService, AddressFinder addressFinder, NetworkUtil networkUtil) {
        this.currentWeatherView = currentWeatherView;
        this.navigator = navigator;
        this.locationService = locationService;
        this.addressFinder = addressFinder;
        this.networkUtil = networkUtil;
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
    public void presentCurrentWeatherForecast(double latitude, double longitude) {
        Double[] locationParams = {latitude, longitude};
        currentWeatherTask = new CurrentWeatherTask(currentWeatherView, addressFinder, networkUtil); // TODO memory leak is possible here, check it later
        currentWeatherTask.execute(locationParams);
    }

    @Override
    public void refreshCurrentWeatherForecast() {
        locationService.getLastLocation();
    }

    @Override
    public void cancelCurrentWeatherTask() {
        if (currentWeatherTask != null && currentWeatherTask.getStatus() == CurrentWeatherTask.TASK_STATUS_RUNNING) {
            currentWeatherTask.cancel(true);
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
}
