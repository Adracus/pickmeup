package com.pickmeupscotty.android.amqp;

/**
 * Created by jannis on 07/03/15.
 */
public interface Subscriber<T> {
    public void on(T request);
}
