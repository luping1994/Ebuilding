package net.suntrans.ebuilding;

import android.support.v4.app.Fragment;


import net.suntrans.ebuilding.api.Api;
import net.suntrans.ebuilding.api.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Looney on 2017/11/16.
 * Des:
 */

public class BasedFragment2 extends Fragment {

    protected Api api = RetrofitHelper.getApi();
    protected CompositeSubscription mCompositeSubscription= new CompositeSubscription();


    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }

        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onUnsubscribe();
    }
}
