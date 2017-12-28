package com.tatar.stormy.weatherforecast;

import android.content.Context;

import com.tatar.stormy.model.CurrrentWeather;

public interface CurrentWeatherContract {

    interface Presenter {
        void connectToLocationService();

        void disconnectFromLocationService();

        void getLastLocation();

        void cancelCurrentWeatherTask();
    }

    interface View {
        void displayEmptyScreenWithMsg(int msgId);

        void toggleRefresh();

        void displayCurrentWeatherData(CurrrentWeather currrentWeather);

        Context getContext();
    }

}
