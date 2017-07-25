package net.suntrans.ebuilding.bean;

/**
 * Created by Looney on 2017/7/8.
 */

public class ControlEntity {
    public int code;
    public Data data;
    public String msg;
    public static class Data{
        public String id;
        public int status;

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", status=" + status +
                    '}';
        }
    }
}
