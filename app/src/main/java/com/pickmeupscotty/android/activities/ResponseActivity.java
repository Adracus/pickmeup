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
    private String facebookId;
    private double destinationLatitude;
    private double destinationLongitude;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
//        facebookId = getIntent().getStringExtra("FACEBOOK_ID");
//        destinationLatitude = getIntent().getDoubleExtra(PickUpRequest.DESTINATION_LATITUDE, 0.0);
//        destinationLongitude = getIntent().getDoubleExtra(PickUpRequest.DESTINATION_LONGITUDE, 0.0);
//        currentLatitude = getIntent().getDoubleExtra(PickUpRequest.CURRENT_LATITUDE, 0.0);
//        currentLongitude = getIntent().getDoubleExtra(PickUpRequest.CURRENT_LONGITUDE, 0.0);
        facebookId = "Test";
        destinationLatitude = 49.021965;
        destinationLongitude = 8.3703167;
        currentLatitude = 49.01312;
        currentLongitude = 8.424298;

        TextView nameView = (TextView) findViewById(R.id.textView4);
        nameView.setText(facebookId);

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.detourmap);
        mMap = mMapFragment.getMap();
        mMap.setMyLocationEnabled(true);

        LatLng destinationPosition = new LatLng(destinationLatitude, destinationLongitude);
        LatLng currentPosition = new LatLng(currentLatitude, currentLongitude);

        Marker home = mMap.addMarker(new MarkerOptions()
                .position(destinationPosition)
                .draggable(true));

        Marker work = mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_response, menu);
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
