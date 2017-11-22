package com.tatar.stormy.forecast;

import com.tatar.stormy.model.Forecast;

/**
 * Created by musta on 11/22/2017.
 */

public interface ForecastView {

    void displayErrorDialog();

    void toggleRefresh();

    void updateUi(Forecast forecast, String address);

}
