package net.suntrans.ebuilding.bean;

/**
 * Created by Administrator on 2017/8/15.
 */

public class SampleResult extends RespondBody<String>{

    /**
     * code : 200
     * data : []
     * msg : ok
     */

//    private int code;
//    private String msg;

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
