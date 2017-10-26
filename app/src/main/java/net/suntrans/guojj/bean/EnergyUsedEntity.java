package net.suntrans.guojj.bean;

import java.util.List;

/**
 * Created by Looney on 2017/8/1.
 */

public class EnergyUsedEntity extends RespondBody<EnergyUsedEntity.Data>{


    public class Data {
        public int total;
        public List<EnergyUsedData> lists;
    }

    public static class EnergyUsedData {
        public String x;
        public String y;

    }
}
