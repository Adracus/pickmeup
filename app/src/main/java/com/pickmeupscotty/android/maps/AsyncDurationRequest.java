package com.pickmeupscotty.android.maps;

import android.os.AsyncTask;

/**
 * Created by axel on 08/03/15.
 */
public class AsyncDurationRequest extends AsyncTask<Double, Void, GooglePlaces.Distance> {

    @Override
    protected GooglePlaces.Distance doInBackground(Double... params) {
        double fromLatitude = params[0];
        double fromLongitude = params[1];
        double toLatitude = params[2];
        double toLongitude = params[3];

        return GooglePlaces.durationBetween(fromLatitude, fromLongitude, toLatitude, toLongitude);
    }
}
