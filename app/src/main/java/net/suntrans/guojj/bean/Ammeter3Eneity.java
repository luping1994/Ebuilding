package net.suntrans.guojj.bean;

import java.util.List;

/**
 * Created by Looney on 2017/7/13.
 */

public class Ammeter3Eneity extends RespondBody<Ammeter3Eneity.Data>{

    public  static class Data{
        public int id;
        public String sno;
        public String name;
        public String today;
        public String yesterday;
        public List<HisItem> eletricity_totay;
    }

    public static class HisItem{
        public String x;
        public String y;
    }
}
