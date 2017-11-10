package net.suntrans.guojj.bean;

import java.util.List;

/**
 * Created by Looney on 2017/11/8.
 * Des:
 */

public class DeviceDetailResult extends RespondBody<List<DeviceDetailResult.DataInfo>> {


    public static class DataInfo {
        public String id;
        public String dev_id;
        public String datapoint;
        public String datapoint_name;
        public String name;
        public String channel_type;
        public String place;
        public String show_sort;

    }
}
