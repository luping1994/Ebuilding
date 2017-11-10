package net.suntrans.guojj.bean;

/**
 * Created by Looney on 2017/11/8.
 * Des:
 */

public class ChannelEditorInfo extends RespondBody<ChannelEditorInfo.DataBean>{

    /**
     * code : 200
     * data : {"name":"演示通道1","channel_type":"1","id":64}
     * msg : ok
     */


    public static class DataBean {
        /**
         * name : 演示通道1
         * channel_type : 1
         * id : 64
         */

        public String name;
        public String channel_type;
        public String id;
    }
}
