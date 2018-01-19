package com.tatar.stormy.ui.currentweather;

import com.tatar.stormy.data.model.CurrrentWeather;

public interface CurrentWeatherContract {

    interface CurrentWeatherPresenter {
        void connectToLocationService();

        void disconnectFromLocationService();

        void presentCurrentWeatherForecast(double latitude, double longitude);

        void refreshCurrentWeatherForecast();

        void cancelCurrentWeatherTask();

        void openHourlyWeatherActivity();

        void openDailyWeatherActivity();
    }

    interface CurrentWeatherView {
        void displayEmptyScreenWithMsg(int msgId);

        void toggleRefresh();

        void displayCurrentWeatherData(CurrrentWeather currrentWeather);
    }

    interface Navigator {
        void openHourlyWeatherActivity();

        void openDailyWeatherActivity();
    }

}
