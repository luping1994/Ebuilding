package net.suntrans.ebuilding.bean;

import java.util.List;

/**
 * Created by Looney on 2017/6/16.
 */

public class SceneChannelResult {
    public int code;
    public Data data;
    public String msg;
    public static class Data {
        public int total;
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
    }
}
