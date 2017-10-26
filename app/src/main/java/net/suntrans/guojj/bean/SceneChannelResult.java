package net.suntrans.guojj.bean;

import java.util.List;

/**
 * Created by Looney on 2017/6/16.
 */

public class SceneChannelResult extends RespondBody<SceneChannelResult.Data>{

    public static class Data {
        public int total;
        public String img_banner;
        public List<SceneChannel> lists;
    }

    public static class SceneChannel {
        public String id;
        public String scene_id;
        public String din;
        public String datapoint;
        public String cmd;
        public String name;
        public String status;
        public String area_name;
    }
}
