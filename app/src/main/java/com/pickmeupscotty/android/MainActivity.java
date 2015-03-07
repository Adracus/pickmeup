package com.pickmeupscotty.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pickmeupscotty.android.activities.DriverActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.pickmeupscotty.android.amqp.MessageConsumer;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.amqp.Request;
import com.pickmeupscotty.android.amqp.Subscriber;



public class MainActivity extends Activity {

    private MessageConsumer mConsumer;
    private TextView mOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RabbitService.create(getApplicationContext());


        RabbitService.subscribe(PickUpRequest.class, new Subscriber<PickUpRequest>() {

            @Override
            public void on(PickUpRequest request) {
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), request.name, duration);
                toast.show();
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
        RabbitService.send(new PickUpRequest());
    }

    @Override
    protected void onResume() {
        super.onPause();
//        mConsumer.connectToRabbitMQ();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mConsumer.dispose();
    }

    public static class PickUpRequest implements Request{
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String name = "test";

        public PickUpRequest() {

        }
    }
}
