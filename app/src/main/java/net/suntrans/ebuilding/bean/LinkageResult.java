package net.suntrans.ebuilding.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Looney on 2017/5/20.
 */

public class LinkageResult {
    public int code;
    public Data data;
    public String msg;

    public static class Data {
        public int total;
        public List<SixInfo> lists;
    }

    public static class SixInfo {
        public String id;
        public String user_id;
        public String din;
        public String sn;
        public String name;
        public String place;
        public String product_id;
        public String is_online;
        public String show_sort;
        public SixDetailData sub;
    }


    public static class SixDetailData implements Parcelable {
        private String d_100004451;
        private String d_100004277;
        private String d_100004276;
        private String d_100004275;
        private String d_100004281;
        private String d_100004280;
        private String d_100004278;
        private String d_100004279;
        private String d_100004450;
        private String d_100004274;
        private String d_100004273;



        public void setD_100004451(String d_100004451) {
            this.d_100004451 = d_100004451;
        }


        public void setD_100004277(String d_100004277) {
            this.d_100004277 = d_100004277;
        }


        public void setD_100004276(String d_100004276) {
            this.d_100004276 = d_100004276;
        }



        public void setD_100004275(String d_100004275) {
            this.d_100004275 = d_100004275;
        }
        public void setD_100004281(String d_100004281) {
            this.d_100004281 = d_100004281;
        }
        public void setD_100004280(String d_100004280) {
            this.d_100004280 = d_100004280;
        }

        public void setD_100004278(String d_100004278) {
            this.d_100004278 = d_100004278;
        }

        public void setD_100004279(String d_100004279) {
            this.d_100004279 = d_100004279;
        }

        public void setD_100004450(String d_100004450) {
            this.d_100004450 = d_100004450;
        }

        public void setD_100004274(String d_100004274) {
            this.d_100004274 = d_100004274;
        }


        public void setD_100004273(String d_100004273) {
            this.d_100004273 = d_100004273;
        }







        public String getOffset() {
            return d_100004451;
        }

        public String getPm10() {
            return d_100004277;
        }

        public String getPm25() {
            return d_100004276;
        }



        public String getPM1() {
            return d_100004275;
        }
        public String getRenyuan() {
            return d_100004281;
        }


        public String getYanwu() {
            return d_100004280;
        }


        public String getJiaquan() {
            return d_100004278;
        }


        public String getDaqiya() {
            return d_100004279;
        }



        public String getLight() {
            return d_100004450;
        }



        public String getShidu() {
            return d_100004274;
        }
        public String getWendu() {
            return d_100004273;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.d_100004451);
            dest.writeString(this.d_100004277);
            dest.writeString(this.d_100004276);
            dest.writeString(this.d_100004275);
            dest.writeString(this.d_100004281);
            dest.writeString(this.d_100004280);
            dest.writeString(this.d_100004278);
            dest.writeString(this.d_100004279);
            dest.writeString(this.d_100004450);
            dest.writeString(this.d_100004274);
            dest.writeString(this.d_100004273);
        }

        public SixDetailData() {
        }

        protected SixDetailData(Parcel in) {
            this.d_100004451 = in.readString();
            this.d_100004277 = in.readString();
            this.d_100004276 = in.readString();
            this.d_100004275 = in.readString();
            this.d_100004281 = in.readString();
            this.d_100004280 = in.readString();
            this.d_100004278 = in.readString();
            this.d_100004279 = in.readString();
            this.d_100004450 = in.readString();
            this.d_100004274 = in.readString();
            this.d_100004273 = in.readString();
        }

        public static final Creator<SixDetailData> CREATOR = new Creator<SixDetailData>() {
            @Override
            public SixDetailData createFromParcel(Parcel source) {
                return new SixDetailData(source);
            }

            @Override
            public SixDetailData[] newArray(int size) {
                return new SixDetailData[size];
            }
        };
    }

    /**
     * "d_100004273": "温度",
     * "d_100004274": "湿度",
     * "d_100004275": "PM1",
     * "d_100004276": "PM2.5",
     * "d_100004277": "PM10",
     * "d_100004278": "甲醛",
     * "d_100004279": "大气气压",
     * "d_100004280": "烟雾浓度",
     * "d_100004281": "人员信息",
     * "d_100004450": "光线强度",
     * "d_100004451": "偏移角度",
     * "d_100004458": "遥控学习",
     * "d_100004463": "红外控制 3",
     * "d_100004464": "红外控制 2",
     * "d_100004465": "红外控制 1",
     * "d_100005390": "温度报警开关",
     * "d_100005391": "烟雾报警开关",
     * "d_100005392": "甲醛报警开关",
     * "d_100005393": "PM2.5报警开关",
     * "d_100005394": "振动报警开关",
     * "d_100005395": "有人报警开关"
     */
}
