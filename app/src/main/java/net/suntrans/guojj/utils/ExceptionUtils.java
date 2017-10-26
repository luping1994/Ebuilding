package net.suntrans.guojj.utils;

import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Looney on 2017/9/6.
 */

public class ExceptionUtils {
    public static void handleException(Throwable e) {
        if (e instanceof HttpException) {
            if (e.getMessage() != null) {
                if (e.getMessage().equals("HTTP 401 Unauthorized")) {
                    UiUtils.showToast("您的登录状态已失效,请重新登录");
                } else {
                    UiUtils.showToast("服务器错误");
                }
            } else {
                UiUtils.showToast("服务器错误");
            }
        }
        if (e instanceof SocketTimeoutException) {
            UiUtils.showToast("连接超时");
        }
    }
}
