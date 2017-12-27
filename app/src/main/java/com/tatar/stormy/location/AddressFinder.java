package com.tatar.stormy.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class AddressFinder {

    public static final String TAG = AddressFinder.class.getSimpleName();

    private Context context;

    public AddressFinder(Context context) {
        this.context = context;
    }

    // TODO check if Geocoder present
    public String getCompleteAddress(double latitude, double longitude) {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    if (!returnedAddress.getAddressLine(i).isEmpty() || !returnedAddress.getAddressLine(i).equals(null)) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i));
                    }
                    if (i != returnedAddress.getMaxAddressLineIndex()) {
                        strReturnedAddress.append(", ");
                    }
                }

                address = strReturnedAddress.toString();
                Log.w(TAG, strReturnedAddress.toString());
            } else {
                address = "Unknown Location";
                Log.d(TAG, "No Address returned.");
            }
        } catch (Exception e) {
            address = "Unknown Location";
            Log.e(TAG, "Failed to get adddress: " + e);
        }

        return address;
    }
}
