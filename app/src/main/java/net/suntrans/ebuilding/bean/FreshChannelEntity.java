package net.suntrans.ebuilding.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class FreshChannelEntity extends RespondBody<FreshChannelEntity.DataBean>{

    /**
     * code : 200
     * data : {"total":1,"lists":[{"id":11,"name":"用餐区照明10"}]}
     * msg : ok
     */

//    private String code;
//    private DataBean data;
//    private String msg;

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
        /**
         * total : 1
         * lists : [{"id":11,"name":"用餐区照明10"}]
         */

        private int total;
        private List<ListsBean> lists;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }

        public static class ListsBean {
            /**
             * id : 11
             * name : 用餐区照明10
             */

            private int id;
            private String name;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            private boolean isChecked = false;
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
        }
    }
}
