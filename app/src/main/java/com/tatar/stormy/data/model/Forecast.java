package com.tatar.stormy.data.model;

public class Forecast {

    private CurrrentWeather currrentWeather;
    private HourlyWeather[] hourlyWeathers;
    private DailyWeather[] dailyWeathers;

    public CurrrentWeather getCurrrentWeather() {
        return currrentWeather;
    }

    public void setCurrrentWeather(CurrrentWeather currrentWeather) {
        this.currrentWeather = currrentWeather;
    }

    public HourlyWeather[] getHourlyWeathers() {
        return hourlyWeathers;
    }

    public void setHourlyWeathers(HourlyWeather[] hourlyWeathers) {
        this.hourlyWeathers = hourlyWeathers;
    }

    public DailyWeather[] getDailyWeathers() {
        return dailyWeathers;
    }

    public void setDailyWeathers(DailyWeather[] dailyWeathers) {
        this.dailyWeathers = dailyWeathers;
    }
}
