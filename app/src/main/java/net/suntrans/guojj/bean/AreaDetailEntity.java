package net.suntrans.guojj.bean;

import java.util.List;

/**
 * Created by Looney on 2017/4/24.
 */

public class AreaDetailEntity extends RespondBody<AreaDetailEntity.AreaDetailData>{

    public static class AreaDetailData   {
        public int total;
        public AreaEntity.FloorRoom area;
        public List<DeviceEntity.ChannelInfo> lists;
    }


}
