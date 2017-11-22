package com.tatar.stormy.networking;

import android.util.Log;

import com.tatar.stormy.BuildConfig;
import com.tatar.stormy.location.LocationProvider;
import com.tatar.stormy.model.Forecast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by musta on 11/21/2017.
 */

public class ForecastService {

    public static final String TAG = ForecastService.class.getSimpleName();

    private static final String BASE_URL = "https://api.darksky.net/forecast/";
    private static final String API_KEY = BuildConfig.DARK_SKY_API_KEY;

    // TODO read temperature unit from preferences
    private static final String FORECAST_URL = BASE_URL + API_KEY + "/";
    private static final String TEMP_UNIT = "?units=si";

    private OkHttpClient okHttpClient;

    public ForecastService() {
        okHttpClient = new OkHttpClient();
    }

    public Forecast getCurrentWeather(double latitude, double longitude) {

        Forecast forecast = null;

        Request request = new Request.Builder()
                .url(FORECAST_URL + latitude + "," + longitude + TEMP_UNIT)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            String jsonData = response.body().string();

            if (response.isSuccessful()) {
                forecast = JsonUtils.getCurrentWeather(jsonData);
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
        private static final String ICON_KEY = "icon";
        private static final String TIME_KEY = "time";
        private static final String TEMPERATURE_KEY = "temperature";
        private static final String HUMIDITY_KEY = "humidity";
        private static final String PRECIP_PROB_KEY = "precipProbability";
        private static final String SUMMARY_KEY = "summary";
        private static final String TIMEZONE_KEY = "timezone";
        private static final String TIMEZONE_CURRENTLY = "currently";

        // parses given JSON string to a Forecast object
        public static Forecast getCurrentWeather(String jsonData) throws JSONException {
            JSONObject forecast = new JSONObject(jsonData);
            JSONObject currently = forecast.getJSONObject(TIMEZONE_CURRENTLY);

            Forecast currentWeather = new Forecast();
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
    }
}
