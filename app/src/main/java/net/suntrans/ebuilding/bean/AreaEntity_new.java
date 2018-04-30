package net.suntrans.ebuilding.bean;


import java.util.List;

/**
 * Created by Looney on 2017/7/8.
 */

public class AreaEntity_new extends RespondBody<List<AreaEntity_new.AreaFloor>>{



    public static class AreaFloor{
        public int id;
        public int area_id;
        public String user_id;
        public String name;
        public String img_url;
        public String show_sort;
        public List<FloorRoom> sub;
        public List<FloorRoom> rooms;
        public List<AreaEntity_new.AreaFloor> lists;
        public boolean isCheck = false;
    }

    public static class FloorRoom{
        public int id;
        public String house_id;
        public String house_number;
        public String name;
        public String img_url;
        public String show_sort;

        public List<Channel> lists;
        public boolean isChecked = false;

        //能耗电表列表要用的字段
        public String yesterday;
        public String today;
        public String allPower;

        //能耗首页列表字段
        public String electricity;
        public String sno; //电表表号



        //能耗监测要用的字段~
        public String power;
        public String current;

        //环境要用的字段
        public String pm25;
        public String jiaquan;
        public String guanzhao;
        public String shidu;
        public String wendu;
        public String yanwu;
        public String zhendong;
        public String isOnline;


        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }

    public static class Channel{
        public String area_id;
        public String channel_id;
        public String id;
        public String name;
        public String title;
        public String permission = "1";


        /**
         * id : 1054
         * name : 通道2
         * number : 2
         * status : 0
         * device_type : 1
         * used : 1
         */
        public String datapoint_name;
        public String number;
        public int status;
        public int device_type;
        public String used;



        /**
         * dev_id : 445
         * house_id : 5
         * max_i : 10
         * classification : 插座
         * number : 1
         * show_sort : 1
         * used : 1
         * channel_type : 2
         * device_group : 0
         * updated_at : null
         * deleted_at : null
         * user_id : 5057
         * created_at : 2018-03-22 14:33:52
         * house_type : 0
         */

        public String dev_id;
        public String house_id;
        public String max_i;
        public String classification;
        public String show_sort;
        public int channel_type;
        public String device_group;
        public String updated_at;
        public String deleted_at;
        public String user_id;
        public String created_at;
        public String house_type;


        /**
         * dev_id : 471
         * house_id : null
         * max_i : 20
         * classification : null
         * number : 1
         * show_sort : 0
         * used : 1
         * channel_type : 2
         * device_group : 0
         * always_open : 0
         * din :
         */


        public int always_open;
        public String din;



        public boolean isChecked = false;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
