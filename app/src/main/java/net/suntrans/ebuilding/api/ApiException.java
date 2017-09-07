package net.suntrans.ebuilding.api;

/**
 * Created by Looney on 2017/9/6.
 */

public class ApiException extends RuntimeException{
    public int code;
    public String msg;
    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
