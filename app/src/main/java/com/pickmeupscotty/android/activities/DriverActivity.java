package com.pickmeupscotty.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pickmeupscotty.android.MainActivity;
import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.amqp.Subscriber;
import com.pickmeupscotty.android.messages.PickUpRequest;

public class DriverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
//        RabbitService.subscribe(PickUpRequest.class, new Subscriber<PickUpRequest>() {
//
//            @Override
//            public void on(PickUpRequest request) {
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(getApplicationContext(), request.toString(), duration);
//                toast.show();
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver, menu);
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
