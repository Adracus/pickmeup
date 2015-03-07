package com.pickmeupscotty.android.maps;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by axel on 07/03/15.
 */
public abstract class LocationAware extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public float distanceTo(Location other) {
        if (null == mLastLocation) {
            mLastLocation = getLastLocation();
        }
        return mLastLocation.distanceTo(other);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = getLastLocation();
    }

    /**
     * Returns the last known location of the device.
     *
     * Caution: This should be called only after the onConnected
     * callback, otherwise this will raise an exception
     *
     * @return The last known location, could be null
     */
    protected Location getLastLocation() {
        return LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
    }
}
