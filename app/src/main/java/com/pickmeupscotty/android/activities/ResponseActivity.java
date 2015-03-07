package com.pickmeupscotty.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pickmeupscotty.android.R;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.messages.PickUpResponse;

public class ResponseActivity extends Activity {

    private String facebookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        facebookId = getIntent().getStringExtra("FACEBOOK_ID");

        TextView nameView = (TextView) findViewById(R.id.textView4);
        nameView.setText(facebookId);
    }

    public void acceptPickUp(View view) {
        RabbitService.send(new PickUpResponse(), facebookId);
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
