package com.pickmeupscotty.android.maps;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by axel on 08/03/15.
 */
public class AsyncDirectionRequest extends AsyncTask<LatLng, Void, List<LatLng>> {

    @Override
    protected List<LatLng> doInBackground(LatLng... params) {
        return GooglePlaces.directionsFor(params[0], params[1], params[2]);
    }
}
