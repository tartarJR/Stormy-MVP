package com.tatar.stormy.weatherforecast;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tatar.stormy.R;
import com.tatar.stormy.location.LocationCallback;
import com.tatar.stormy.location.LocationService;
import com.tatar.stormy.model.WeatherForecast;
import com.tatar.stormy.util.NetworkUtils;
import com.tatar.stormy.util.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherForecastActivity extends AppCompatActivity implements LocationCallback, WeatherForecastContract.View {

    public static final String TAG = WeatherForecastActivity.class.getSimpleName();

    @BindView(R.id.timeTextView)
    TextView timeTextView;
    @BindView(R.id.temperatureTextView)
    TextView temperatureTextView;
    @BindView(R.id.humidityTextView)
    TextView humidityTextView;
    @BindView(R.id.precipChanceTextView)
    TextView precipChanceTextView;
    @BindView(R.id.summaryTextView)
    TextView summaryTextView;
    @BindView(R.id.iconImageView)
    ImageView iconImageView;
    @BindView(R.id.refreshImageView)
    ImageView refreshImageView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.locationTextView)
    TextView locationTextView;

    private LocationService locationService;
    private WeatherForecastPresenter weatherForecastPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        locationService = new LocationService(this, this);
        weatherForecastPresenter = new WeatherForecastPresenter(this);
    }

    @OnClick(R.id.refreshImageView)
    void submit() {
        Log.d(TAG, "refreshImageView: click");

        if (NetworkUtils.isNetworkAvailable(this)) {
            locationService.getLastLocation();
        } else {
            displayEmptyScreenWithMsg(R.string.summary_network_error_txt);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!PermissionUtil.isPermissionsGranted(this)) {
            PermissionUtil.askPermissions(this);
        } else {
            locationService.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        locationService.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weatherForecastPresenter.stop();
    }

    @Override
    public void diplayServiceError() {
        displayEmptyScreenWithMsg(R.string.summary_service_error_txt);
    }

    @Override
    public void toggleRefresh() {
        if (progressBar.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);

            displayEmptyScreenWithMsg(R.string.summary_init_txt);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayWeatherForecastData(WeatherForecast weatherForecast) {
        timeTextView.setText(weatherForecast.getFormattedTime());
        temperatureTextView.setText(weatherForecast.getTemperature());
        humidityTextView.setText(weatherForecast.getHumidity());
        precipChanceTextView.setText(weatherForecast.getPrecipChance());
        summaryTextView.setText(weatherForecast.getSummary());
        iconImageView.setImageDrawable(getResources().getDrawable(weatherForecast.getIconId()));
        locationTextView.setText(weatherForecast.getAddress());
    }

    @Override
    public Context getContext() {
        return WeatherForecastActivity.this;
    }

    private void displayEmptyScreenWithMsg(int msgId) {
        timeTextView.setText(R.string.double_dash);
        temperatureTextView.setText(R.string.double_dash);
        humidityTextView.setText(R.string.double_dash);
        precipChanceTextView.setText(R.string.double_dash);
        locationTextView.setText(R.string.double_dash);
        iconImageView.setImageDrawable(null);
        summaryTextView.setText(msgId);
    }

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        if (NetworkUtils.isNetworkAvailable(this)) {
            weatherForecastPresenter.fetchWeatherForecastData(latitude, longitude);
        } else {
            displayEmptyScreenWithMsg(R.string.summary_network_error_txt);
        }
    }
}
