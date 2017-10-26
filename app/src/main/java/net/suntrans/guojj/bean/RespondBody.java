package net.suntrans.guojj.bean;

/**
 * Created by Looney on 2017/9/6.
 */

public  class RespondBody <T>{
    public int code;
    public T data;
    public String msg;

    public boolean isOk(){
        return code!=401;
    }

}
