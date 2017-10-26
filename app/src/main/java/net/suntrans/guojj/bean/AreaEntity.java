package net.suntrans.guojj.bean;

import java.util.List;

/**
 * Created by Looney on 2017/7/8.
 */

public class AreaEntity extends RespondBody<AreaEntity.AreaData>{

    public static class AreaData{
        public int total;
        public List<AreaFloor> lists;

    }

    public static class AreaFloor{
        public int id;
        public String user_id;
        public String name;
        public String img_url;
        public String show_sort;
        public List<FloorRoom> sub;
        public boolean isCheck = false;
    }

    public static class FloorRoom{
        public int id;
        public String house_id;
        public String name;
        public String img_url;
        public String show_sort;
        public List<Channel> lists;
        public boolean isChecked = false;

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
        public String permission;
        public boolean isChecked = false;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
