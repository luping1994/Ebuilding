package net.suntrans.guojj.bean;

/**
 * Created by Administrator on 2017/8/15.
 */

public class SampleResult extends RespondBody{

    /**
     * code : 200
     * data : []
     * msg : ok
     */

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
