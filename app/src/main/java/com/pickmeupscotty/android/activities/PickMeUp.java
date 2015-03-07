package com.pickmeupscotty.android.activities;

import android.app.DialogFragment;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.amqp.Subscriber;
import com.pickmeupscotty.android.maps.ChooseDestinationDialogFragment;
import com.pickmeupscotty.android.maps.LocationAware;
import com.pickmeupscotty.android.messages.PickUpResponse;

public class PickMeUp extends LocationAware implements ChooseDestinationDialogFragment.ChooseDestinationDialogListener {
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private AddFloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_me_up);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap = mMapFragment.getMap();
        mMap.setMyLocationEnabled(true);

        fab = (AddFloatingActionButton) findViewById(R.id.pickup_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ChooseDestinationDialogFragment();
                newFragment.show(getFragmentManager(), "destination");
            }
        });
        fab.setEnabled(false);

        RabbitService.subscribe(PickUpResponse.class, new Subscriber<PickUpResponse>() {
            @Override
            public void on(PickUpResponse request) {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        if (null != mLastLocation) {
            fab.setEnabled(true);
            LatLng position = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            CameraUpdate myLocation = CameraUpdateFactory.newLatLng(position);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

            mMap.moveCamera(myLocation);
            mMap.animateCamera(zoom);
        }
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

    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {

    }
}
