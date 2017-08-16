package net.suntrans.ebuilding.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class SceneEdit {

    /**
     * code : 200
     * data : {"row":[{"id":1,"name":"午餐模式","name_en":"Lunch Mode","img_url":"http://tit.suntrans-cloud.com/app/images/scene_1.png","img_banner":"app/images/scene_banner_1.png"}]}
     * msg : ok
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private List<RowBean> row;

        public List<RowBean> getRow() {
            return row;
        }

        public void setRow(List<RowBean> row) {
            this.row = row;
        }

        public static class RowBean {
            /**
             * id : 1
             * name : 午餐模式
             * name_en : Lunch Mode
             * img_url : http://tit.suntrans-cloud.com/app/images/scene_1.png
             * img_banner : app/images/scene_banner_1.png
             */

            private int id;
            private String name;
            private String name_en;
            private String img_url;
            private String img_banner;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName_en() {
                return name_en;
            }

            public void setName_en(String name_en) {
                this.name_en = name_en;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getImg_banner() {
                return img_banner;
            }

            public void setImg_banner(String img_banner) {
                this.img_banner = img_banner;
            }
        }
    }
}
