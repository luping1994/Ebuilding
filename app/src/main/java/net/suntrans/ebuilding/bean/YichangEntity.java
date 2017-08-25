package net.suntrans.ebuilding.bean;

import java.util.List;

/**
 * Created by Looney on 2017/8/17.
 */

public class YichangEntity {

    /**
     * code : 200
     * data : {"lists":{"current_page":1,"data":[{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:45","log_id":5679,"message":"设备重连","name":"1楼大厅第六感","device_id":79},{"updated_at":"2017-08-25 08:37:05","created_at":"2017-08-25 08:08:46","log_id":5680,"message":"湿度过高","name":"1楼大厅第六感","device_id":79},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:57","log_id":5681,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:57","log_id":5682,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:58","log_id":5683,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:28:12","created_at":"2017-08-25 08:08:58","log_id":5684,"message":"湿度过高","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 14:52:10","created_at":"2017-08-25 08:09:30","log_id":5685,"message":"建筑危险","name":"2楼大厅第六感","device_id":84},{"updated_at":"2017-08-25 08:45:46","created_at":"2017-08-25 08:13:26","log_id":5687,"message":"甲醛超标","name":"1楼大厅第六感","device_id":79},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:13:31","log_id":5688,"message":"甲醛超标","name":"2楼餐厅第六感","device_id":80},{"updated_at":"2017-08-25 09:27:12","created_at":"2017-08-25 08:13:32","log_id":5689,"message":"甲醛超标","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:54","log_id":5690,"message":"设备重连","name":"2楼六通道","device_id":6},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:54","log_id":5691,"message":"设备重连","name":"1楼六通道","device_id":5},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:56","log_id":5692,"message":"设备重连","name":"二楼十通道1-1","device_id":3},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:58","log_id":5693,"message":"设备重连","name":"一楼十通道2","device_id":2},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:25:00","log_id":5694,"message":"设备重连","name":"一楼十通道1","device_id":1},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:25:02","log_id":5695,"message":"设备重连","name":"二楼十通道2","device_id":4},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:25:02","log_id":5696,"message":"设备重连","name":"二楼十通道2","device_id":4},{"updated_at":"2017-08-25 16:16:17","created_at":"2017-08-25 08:25:13","log_id":5697,"message":"开关电压过大","name":"二楼十通道2","device_id":4},{"updated_at":"2017-08-25 08:26:08","created_at":"2017-08-25 08:25:53","log_id":5698,"message":"设备重连","name":"RTU-8-1","device_id":86},{"updated_at":"2017-08-25 08:30:58","created_at":"2017-08-25 08:30:01","log_id":5699,"message":"设备离线","name":"1楼餐厅第六感","device_id":77}],"from":1,"last_page":3,"next_page_url":"http://adminiot.suntrans-cloud.com/api/v1/device/abnormal?page=2","path":"http://adminiot.suntrans-cloud.com/api/v1/device/abnormal","per_page":20,"prev_page_url":null,"to":20,"total":51}}
     * msg : ok
     */

    public int code;
    public DataBeanX data;
    public String msg;

    public static class DataBeanX {
        /**
         * lists : {"current_page":1,"data":[{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:45","log_id":5679,"message":"设备重连","name":"1楼大厅第六感","device_id":79},{"updated_at":"2017-08-25 08:37:05","created_at":"2017-08-25 08:08:46","log_id":5680,"message":"湿度过高","name":"1楼大厅第六感","device_id":79},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:57","log_id":5681,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:57","log_id":5682,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:58","log_id":5683,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:28:12","created_at":"2017-08-25 08:08:58","log_id":5684,"message":"湿度过高","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 14:52:10","created_at":"2017-08-25 08:09:30","log_id":5685,"message":"建筑危险","name":"2楼大厅第六感","device_id":84},{"updated_at":"2017-08-25 08:45:46","created_at":"2017-08-25 08:13:26","log_id":5687,"message":"甲醛超标","name":"1楼大厅第六感","device_id":79},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:13:31","log_id":5688,"message":"甲醛超标","name":"2楼餐厅第六感","device_id":80},{"updated_at":"2017-08-25 09:27:12","created_at":"2017-08-25 08:13:32","log_id":5689,"message":"甲醛超标","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:54","log_id":5690,"message":"设备重连","name":"2楼六通道","device_id":6},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:54","log_id":5691,"message":"设备重连","name":"1楼六通道","device_id":5},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:56","log_id":5692,"message":"设备重连","name":"二楼十通道1-1","device_id":3},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:58","log_id":5693,"message":"设备重连","name":"一楼十通道2","device_id":2},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:25:00","log_id":5694,"message":"设备重连","name":"一楼十通道1","device_id":1},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:25:02","log_id":5695,"message":"设备重连","name":"二楼十通道2","device_id":4},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:25:02","log_id":5696,"message":"设备重连","name":"二楼十通道2","device_id":4},{"updated_at":"2017-08-25 16:16:17","created_at":"2017-08-25 08:25:13","log_id":5697,"message":"开关电压过大","name":"二楼十通道2","device_id":4},{"updated_at":"2017-08-25 08:26:08","created_at":"2017-08-25 08:25:53","log_id":5698,"message":"设备重连","name":"RTU-8-1","device_id":86},{"updated_at":"2017-08-25 08:30:58","created_at":"2017-08-25 08:30:01","log_id":5699,"message":"设备离线","name":"1楼餐厅第六感","device_id":77}],"from":1,"last_page":3,"next_page_url":"http://adminiot.suntrans-cloud.com/api/v1/device/abnormal?page=2","path":"http://adminiot.suntrans-cloud.com/api/v1/device/abnormal","per_page":20,"prev_page_url":null,"to":20,"total":51}
         */

