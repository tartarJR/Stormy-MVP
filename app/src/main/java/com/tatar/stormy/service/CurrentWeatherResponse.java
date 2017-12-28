package com.tatar.stormy.service;

import com.tatar.stormy.model.CurrrentWeather;

public class CurrentWeatherResponse {

    public static final String SUCCESS_MSG = "success";
    public static final String NO_NETWORK_CONNECTION_MSG = "network-unavailable";
    public static final String SERVICE_UNAVAILABLE_MSG = "service-unavailable";

    private String message;
    private CurrrentWeather currrentWeather;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CurrrentWeather getCurrrentWeather() {
        return currrentWeather;
    }

    public void setCurrrentWeather(CurrrentWeather currrentWeather) {
        this.currrentWeather = currrentWeather;
    }
}
