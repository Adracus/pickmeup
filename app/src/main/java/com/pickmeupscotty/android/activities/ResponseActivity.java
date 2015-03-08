package com.pickmeupscotty.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Response;
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
import com.pickmeupscotty.android.login.FBWrapper;
import com.pickmeupscotty.android.maps.GooglePlaces;
import com.pickmeupscotty.android.messages.PickUpRequest;
import com.pickmeupscotty.android.messages.PickUpResponse;

import java.util.concurrent.ExecutionException;

import javax.xml.datatype.Duration;

public class ResponseActivity extends Activity {
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private boolean movedMap;
    private PickUpRequest request;
    private GooglePlaces.Distance distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        request = getIntent().getParcelableExtra(PickUpRequest.PICK_UP_REQUEST);
        TextView nameView = (TextView) findViewById(R.id.textView4);
        nameView.setText(request.getFacebookName());

        try {
            distance = GooglePlaces.durationBetweenAsync(
                    request.getCurrentLatitude(),
                    request.getCurrentLongitude(),
                    request.getDestinationLatitude(),
                    request.getDestinationLongitude()).get();

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
                    .title(request.getFacebookName())
                    .snippet(distance.getDurationText() + " (" + distance.getDistanceText() + ") away")
                    .draggable(true));

            Marker work = mMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .title("Current")
                    .draggable(true));

            LatLngBounds.Builder bounds = new LatLngBounds.Builder();
            bounds.include(destinationPosition);
            bounds.include(currentPosition);
            moveMapToBounds(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void acceptPickUp(View view) {
        FBWrapper.INSTANCE.getUserId(new FBWrapper.UserIdCallback() {
            @Override
            public void onCompleted(String fbid) {
                Log.e("service2", fbid);
                Log.e("service2", request.getFacebookId());
                PickUpResponse response = new PickUpResponse(
                        fbid,
                        request.getFacebookName(),
                        distance.getDurationText());
                RabbitService.send(response, request.getFacebookId());

                Intent intent = new Intent(ResponseActivity.this, DriverActivity.class);
                startActivity(intent);
            }
        });
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
