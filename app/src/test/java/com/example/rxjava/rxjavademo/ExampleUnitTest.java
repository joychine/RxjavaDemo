package com.example.rxjava.rxjavademo;

import android.util.Log;

import org.junit.Test;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.observables.AsyncOnSubscribe;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private String TAG = getClass().getSimpleName();
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testCreate(){
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e(TAG,"onSubsrible...");

            }
        });
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        });

    }
}