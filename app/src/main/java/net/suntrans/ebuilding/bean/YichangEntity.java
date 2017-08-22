package net.suntrans.ebuilding.bean;

import java.util.List;

/**
 * Created by Looney on 2017/8/17.
 */

public class YichangEntity {
    /**
     * code : 200
     * data : {"total":8,"lists":[{"created_at":"2017-08-18 11:46:34","log_id":1,"message":"设备重连","name":"二楼十通道1-1","device_id":3},{"created_at":"2017-08-18 11:46:34","log_id":2,"message":"设备重连","name":"二楼十通道2","device_id":4},{"created_at":"2017-08-18 11:46:34","log_id":3,"message":"设备重连","name":"一楼十通道1","device_id":1},{"created_at":"2017-08-18 11:46:34","log_id":4,"message":"设备重连","name":"2楼餐厅第六感","device_id":80},{"created_at":"2017-08-18 11:46:34","log_id":5,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"created_at":"2017-08-18 11:47:23","log_id":6,"message":"设备离线","name":"一楼十通道2","device_id":2},{"created_at":"2017-08-18 11:47:23","log_id":7,"message":"设备离线","name":"1楼六通道","device_id":5},{"created_at":"2017-08-18 11:47:23","log_id":8,"message":"设备离线","name":"2楼六通道","device_id":6}]}
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
        /**
         * total : 8
         * lists : [{"created_at":"2017-08-18 11:46:34","log_id":1,"message":"设备重连","name":"二楼十通道1-1","device_id":3},{"created_at":"2017-08-18 11:46:34","log_id":2,"message":"设备重连","name":"二楼十通道2","device_id":4},{"created_at":"2017-08-18 11:46:34","log_id":3,"message":"设备重连","name":"一楼十通道1","device_id":1},{"created_at":"2017-08-18 11:46:34","log_id":4,"message":"设备重连","name":"2楼餐厅第六感","device_id":80},{"created_at":"2017-08-18 11:46:34","log_id":5,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"created_at":"2017-08-18 11:47:23","log_id":6,"message":"设备离线","name":"一楼十通道2","device_id":2},{"created_at":"2017-08-18 11:47:23","log_id":7,"message":"设备离线","name":"1楼六通道","device_id":5},{"created_at":"2017-08-18 11:47:23","log_id":8,"message":"设备离线","name":"2楼六通道","device_id":6}]
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
             * created_at : 2017-08-18 11:46:34
             * log_id : 1
             * message : 设备重连
             * name : 二楼十通道1-1
             * device_id : 3
             */

            private String created_at;
            private String updated_at;

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            private int log_id;
            private String message;
            private String name;
            private int device_id;

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public int getLog_id() {
                return log_id;
            }

            public void setLog_id(int log_id) {
                this.log_id = log_id;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getDevice_id() {
                return device_id;
            }

            public void setDevice_id(int device_id) {
                this.device_id = device_id;
            }
        }
    }
}
