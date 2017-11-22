package com.tatar.stormy.forecast;

import android.os.AsyncTask;

import com.tatar.stormy.model.Forecast;
import com.tatar.stormy.networking.ForecastService;

/**
 * Created by musta on 11/22/2017.
 */

public class ForecastPresenter extends AsyncTask<Void, Void, Forecast> {

    private double latitude;
    private double longitude;
    private String address;

    private ForecastService forecastService;
    private ForecastView forecastView;

    public ForecastPresenter(ForecastView forecastView, double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;

        this.forecastView = forecastView;
        forecastService = new ForecastService();
    }

    @Override
    protected void onPreExecute() {
        forecastView.toggleRefresh();
    }

    @Override
    protected Forecast doInBackground(Void... voids) {
        return forecastService.getCurrentWeather(latitude, longitude);
    }

    @Override
    protected void onPostExecute(Forecast forecast) {
        forecastView.toggleRefresh();

        if (forecast != null) {
            forecastView.updateUi(forecast, address);
        } else {
            forecastView.displayErrorDialog();
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }
}