        public ListsBean lists;

        public static class ListsBean {
            /**
             * current_page : 1
             * data : [{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:45","log_id":5679,"message":"设备重连","name":"1楼大厅第六感","device_id":79},{"updated_at":"2017-08-25 08:37:05","created_at":"2017-08-25 08:08:46","log_id":5680,"message":"湿度过高","name":"1楼大厅第六感","device_id":79},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:57","log_id":5681,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:57","log_id":5682,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:08:58","log_id":5683,"message":"设备重连","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:28:12","created_at":"2017-08-25 08:08:58","log_id":5684,"message":"湿度过高","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 14:52:10","created_at":"2017-08-25 08:09:30","log_id":5685,"message":"建筑危险","name":"2楼大厅第六感","device_id":84},{"updated_at":"2017-08-25 08:45:46","created_at":"2017-08-25 08:13:26","log_id":5687,"message":"甲醛超标","name":"1楼大厅第六感","device_id":79},{"updated_at":"2017-08-25 08:21:24","created_at":"2017-08-25 08:13:31","log_id":5688,"message":"甲醛超标","name":"2楼餐厅第六感","device_id":80},{"updated_at":"2017-08-25 09:27:12","created_at":"2017-08-25 08:13:32","log_id":5689,"message":"甲醛超标","name":"1楼餐厅第六感","device_id":77},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:54","log_id":5690,"message":"设备重连","name":"2楼六通道","device_id":6},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:54","log_id":5691,"message":"设备重连","name":"1楼六通道","device_id":5},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:56","log_id":5692,"message":"设备重连","name":"二楼十通道1-1","device_id":3},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:24:58","log_id":5693,"message":"设备重连","name":"一楼十通道2","device_id":2},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:25:00","log_id":5694,"message":"设备重连","name":"一楼十通道1","device_id":1},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:25:02","log_id":5695,"message":"设备重连","name":"二楼十通道2","device_id":4},{"updated_at":"2017-08-25 08:25:10","created_at":"2017-08-25 08:25:02","log_id":5696,"message":"设备重连","name":"二楼十通道2","device_id":4},{"updated_at":"2017-08-25 16:16:17","created_at":"2017-08-25 08:25:13","log_id":5697,"message":"开关电压过大","name":"二楼十通道2","device_id":4},{"updated_at":"2017-08-25 08:26:08","created_at":"2017-08-25 08:25:53","log_id":5698,"message":"设备重连","name":"RTU-8-1","device_id":86},{"updated_at":"2017-08-25 08:30:58","created_at":"2017-08-25 08:30:01","log_id":5699,"message":"设备离线","name":"1楼餐厅第六感","device_id":77}]
             * from : 1
             * last_page : 3
             * next_page_url : http://adminiot.suntrans-cloud.com/api/v1/device/abnormal?page=2
             * path : http://adminiot.suntrans-cloud.com/api/v1/device/abnormal
             * per_page : 20
             * prev_page_url : null
             * to : 20
             * total : 51
             */

            public int current_page;
            public int from;
            public int last_page;
            public String next_page_url;
            public String path;
            public int per_page;
            public String prev_page_url;
            public int to;
            public int total;
            public List<DataBean> data;

            public static class DataBean {
                /**
                 * updated_at : 2017-08-25 08:21:24
                 * created_at : 2017-08-25 08:08:45
                 * log_id : 5679
                 * message : 设备重连
                 * name : 1楼大厅第六感
                 * device_id : 79
                 */

                public String updated_at;
                public String created_at;
                public int log_id;
                public String message;
                public String name;
                public int device_id;
            }
        }
    }
}
