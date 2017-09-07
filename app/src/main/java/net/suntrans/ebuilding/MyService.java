package net.suntrans.ebuilding;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import net.suntrans.ebuilding.rx.RxBus;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Looney on 2017/9/7.
 */

public class MyService extends Service {
    private boolean isShowAlert = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxBus.getInstance().toObserverable(Intent.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Intent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Intent intent) {
                        if (isShowAlert) {
                            isShowAlert = false;
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        RxBus.getInstance().toObserverable(String.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("允许弹框")) {
                            isShowAlert = true;
                        }
                    }
                });
    }
}
