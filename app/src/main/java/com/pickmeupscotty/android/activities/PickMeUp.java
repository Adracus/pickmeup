package com.pickmeupscotty.android.activities;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.maps.LocationAware;

public class PickMeUp extends LocationAware {
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_me_up);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap = mMapFragment.getMap();

        fab = (FloatingActionButton) findViewById(R.id.pickup_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PickMeUp.this, "You did it", Toast.LENGTH_SHORT).show();
            }
        });
        fab.setEnabled(false);
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        updateUI();
    }

    private synchronized void updateUI() {
        zoomToCurrentLocation();
    }

    private void zoomToCurrentLocation() {
        if (null != mLastLocation) {
            fab.setEnabled(true);
            mMap.clear();

            LatLng position = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            CameraUpdate myLocation = CameraUpdateFactory.newLatLng(position);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(20);

            mMap.moveCamera(myLocation);
            mMap.animateCamera(zoom);

            mMap.addMarker(new MarkerOptions().position(position).title("You are here!"));
            return;
        }
        Toast
            .makeText(this, "Could not fetch latest position", Toast.LENGTH_LONG)
            .show();
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        updateUI();
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
