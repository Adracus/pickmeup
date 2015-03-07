package com.pickmeupscotty.android.maps;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by axel on 07/03/15.
 */
public class AsyncPlacesRequest extends AsyncTask<String, Void, List<GooglePlaces.Place>> {
    private static final String LOG_TAG = AsyncPlacesRequest.class.getName();

    @Override
    protected List<GooglePlaces.Place> doInBackground(String... params) {
        return GooglePlaces.placesFor(params[0]);
    }
}
