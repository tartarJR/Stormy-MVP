package com.tatar.stormy.service;

import android.util.Log;

import com.tatar.stormy.BuildConfig;
import com.tatar.stormy.model.CurrrentWeather;
import com.tatar.stormy.model.DailyWeather;
import com.tatar.stormy.model.Forecast;
import com.tatar.stormy.model.HourlyWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by musta on 11/21/2017.
 */

public class WeatherForecastService {

    public static final String TAG = WeatherForecastService.class.getSimpleName();

    private static final String BASE_URL = "https://api.darksky.net/forecast/";
    private static final String API_KEY = BuildConfig.DARK_SKY_API_KEY;

    // TODO read temperature unit from preferences
    private static final String FORECAST_URL = BASE_URL + API_KEY + "/";
    private static final String TEMP_UNIT = "?units=si";

    private OkHttpClient okHttpClient;

    public WeatherForecastService() {
        okHttpClient = new OkHttpClient();
    }

    public Forecast getWeatherForecastData(double latitude, double longitude) {

        Forecast forecast = null;

        CurrrentWeather currrentWeather = null;

        Request request = new Request.Builder()
                .url(FORECAST_URL + latitude + "," + longitude + TEMP_UNIT)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            String jsonData = response.body().string();

            if (response.isSuccessful()) {
                forecast = JsonUtils.parseForecastDetails(jsonData);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException Caught: " + e);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException Caught: " + e);
        }

        return forecast;
    }

    // TODO use gson or jackson, remove static class
    private static class JsonUtils {

        private static final String TAG = JsonUtils.class.getSimpleName();

        // JSON keys
        private static final String CURRENTLY_WEATHER = "currently";
        private static final String HOURLY_WEATHER = "hourly";
        private static final String DAILY_WEATHER = "daily";

        private static final String HOURLY_DATA= "data";

        private static final String ICON_KEY = "icon";
        private static final String TIME_KEY = "time";
        private static final String TEMPERATURE_KEY = "temperature";
        private static final String TEMPERATURE_MAX_KEY = "temperatureMax";
        private static final String HUMIDITY_KEY = "humidity";
        private static final String PRECIP_PROB_KEY = "precipProbability";
        private static final String SUMMARY_KEY = "summary";
        private static final String TIMEZONE_KEY = "timezone";

        public static Forecast parseForecastDetails(String jsonData) throws JSONException {
            Forecast forecast = new Forecast();

            forecast.setCurrrentWeather(parseCurrentWeather(jsonData));
            forecast.setHourlyWeathers(parseHourlyWeather(jsonData));
            forecast.setDailyWeathers(parseDailyWeather(jsonData));

            return forecast;
        }

        // parses given JSON string to a CurrrentWeather object
        private static CurrrentWeather parseCurrentWeather(String jsonData) throws JSONException {
            JSONObject forecast = new JSONObject(jsonData);
            JSONObject currently = forecast.getJSONObject(CURRENTLY_WEATHER);

            CurrrentWeather currentWeather = new CurrrentWeather();
            currentWeather.setIcon(currently.getString(ICON_KEY));
            currentWeather.setTime(currently.getLong(TIME_KEY));
            currentWeather.setTemperature(currently.getDouble(TEMPERATURE_KEY));
            currentWeather.setHumidity(currently.getDouble(HUMIDITY_KEY));
            currentWeather.setPrecipChance(currently.getDouble(PRECIP_PROB_KEY));
            currentWeather.setSummary(currently.getString(SUMMARY_KEY));
            currentWeather.setTimeZone(forecast.getString(TIMEZONE_KEY));

            Log.i(TAG, currentWeather.toString());

            return currentWeather;
        }

        private static HourlyWeather[] parseHourlyWeather(String jsonData) throws JSONException {

            JSONObject forecast = new JSONObject(jsonData);
            JSONObject hourly = forecast.getJSONObject(HOURLY_WEATHER);
            JSONArray hourlyData = hourly.getJSONArray(HOURLY_DATA);

            Log.i(TAG, "hourly: " + hourlyData.toString());

            HourlyWeather[] hourlyWeatherData = new HourlyWeather[hourlyData.length()];
            HourlyWeather hourlyWeather;

            for (int i = 0; i < hourlyData.length() ; i++) {
                hourlyWeather = new HourlyWeather();
                hourlyWeather.setTime(hourlyData.getJSONObject(i).getLong(TIME_KEY));
                hourlyWeather.setSummary(hourlyData.getJSONObject(i).getString(SUMMARY_KEY));
                hourlyWeather.setTemperature(hourlyData.getJSONObject(i).getDouble(TEMPERATURE_KEY));
                hourlyWeather.setIcon(hourlyData.getJSONObject(i).getString(ICON_KEY));
                hourlyWeather.setTimezone(forecast.getString(TIMEZONE_KEY));

                hourlyWeatherData[i] = hourlyWeather;
            }

            return hourlyWeatherData;
        }

        private static DailyWeather[] parseDailyWeather(String jsonData) throws JSONException {

            JSONObject forecast = new JSONObject(jsonData);
            JSONObject daily = forecast.getJSONObject(DAILY_WEATHER);
            JSONArray dailyData = daily.getJSONArray(HOURLY_DATA);

            Log.i(TAG, "daily: " + dailyData.toString());

            DailyWeather[] dailyWeatherData = new DailyWeather[dailyData.length()];
            DailyWeather dailyWeather;

            for (int i = 0; i < dailyData.length() ; i++) {
                dailyWeather = new DailyWeather();
                dailyWeather.setTime(dailyData.getJSONObject(i).getLong(TIME_KEY));
                dailyWeather.setSummary(dailyData.getJSONObject(i).getString(SUMMARY_KEY));
                dailyWeather.setMaxTemperature(dailyData.getJSONObject(i).getDouble(TEMPERATURE_MAX_KEY));
                dailyWeather.setIcon(dailyData.getJSONObject(i).getString(ICON_KEY));
                dailyWeather.setTimezone(forecast.getString(TIMEZONE_KEY));

                dailyWeatherData[i] = dailyWeather;
            }

            return dailyWeatherData;
        }
    }
}
