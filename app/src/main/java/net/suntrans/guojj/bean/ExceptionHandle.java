package net.suntrans.guojj.bean;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Looney on 2017/9/6.
 */

public class ExceptionHandle {
    //该方法用来判断异常类型 并将异常类型封装在ResponeThrowable返回
    public static ResponeThrowable handleException(Throwable e) {
        ResponeThrowable ex=null;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponeThrowable(e, ResponeThrowable.HTTP_ERROR);
            ex.message = "网络错误";
            return ex;
        }

        return ex;

    }


}
