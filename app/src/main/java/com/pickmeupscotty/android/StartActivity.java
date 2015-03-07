package com.pickmeupscotty.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.Session;
import com.pickmeupscotty.android.activities.DriverActivity;
import com.pickmeupscotty.android.activities.PickMeUp;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.login.FBWrapper;
import com.pickmeupscotty.android.login.LoginFragment;
import com.pickmeupscotty.android.services.NotificationService;

import java.util.ArrayList;


public class StartActivity extends FragmentActivity {
    private static final String TAG = StartActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        FBWrapper.INSTANCE.addFacebookLoginOpenedListener(new FBWrapper.FacebookLoginStateListener() {
            @Override
            public void onStateChanged() {
                //Forward to MainActivity
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        if (savedInstanceState == null) {
//            //Add the fragment on initial activity setup
//            LoginFragment loginFragment = new LoginFragment();
//
//            if (Session.getActiveSession().isOpened()) {
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .remove(loginFragment)
//                        .commit();
//            }
//        } else {
//            // Or set the fragment from restored state info
//            //loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.login_fragment);
//        }

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
