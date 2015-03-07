package com.pickmeupscotty.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.SessionState;
import com.pickmeupscotty.android.activities.DriverActivity;

import com.pickmeupscotty.android.activities.PickMeUp;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.services.NotificationService;
import com.pickmeupscotty.android.login.FBWrapper;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RabbitService.create("myFacebookID");

        Intent mServiceIntent = new Intent(this, NotificationService.class);
        startService(mServiceIntent);


        FBWrapper.INSTANCE.addFacebookLoginStateListener(new FBWrapper.FacebookLoginStateListener() {
            @Override
            public void onStateChanged(SessionState sessionState) {
                Log.i(TAG, "StateChanged: " + sessionState);
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
        Intent intent = new Intent(this, PickMeUp.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
