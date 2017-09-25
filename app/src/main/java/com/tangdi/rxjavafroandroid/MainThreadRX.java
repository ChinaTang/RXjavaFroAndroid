package com.tangdi.rxjavafroandroid;


import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tangdi on 9/23/17.
 */

public class MainThreadRX {

    private static final String TAG = "MainThreadRX";

    private Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> e) throws Exception {
            Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
            e.onNext(2);
            e.onNext(1);
            e.onNext(4);
            e.onComplete();
            e.onNext(5);
        }
    });

    private Observer<Integer> observer = new Observer<Integer>() {

        private Disposable disposable;

        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
            Log.d(TAG, "onSubscribe");
            disposable = d;
        }

        @Override
        public void onNext(Integer value) {
            Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
            Log.d(TAG, "" + value);
            if(value == 1){
                disposable.dispose();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, e.getMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
            Log.d(TAG, "complete");
        }
    };

    private Consumer<Integer> consumer = new Consumer<Integer>() {
        @Override
        public void accept(Integer integer) throws Exception {
            Log.d(TAG, integer + "");
        }
    };

    public MainThreadRX(){
    }

    public void SendOnOneThread(){
        observable.subscribe(observer);
    }

    public void SendOnOtherThread(){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /*===========================Map转换符=================================*/

    public void RXmapChange(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                switch (integer){
                    case 1:
                        return "one";
                    case 2:
                        return "two";
                    case 3:
                        return "three";
                    case 4:
                        return "four";
                    default:
                        return "";
                }
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept===> " + s);
            }
        });
    }

}
