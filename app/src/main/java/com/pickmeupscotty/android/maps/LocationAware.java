package com.pickmeupscotty.android.maps;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by axel on 07/03/15.
 */
public abstract class LocationAware extends Activity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private boolean mConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        mConnected = false;
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

    private LocationRequest createLocationRequest() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return request;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mConnected = true;
        onLocationChanged(getLastLocation());
        startLocationUpdates();
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
        if (mConnected) {
            return LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }
        throw new IllegalStateException("Needs to be connected before fetching position");
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private void startLocationUpdates() {
        if (mConnected) {
            LocationRequest request = createLocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, request, this);
        }
    }
}
