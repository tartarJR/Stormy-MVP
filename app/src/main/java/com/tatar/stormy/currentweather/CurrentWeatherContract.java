package com.tatar.stormy.currentweather;

import android.content.Context;

import com.tatar.stormy.model.CurrrentWeather;

public interface CurrentWeatherContract {

    interface CurrentWeatherPresenter {
        void connectToLocationService();

        void disconnectFromLocationService();

        void getLastLocation();

        void cancelCurrentWeatherTask();

        void openHourlyWeatherActivity();

        void openDailyWeatherActivity();
    }

    interface CurrentWeatherView {
        void displayEmptyScreenWithMsg(int msgId);

        void toggleRefresh();

        void displayCurrentWeatherData(CurrrentWeather currrentWeather);

        Context getContext();
    }

    interface Navigator {
        void openHourlyWeatherActivity();

        void openDailyWeatherActivity();
    }

}
