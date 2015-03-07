package com.pickmeupscotty.android;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class RabbitMQTest extends ApplicationTestCase<Application> {
    public RabbitMQTest() {
        super(Application.class);
    }

    public void testME() {
        Log.d("tst", "teststs");
    }
}