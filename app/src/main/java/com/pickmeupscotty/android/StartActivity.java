package com.pickmeupscotty.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pickmeupscotty.android.activities.DriverActivity;
import com.pickmeupscotty.android.activities.PickMeUpActivity;
import com.pickmeupscotty.android.login.FBWrapper;


public class StartActivity extends FragmentActivity {
    private static final String TAG = StartActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        registerFacebookStateChangeListener();
    }

    private void registerFacebookStateChangeListener() {
        FBWrapper.INSTANCE.addFacebookLoginOpenedListener(new FBWrapper.FacebookLoginStateListener() {
            @Override
            public void onStateChanged() {
                //Forward to MainActivity
                FBWrapper.INSTANCE.getUserId(new FBWrapper.UserIdCallback() {
                    @Override
                    public void onCompleted(String fbid) {
                        Log.i("FBID", fbid);
                    }
                });
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void startDriverActivity(View view) {
        Intent intent = new Intent(this, DriverActivity.class);
        startActivity(intent);
    }

    public void startPickMeUpActivity(View view) {
        Intent intent = new Intent(this, PickMeUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerFacebookStateChangeListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
