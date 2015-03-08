package com.pickmeupscotty.android.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.amqp.Subscriber;
import com.pickmeupscotty.android.login.FBWrapper;
import com.pickmeupscotty.android.maps.ChooseDestinationDialogFragment;
import com.pickmeupscotty.android.maps.ChoosePlaceDialogFragment;
import com.pickmeupscotty.android.maps.GooglePlaces;
import com.pickmeupscotty.android.maps.LocationAware;
import com.pickmeupscotty.android.messages.PickUpRequest;
import com.pickmeupscotty.android.messages.PickUpResponse;

import java.util.List;

public class PickMeUpActivity extends LocationAware implements ChooseDestinationDialogFragment.ChooseDestinationDialogListener, ChoosePlaceDialogFragment.ChoosePlaceListener {
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_me_up);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap = mMapFragment.getMap();
        mMap.setMyLocationEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.driver_button);
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
    public void onDialogPositiveClick(List<GooglePlaces.Place> places) {
        if (places.isEmpty()) {
            Toast
                    .makeText(this, "Could not find any matching location", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        ChoosePlaceDialogFragment placeChooser = new ChoosePlaceDialogFragment();
        placeChooser.setPlaces(places);
        placeChooser.show(getFragmentManager(), "place_choose");
    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {
        onCancelPickupRequest();
    }

    private void onCancelPickupRequest() {
        Toast
                .makeText(this, "Canceled pick up request", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void placeChosen(final GooglePlaces.Place place) {
        FBWrapper.INSTANCE.getMyUser(new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser graphUser, Response response) {
                PickUpRequest request = new PickUpRequest(
                        mLastLocation.getLatitude(),
                        mLastLocation.getLongitude(),
                        place.getLatitude(),
                        place.getLongitude(),
                        graphUser.getId(),
                        graphUser.getName());
                RabbitService.send(request);
                Toast
                        .makeText(PickMeUpActivity.this, "Sent pickup request", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(PickMeUpActivity.this, WaitForPickUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void canceled() {
        onCancelPickupRequest();
    }
}
