package net.suntrans.ebuilding.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */

public class AddSceneChannelResult{

    /**
     * code : 200
     * data : []
     * msg : ok
     */

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getMsg() {
        return msg;
    }

    public void setMsg(List<String> msg) {
        this.msg = msg;
    }

    private List<String> msg;

}
