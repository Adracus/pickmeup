package com.pickmeupscotty.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.messages.PickUpResponse;

import java.util.List;

public class RequestAcceptedActivity extends Activity {
    private PickUpResponse response;
    private GoogleMap mMap;
    private boolean movedMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_accepted);
        response = getIntent().getParcelableExtra(PickUpResponse.PICK_UP_RESPONSE);
        TextView etaMessageTextView = (TextView) findViewById(R.id.etaMessageTextView);
        etaMessageTextView.setText(String.format(
                getResources().getString(R.string.request_accepted_eta_message),
                response.getDriverName(),
                response.getEta()));
        MapFragment mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap = mMapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.clear();
        LatLng destinationPosition = new LatLng(response.getDestinationLatitude(), response.getDestinationLongitude());
        LatLng driverPosition = new LatLng(response.getDriverLatitude(), response.getDriverLongitude());
        LatLng friendPosition = new LatLng(response.getFriendLatitude(), response.getFriendLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(destinationPosition)
                .draggable(true));
        mMap.addMarker(new MarkerOptions()
                .position(driverPosition)
                .draggable(true));
        mMap.addMarker(new MarkerOptions()
                .position(friendPosition)
                .draggable(true));

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        bounds.include(destinationPosition);
        bounds.include(driverPosition);
        bounds.include(friendPosition);
        moveMapToBounds(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_accepted, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
