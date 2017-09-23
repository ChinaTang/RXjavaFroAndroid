package com.tangdi.rxjavafroandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tangdi.rxjavafroandroid.network.LogingRequest;
import com.tangdi.rxjavafroandroid.network.LogingResult;
import com.tangdi.rxjavafroandroid.network.NetApi;
import com.tangdi.rxjavafroandroid.network.NetWorkFactory;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private MainThreadRX mainThreadRX;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainThreadRX = new MainThreadRX();
        mainThreadRX.SendOnOtherThread();

        NetWorkFactory.createRetrofit().create(NetApi.class).Login(new LogingRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LogingResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LogingResult value) {
                        Log.d(TAG, value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Login error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Login success");
                    }
                });
    }
}
