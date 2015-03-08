package com.pickmeupscotty.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.messages.PickUpRequest;
import com.pickmeupscotty.android.messages.PickUpResponse;

public class ResponseActivity extends Activity {
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private boolean movedMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PickUpRequest request = savedInstanceState.getParcelable(PickUpRequest.PICK_UP_REQUEST);

        TextView nameView = (TextView) findViewById(R.id.textView4);
        nameView.setText(request.getFacebookId());

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.detourmap);
        mMap = mMapFragment.getMap();
        mMap.setMyLocationEnabled(true);

        LatLng destinationPosition = new LatLng(
                request.getDestinationLatitude(),
                request.getDestinationLongitude());
        LatLng currentPosition = new LatLng(
                request.getCurrentLatitude(),
                request.getCurrentLongitude());

        Marker home = mMap.addMarker(new MarkerOptions()
                .position(destinationPosition)
                .title("Destination")
                .draggable(true));

        Marker work = mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .title("Current")
                .draggable(true));

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        bounds.include(destinationPosition);
        bounds.include(currentPosition);
        moveMapToBounds(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
    }

    public void acceptPickUp(View view) {
        //RabbitService.send(new PickUpResponse(), facebookId);
        Intent intent = new Intent(this, DriverActivity.class);
        startActivity(intent);
    }

    public void denyPickUp(View view) {
        Intent intent = new Intent(this, DriverActivity.class);
        startActivity(intent);
    }

    private void moveMapToBounds(final CameraUpdate update) {
        try {
            if (movedMap) {
                // Move map smoothly from the current position.
                mMap.animateCamera(update);
            } else {
                // Move the map immediately to the starting position.
                mMap.moveCamera(update);
                movedMap = true;
            }
        } catch (IllegalStateException e) {
            // Map may not be laid out yet.
            getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                moveMapToBounds(update);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
