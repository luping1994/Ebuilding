package net.suntrans.ebuilding.bean;

/**
 * Created by Looney on 2017/9/6.
 */

class ResponeThrowable {
    public static final int HTTP_ERROR = 404;
    public String message;
    public Throwable e;
    private int mErrorCode;

    public ResponeThrowable(Throwable e, int errorcode) {
        this.e = e;
        this.mErrorCode = errorcode;
    }
}
