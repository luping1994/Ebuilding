package net.suntrans.ebuilding.bean;

import rx.functions.Func1;
import rx.functions.Function;

/**
 * Created by Looney on 2017/9/6.
 */

public class HandleFuc<T> implements Func1<RespondBody<T>, T> {
    @Override
    public T call(RespondBody<T> tRespondBody) {
        if (tRespondBody.code != 200) {
            throw new RuntimeException(tRespondBody.code + "" + tRespondBody.msg != null ? tRespondBody.msg : "");
        }
        return tRespondBody.data;
    }
}
