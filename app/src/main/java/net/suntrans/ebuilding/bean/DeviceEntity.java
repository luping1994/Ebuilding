package net.suntrans.ebuilding.bean;

import java.util.List;

/**
 * Created by Looney on 2017/6/10.
 */

public class DeviceEntity {

    public int code;
    public Data data;
    public String msg;
    public class Data {
        public int total;
        public List<ChannelInfo> lists;
    }

    public static class ChannelInfo {
        public String id;
        public String dev_id;
        public String datapoint;
        public String name;
        public String data_type;
        public String place;
        public String status;
        public String show_sort;
        public String din;
        public String channel_type;
        public String area_name;

    }
}
