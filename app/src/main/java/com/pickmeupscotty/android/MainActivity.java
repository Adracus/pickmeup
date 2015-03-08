package com.pickmeupscotty.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.pickmeupscotty.android.activities.DriverActivity;

import com.pickmeupscotty.android.activities.PickMeUpActivity;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.services.NotificationService;
import com.pickmeupscotty.android.login.FBWrapper;


public class MainActivity extends FragmentActivity {
    private static final String TAG = MainActivity.class.getName();

//    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Button drivingButton = (Button) findViewById(R.id.drivingButton);
        final Button pickMeUpButton = (Button) findViewById(R.id.pickMeUpButton);

        RabbitService.create("facebookid");
        FBWrapper.INSTANCE.addFacebookLoginOpenedListener(new FBWrapper.FacebookLoginStateListener() {
            @Override
            public void onStateChanged() {
                FBWrapper.INSTANCE.getUserId(new FBWrapper.UserIdCallback() {
                                                 @Override
                                                 public void onCompleted(String fbid) {
                                                     Log.e("test", fbid);
                                                     RabbitService.create(fbid);
                                                 }
                   }

                );
            }
        });

        Intent mServiceIntent = new Intent(this, NotificationService.class);
        startService(mServiceIntent);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
