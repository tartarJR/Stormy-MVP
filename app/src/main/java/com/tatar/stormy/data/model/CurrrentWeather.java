package com.tatar.stormy.data.model;

import com.tatar.stormy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by musta on 11/17/2017.
 */

public class CurrrentWeather {
    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;
    private String timeZone;
    private String address;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTemperature() {
        return Integer.toString((int) Math.round(temperature));
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return Double.toString(humidity);
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getPrecipChance() {
        return Integer.toString((int) Math.round(precipChance * 100)) + "%";
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        String timeString = formatter.format(new Date(getTime() * 1000));

        return timeString;
    }

    public int getIconId() {
        int iconId = R.drawable.clear_day;

        if (icon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        } else if (icon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        } else if (icon.equals("rain")) {
            iconId = R.drawable.rain;
        } else if (icon.equals("snow")) {
            iconId = R.drawable.snow;
        } else if (icon.equals("sleet")) {
            iconId = R.drawable.sleet;
        } else if (icon.equals("wind")) {
            iconId = R.drawable.wind;
        } else if (icon.equals("fog")) {
            iconId = R.drawable.fog;
        } else if (icon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        } else if (icon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        } else if (icon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CurrrentWeather{" +
                "icon='" + icon + '\'' +
                ", time=" + getFormattedTime() +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", precipChance=" + precipChance +
                ", summary='" + summary + '\'' +
                '}';
    }
}
