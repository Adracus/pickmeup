package com.pickmeupscotty.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pickmeupscotty.android.activities.DriverActivity;
import com.pickmeupscotty.android.activities.PickMeUpActivity;
import com.pickmeupscotty.android.amqp.RabbitService;
import com.pickmeupscotty.android.login.FBWrapper;
import com.pickmeupscotty.android.services.NotificationService;


public class MainActivity extends FragmentActivity {
    private static final String TAG = MainActivity.class.getName();

    //    private LoginFragment loginFragment;
    private long backTriggeredAt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

                FBWrapper.INSTANCE.getUserId(new FBWrapper.UserIdCallback() {
                                                 @Override
                                                 public void onCompleted(String fbid) {
                                                     RabbitService.create(fbid);
                                                 }
                                             }
                );

        Intent mServiceIntent = new Intent(this, NotificationService.class);
        startService(mServiceIntent);
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

    @Override
    public void onBackPressed() {
        if (backTriggeredAt + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);

            startActivity(intent);
            return;
        }

        backTriggeredAt = System.currentTimeMillis();
        Toast.makeText(this, "Press BACK once more to exit", Toast.LENGTH_SHORT).show();
    }

}
