package com.tatar.stormy.weatherforecast;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tatar.stormy.R;
import com.tatar.stormy.model.CurrrentWeather;
import com.tatar.stormy.util.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurrentWeatherActivity extends AppCompatActivity implements CurrentWeatherContract.View {

    public static final String TAG = CurrentWeatherActivity.class.getSimpleName();

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

    private CurrentWeatherPresenter currentWeatherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        currentWeatherPresenter = new CurrentWeatherPresenter(this);
    }

    @OnClick(R.id.refreshImageView)
    void submit() {
        currentWeatherPresenter.getLastLocation();
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
    public Context getContext() {
        return CurrentWeatherActivity.this;
    }
}
