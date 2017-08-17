package net.suntrans.ebuilding.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/17.
 */

public class AmmeterInfos {
    public int code;
    public String msg;
    @SerializedName("data")
    public Map<String,Object> data;
}
