package net.suntrans.guojj.bean;

import java.util.List;

/**
 * Created by Looney on 2017/4/21.
 */

public class DeviceInfoResult extends RespondBody<DeviceInfoResult.Data> {

    public static class Data {
        public int total;
        public List<DeviceInfo> lists;
    }

    public static class DeviceInfo {
        public String id;
        public String device_name;
        public String din;
        public String sn;
        public String is_online;
        public String show_sort;
        public String product_id;
        public String product_name;
        public String product_img;
    }

    public boolean isEmpty() {
        if (data.lists == null) {
            return true;
        }
        return data.lists.size() == 0 ? true : false;
    }
}
