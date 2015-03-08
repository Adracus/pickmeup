package com.pickmeupscotty.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.messages.PickUpResponse;

import java.util.List;

public class RequestAcceptedActivity extends Activity {
    private PickUpResponse response;

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
        List<LatLng> positions = response.getPositions();
        MapFragment mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.routeMap);
        GoogleMap mMap = mMapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(positions.get(0))
                .draggable(true));
        mMap.addMarker(new MarkerOptions()
                .position(positions.get(1))
                .draggable(true));
        mMap.addMarker(new MarkerOptions()
                .position(positions.get(2))
                .draggable(true));
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
