package com.pickmeupscotty.android.activities;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.MapFragment;
import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.maps.LocationAware;

public class PickMeUp extends LocationAware {
    MapFragment mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_me_up);
        mMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        if (null != mLastLocation) {
            mMap.getMap().getMyLocation().set(mLastLocation);
            return;
        }
        Toast
            .makeText(this, "Could not fetch latest position", Toast.LENGTH_LONG)
            .show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast
            .makeText(this, "Couldn't connect to Maps", Toast.LENGTH_SHORT)
            .show();
    }
}
