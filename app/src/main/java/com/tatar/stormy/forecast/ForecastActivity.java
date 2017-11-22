package com.tatar.stormy.forecast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tatar.stormy.R;
import com.tatar.stormy.location.LocationProviderCallback;
import com.tatar.stormy.location.LocationProvider;
import com.tatar.stormy.model.Forecast;
import com.tatar.stormy.ui.AlertDialogFragment;
import com.tatar.stormy.util.NetworkUtils;
import com.tatar.stormy.util.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForecastActivity extends AppCompatActivity implements LocationProviderCallback, ForecastView {

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

    private LocationProvider mLocationProvider;
    private ForecastPresenter forecastPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mLocationProvider = new LocationProvider(this, this);
    }

    @OnClick(R.id.refreshImageView)
    void submit() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!PermissionUtil.isPermissionsGranted(this)) {
            PermissionUtil.askPermissions(this);
        } else {
            mLocationProvider.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mLocationProvider.disconnect();
    }

    @Override
    public void displayErrorDialog() {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.show(getFragmentManager(), "error_dialog");
    }

    @Override
    public void toggleRefresh() {
        if (progressBar.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);

            timeTextView.setText(R.string.double_dash);
            temperatureTextView.setText(R.string.double_dash);
            humidityTextView.setText(R.string.double_dash);
            precipChanceTextView.setText(R.string.double_dash);
            locationTextView.setText(R.string.double_dash);
            summaryTextView.setText(R.string.summary_init_txt);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateUi(Forecast forecast, String address) {
        timeTextView.setText(forecast.getFormattedTime());
        temperatureTextView.setText(forecast.getTemperature());
        humidityTextView.setText(forecast.getHumidity());
        precipChanceTextView.setText(forecast.getPrecipChance());
        summaryTextView.setText(forecast.getSummary());
        iconImageView.setImageDrawable(getResources().getDrawable(forecast.getIconId()));
        locationTextView.setText(address);
    }

    @Override
    public void getForecast(double latitude, double longitude, String address) {
        if (NetworkUtils.isNetworkAvailable(this)) {
            forecastPresenter = new ForecastPresenter(this, latitude, longitude, address);
            forecastPresenter.execute();
        }
    }
}
