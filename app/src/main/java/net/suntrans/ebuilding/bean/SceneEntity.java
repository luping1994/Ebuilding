package net.suntrans.ebuilding.bean;

import java.util.List;

/**
 * Created by Looney on 2017/4/24.
 */

public class SceneEntity {
    public int code;
    public Result data;

    public static class Result {
        public int total;
        public List<Scene> lists;
    }

    public static class Scene  {
        public String id;
        public String user_id;
        public String name;
        public String name_en;
        public String show_sort;
        public String img_url;

    }
}
