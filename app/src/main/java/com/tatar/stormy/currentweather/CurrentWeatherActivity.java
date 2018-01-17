package com.tatar.stormy.currentweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tatar.stormy.R;
import com.tatar.stormy.dailyweather.DailyWeatherActivity;
import com.tatar.stormy.hourlyweather.HourlyWeatherActivity;
import com.tatar.stormy.location.AddressFinder;
import com.tatar.stormy.location.LocationCallback;
import com.tatar.stormy.location.LocationService;
import com.tatar.stormy.model.CurrrentWeather;
import com.tatar.stormy.util.NetworkUtil;
import com.tatar.stormy.util.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurrentWeatherActivity extends AppCompatActivity implements CurrentWeatherContract.CurrentWeatherView, CurrentWeatherContract.Navigator, LocationCallback {

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

    private NetworkUtil networkUtil;
    private LocationService locationService;
    private AddressFinder addressFinder;
    private CurrentWeatherContract.CurrentWeatherPresenter currentWeatherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        networkUtil = new NetworkUtil(CurrentWeatherActivity.this);
        locationService = new LocationService(CurrentWeatherActivity.this, this);
        addressFinder = new AddressFinder(CurrentWeatherActivity.this);
        currentWeatherPresenter = new CurrentWeatherPresenterImpl(this, this, locationService, addressFinder, networkUtil);
    }

    @OnClick(R.id.refreshImageView)
    void refresh() {
        currentWeatherPresenter.refreshCurrentWeatherForecast();
    }

    @OnClick(R.id.hourlyButton)
    void goToHourly() {
        currentWeatherPresenter.openHourlyWeatherActivity();
    }

    @OnClick(R.id.dailyButton)
    void goToDaily() {
        currentWeatherPresenter.openDailyWeatherActivity();
    }

    @Override
    public void openHourlyWeatherActivity() {
        Intent navigationIntent = new Intent(CurrentWeatherActivity.this, HourlyWeatherActivity.class);
        startActivity(navigationIntent);
    }

    @Override
    public void openDailyWeatherActivity() {
        Intent navigationIntent = new Intent(CurrentWeatherActivity.this, DailyWeatherActivity.class);
        startActivity(navigationIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PermissionUtil.isPermissionsGranted(this)) {
            currentWeatherPresenter.connectToLocationService();
        } else {
            PermissionUtil.askPermissions(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentWeatherPresenter.disconnectFromLocationService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentWeatherPresenter.cancelCurrentWeatherTask();
    }

    @Override
    public void displayEmptyScreenWithMsg(int msgId) {
        timeTextView.setText(R.string.double_dash);
        temperatureTextView.setText(R.string.double_dash);
        humidityTextView.setText(R.string.double_dash);
        precipChanceTextView.setText(R.string.double_dash);
        locationTextView.setText(R.string.double_dash);
        iconImageView.setImageDrawable(null);
        summaryTextView.setText(msgId);
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
    public void displayCurrentWeatherData(CurrrentWeather currrentWeather) {
        timeTextView.setText(currrentWeather.getFormattedTime());
        temperatureTextView.setText(currrentWeather.getTemperature());
        humidityTextView.setText(currrentWeather.getHumidity());
        precipChanceTextView.setText(currrentWeather.getPrecipChance());
        summaryTextView.setText(currrentWeather.getSummary());
        iconImageView.setImageDrawable(getResources().getDrawable(currrentWeather.getIconId()));
        locationTextView.setText(currrentWeather.getAddress());
    }

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        currentWeatherPresenter.presentCurrentWeatherForecast(latitude, longitude);
    }
}
