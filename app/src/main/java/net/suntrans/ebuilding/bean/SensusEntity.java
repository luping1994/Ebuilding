package net.suntrans.ebuilding.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import static net.suntrans.ebuilding.R.id.pm25;
import static net.suntrans.ebuilding.R.id.shidu;
import static net.suntrans.ebuilding.R.id.wendu;

/**
 * Created by Looney on 2017/5/20.
 */

public class SensusEntity extends RespondBody<SensusEntity.Data>{
    public int code;
//    public Data data;

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


        public String updated_at;


        public String pm25Eva;
        public String pm1Eva;
        public String pm10Eva;
        public String wenduEva;
        public String shiduEva;
        public String guanzhaoEva;
        public String zEva;
        public String yanwuEva;
        public String jiaquanEva;
        public String daqiYaEva;
        public String xEva;
        public String yEva;

        public int wenduPro;
        public int shiduPro;
        public int daqiyaPro;
        public int guanzhaoPro;
        public int yanwuPro;
        public int jiaquanPro;


        public int pm1Pro;
        public int pm25Pro;
        public int pm10Pro;

        public void setEva() {
            if (getPm25() != null) {

                float pm25f = 0;
                try {
                    pm25f = Float.valueOf(getPm25());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (pm25f <= 35) {
                    this.pm25Eva = "优";
                    pm25Pro = (int) (pm25f / 35 / 6 * 100);
                } else if (pm25f <= 75) {
                    this.pm25Eva = "良";
                    pm25Pro = (int) ((pm25f - 35) / 240 * 100 + 100 / 6);
                } else if (pm25f <= 115) {
                    this.pm25Eva = "轻度污染";
                    pm25Pro = (int) ((pm25f - 75) / 240 * 100 + 200 / 6);
                } else if (pm25f <= 150) {
                    this.pm25Eva = "中度污染";
                    pm25Pro = (int) ((pm25f - 115) / 35 / 6 * 100 + 300 / 6);
                } else if (pm25f <= 250) {
                    this.pm25Eva = "重度污染";
                    pm25Pro = (int) ((pm25f - 150) / 6 + 400 / 6);

                } else {
                    this.pm25Eva = "严重污染";
                    pm1Pro = 90;

                }
            }

            if (getPM1() != null) {
                float pm1F = 0.0f;
                try {
                    pm1F = Float.valueOf(getPM1());
                } catch (NumberFormatException e) {

                }
                if (pm1F <= 35) {
                    this.pm1Eva = "优";
                    pm1Pro = (int) (pm1F / 35 / 6 * 100);

                } else if (pm1F <= 75) {
                    pm1Pro = (int) ((pm1F - 35) / 240 * 100 + 100 / 6);

                    this.pm1Eva = "良";
                } else if (pm1F <= 115) {
                    pm1Pro = (int) ((pm1F - 75) / 240 * 100 + 200 / 6);

                    this.pm1Eva = "轻度污染";
                } else if (pm1F <= 150) {
                    pm1Pro = (int) ((pm1F - 115) / 35 / 6 * 100 + 300 / 6);

                    this.pm1Eva = "中度污染";
                } else if (pm1F <= 250) {
                    pm1Pro = (int) ((pm1F - 150) / 6 + 400 / 6);

                    this.pm1Eva = "重度污染";
                } else {
                    pm1Pro = 90;

                    this.pm1Eva = "严重污染";
                }
            }

            if (getPm10() != null) {
                float pm10F = 0;
                try {
                    pm10F = Float.valueOf(getPm10());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (pm10F <= 50) {
                    this.pm10Eva = "优";
                    pm10Pro = (int) (pm10F / 50 * 100 / 6);

                } else if (pm10F <= 150) {
                    this.pm10Eva = "良";
                    pm10Pro = (int) ((pm10F - 50) / 6 + 100 / 6);

                } else if (pm10F <= 250) {
                    this.pm10Eva = "轻度污染";
                    pm10Pro = (int) ((pm10F - 150) / 6 + 200 / 6);

                } else if (pm10F <= 350) {
                    this.pm10Eva = "中度污染";
                    pm10Pro = (int) ((pm10F - 250) / 6 + 300 / 6);

                } else if (pm10F <= 420) {
                    this.pm10Eva = "重度污染";
                    pm10Pro = (int) ((pm10F - 350) / 420 + 400 / 6);

                } else {
                    this.pm10Eva = "严重污染";
                    pm10Pro = 90;

                }
            }

            if (getYanwu() != null) {
                float aFloat = 0;
                try {
                    aFloat = Float.valueOf(getYanwu());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (aFloat < 750) {
                    yanwuEva = "清洁";
                    yanwuPro = (int) (aFloat / 750 * 100 / 6);
                } else {
                    yanwuEva = "污染";
                    yanwuPro = (int) (100 / 6 + (aFloat - 750) * 500 / 9250 / 6);

                }
            }
            if (getJiaquan() != null) {
                float jiaquanF = 0;
                try {
                    jiaquanF = Float.valueOf(getJiaquan());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (jiaquanF < 0.1) {
                    jiaquanPro = (int) (jiaquanF / 0.1 * 100 / 6);

                    this.jiaquanEva = "清洁";
                } else {
                    this.jiaquanEva = "超标";
                    jiaquanPro = (int) (100 / 6 + (jiaquanF - 0.1) * 500 / 6);
                    if (jiaquanPro >= 80)
                        jiaquanPro = 80;
                }
            }

            if (getWendu() != null) {
                float wenduF = 0;
                try {
                    wenduF = Float.valueOf(getWendu());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (wenduF <= 10) {
                    wenduEva = "极寒";
                    wenduPro = 50 / 6;

                } else if (wenduF <= 15) {
                    wenduEva = "寒冷";
                    wenduPro = (int) ((wenduF - 10) / 5 * 100 / 6 + 100 / 6);

                } else if (wenduF <= 20) {
                    wenduPro = (int) ((wenduF - 15) / 5 * 100 / 6 + 200 / 6);
                    wenduEva = "凉爽";
                } else if (wenduF <= 28) {
                    wenduEva = "舒适";
                    wenduPro = (int) ((wenduF - 20) / 8 * 100 / 6 + 300 / 6);

                } else if (wenduF <= 34) {
                    wenduEva = "闷热";
                    wenduPro = (int) ((wenduF - 28) / 6 * 100 / 6 + 400 / 6);

                } else {
                    wenduEva = "极热";
                    wenduPro = 550 / 6;

                }
            }

            if (getShidu() != null) {
                float shiduF = Float.valueOf(getShidu());
                if (shiduF <= 40) {
                    shiduEva = "干燥";
                    shiduPro = (int) (shiduF / 40.0 * 100 / 3.0);
                } else if (shiduF <= 70) {
                    shiduEva = "舒适";
                    shiduPro = (int) ((shiduF - 40) / 30.0 * 100 / 3.0 + 100 / 3.0);

                } else {
                    shiduEva = "潮湿";
                    shiduPro = (int) ((shiduF - 70) / 30.0 * 100 / 3.0 + 200 / 3.0);

                }
            }
            if (getLight() != null) {
                float guangxianqdF = 0;
                try {
                    guangxianqdF = Float.valueOf(getLight());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (guangxianqdF == 0) {
                    guanzhaoEva = "极弱";
                    guanzhaoPro = 10;
                } else if (guangxianqdF == 1) {
                    guanzhaoEva = "适中";
                    guanzhaoPro = 30;
                } else if (guangxianqdF == 2) {
                    guanzhaoEva = "强";
                    guanzhaoPro = 50;

                } else if (guangxianqdF == 3) {
                    guanzhaoEva = "很强";
                    guanzhaoPro = 70;
                } else {
                    guanzhaoPro = 90;
                    guanzhaoEva = "极强";
                }
            }

            if (getOffset() != null) {
                float qingxie = 0;
                try {
                    qingxie = Float.valueOf(getOffset());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (qingxie > 10) {
                    zEva = "倾斜";
                } else {
                    zEva = "正常";
                }
            }
//            if (x_zhou != null) {
//                float qingxie = 0;
//                try {
//                    qingxie = Float.valueOf(x_zhou);
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//                if (qingxie > 10) {
//                    xEva = "倾斜";
//                } else {
//                    xEva = "正常";
//                }
//            }
//            if (y_zhou != null) {
//                float qingxie = 0;
//                try {
//                    qingxie = Float.valueOf(y_zhou);
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//                if (qingxie > 10) {
//                    yEva = "倾斜";
//                } else {
//                    yEva = "正常";
//                }
//            }

            if (getDaqiya() != null) {
                float daqiyaF = Float.valueOf(getDaqiya());
                if (daqiyaF >= 110) {
                    daqiYaEva = "气压高";
                    daqiyaPro = 80;
                } else if (daqiyaF <= 90) {
                    daqiYaEva = "气压低";
                    daqiyaPro = 20;
                } else {
                    daqiYaEva = "正常";
                    daqiyaPro = 50;
                }
            }


        }


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
