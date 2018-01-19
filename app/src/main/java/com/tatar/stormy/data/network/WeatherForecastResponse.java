package com.tatar.stormy.data.network;

import com.tatar.stormy.data.model.Forecast;

public class WeatherForecastResponse {

    public static final String SUCCESS_MSG = "success";
    public static final String NO_NETWORK_CONNECTION_MSG = "network-unavailable";
    public static final String SERVICE_UNAVAILABLE_MSG = "service-unavailable";

    private String message;
    private Forecast forecast;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
}
