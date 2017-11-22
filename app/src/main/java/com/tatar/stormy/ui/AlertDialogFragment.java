package com.tatar.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.tatar.stormy.R;

/**
 * Created by musta on 11/17/2017.
 */

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
            .setTitle(context.getString(R.string.alert_dialog_title))
            .setMessage(context.getString(R.string.alert_dialog_msg))
            .setPositiveButton(context.getString(R.string.alert_dialog_btn_txt), null);

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }
}
