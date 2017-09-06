package net.suntrans.ebuilding.bean;

import java.util.List;

/**
 * Created by Looney on 2017/5/20.
 */

public class EnergyEntity extends RespondBody<EnergyEntity.Data>{

    public static class Data {
        public int total;
        public List<EnergyData> lists;
    }

    public static class EnergyData {
        public int id;
        public String sno;
        public String name;
        public String yesterday;
        public String today;
        public AmmeterInfo ammeter3;

    }

    public static class AmmeterInfo {
        public int id;
        public String updated_at;
        public String din;
        public String sno;
        public String VolA;
        public String VolB;
        public String VolC;
        public String IA;
        public String IB;
        public String IC;
        public String ActivePower;
        public String ActivePowerA;
        public String ActivePowerB;
        public String ActivePowerC;
        public String ReactivePowerA;
        public String ReactivePowerB;
        public String ReactivePowerC;
        public String PowerFactor;
        public String PowerFactorA;
        public String PowerFactorB;
        public String PowerFactorC;
        public String EletricityValue;

    }
}
