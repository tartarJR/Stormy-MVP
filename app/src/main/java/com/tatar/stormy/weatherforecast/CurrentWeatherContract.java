package com.tatar.stormy.weatherforecast;

import android.content.Context;

import com.tatar.stormy.model.CurrrentWeather;

public interface CurrentWeatherContract {

    interface CurrentWeatherPresenter {
        void connectToLocationService();

        void disconnectFromLocationService();

        void getLastLocation();

        void cancelCurrentWeatherTask();
    }

    interface CurrentWeatherView {
        void displayEmptyScreenWithMsg(int msgId);

        void toggleRefresh();

        void displayCurrentWeatherData(CurrrentWeather currrentWeather);

        Context getContext();
    }

}
