package net.suntrans.ebuilding.bean;

import java.util.List;

/**
 * Created by Looney on 2017/4/24.
 */

public class AreaDetailEntity {
    public int code;
    public String msg;
    public AreaDetailData data;


    public static class AreaDetailData   {
        public int total;
        public AreaEntity.FloorRoom area;
        public List<DeviceEntity.ChannelInfo> lists;
    }
}
