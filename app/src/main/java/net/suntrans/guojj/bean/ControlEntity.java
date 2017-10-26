package net.suntrans.guojj.bean;

/**
 * Created by Looney on 2017/7/8.
 */

public class ControlEntity extends RespondBody<ControlEntity.Data> {

    public static class Data {
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
