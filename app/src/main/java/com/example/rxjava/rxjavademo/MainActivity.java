package com.example.rxjava.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] arr = {"123","abc"};
        Observable.just(arr)
                .filter(new Func1<String[], Boolean>() {
                    @Override
                    public Boolean call(String[] strings) {
                        return strings.length==2;
                    }
                })
                .flatMap(a -> Observable.from(a))
                .subscribe(url -> System.out.println("==="+url));

        Observable.just("Hello, world!")
                .map(s -> s.hashCode())
                .map(i -> Integer.toString(i))
                .subscribe(s -> System.out.println(s));


        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e(TAG, "OnSubscribe-call-TID:" + Thread.currentThread().getId());

                subscriber.onNext("1111");
            }

        })
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(new Action0() {
            @Override
            public void call() {
                Toast.makeText(MainActivity.this, "xinacheng", Toast.LENGTH_SHORT).show(); // 需要在主线程执行
            }
        })
        .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "Subscriber-onNext-TID:" + Thread.currentThread().getId());
            }

            @Override
            public void onStart() {
                super.onStart();
                Log.e(TAG,"onStart--tid:"+Thread.currentThread().getId());
            }
        });


        String[] names = {"1", "2", "3"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        Log.e(TAG, name);
                    }
                });


    }
}
