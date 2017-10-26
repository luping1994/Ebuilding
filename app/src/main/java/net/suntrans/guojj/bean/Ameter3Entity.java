package net.suntrans.guojj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */

public class Ameter3Entity extends RespondBody<Ameter3Entity.DataBean>{

    /**
     * code : 200
     * data : {"sno":"222170077877","eletricity_day":[{"y":"0.00","x":0},{"y":"0.00","x":1},{"y":"0.80","x":2},{"y":"0.00","x":3},{"y":"0.00","x":4},{"y":"0.80","x":5},{"y":"0.00","x":6},{"y":"0.80","x":7},{"y":"0.00","x":8},{"y":"0.00","x":9},{"y":"0.80","x":10},{"y":"0.00","x":11},{"y":"0.00","x":12},{"y":"0.80","x":13},{"y":"0.00","x":14},{"y":"0.00","x":15},{"y":"0.80","x":16},{"y":"0.00","x":17},{"y":"0.00","x":18},{"y":"0.00","x":19},{"y":"0.00","x":20},{"y":"0.00","x":21},{"y":"0.00","x":22},{"y":"0.00","x":23}],"eletricity_month":[{"y":"2.40","x":3},{"y":"2.00","x":4},{"y":"0.00","x":14},{"y":"2.40","x":15},{"y":"4.80","x":16},{"y":"0.00","x":17}],"eletricity_year":[{"y":"11.60","x":8}]}
     * msg : ok
     */


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
         * sno : 222170077877
         * eletricity_day : [{"y":"0.00","x":0},{"y":"0.00","x":1},{"y":"0.80","x":2},{"y":"0.00","x":3},{"y":"0.00","x":4},{"y":"0.80","x":5},{"y":"0.00","x":6},{"y":"0.80","x":7},{"y":"0.00","x":8},{"y":"0.00","x":9},{"y":"0.80","x":10},{"y":"0.00","x":11},{"y":"0.00","x":12},{"y":"0.80","x":13},{"y":"0.00","x":14},{"y":"0.00","x":15},{"y":"0.80","x":16},{"y":"0.00","x":17},{"y":"0.00","x":18},{"y":"0.00","x":19},{"y":"0.00","x":20},{"y":"0.00","x":21},{"y":"0.00","x":22},{"y":"0.00","x":23}]
         * eletricity_month : [{"y":"2.40","x":3},{"y":"2.00","x":4},{"y":"0.00","x":14},{"y":"2.40","x":15},{"y":"4.80","x":16},{"y":"0.00","x":17}]
         * eletricity_year : [{"y":"11.60","x":8}]
         */

        private String sno;
        private List<EletricityDayBean> eletricity_day;
        private List<EletricityMonthBean> eletricity_month;
        private List<EletricityYearBean> eletricity_year;

        public String getSno() {
            return sno;
        }

        public void setSno(String sno) {
            this.sno = sno;
        }

        public List<EletricityDayBean> getEletricity_day() {
            return eletricity_day;
        }

        public void setEletricity_day(List<EletricityDayBean> eletricity_day) {
            this.eletricity_day = eletricity_day;
        }

        public List<EletricityMonthBean> getEletricity_month() {
            return eletricity_month;
        }

        public void setEletricity_month(List<EletricityMonthBean> eletricity_month) {
            this.eletricity_month = eletricity_month;
        }

        public List<EletricityYearBean> getEletricity_year() {
            return eletricity_year;
        }

        public void setEletricity_year(List<EletricityYearBean> eletricity_year) {
            this.eletricity_year = eletricity_year;
        }

        public static class EletricityDayBean {
            /**
             * y : 0.00
             * x : 0
             */
            private String y;
            private int x;

            public String getY() {
                return y;
            }

            public void setY(String y) {
                this.y = y;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }
        }

        public static class EletricityMonthBean {
            /**
             * y : 2.40
             * x : 3
             */
            private String y;
            private int x;

            public String getY() {
                return y;
            }

            public void setY(String y) {
                this.y = y;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }
        }

        public static class EletricityYearBean {
            /**
             * y : 11.60
             * x : 8
             */
            private String y;
            private int x;

            public String getY() {
                return y;
            }

            public void setY(String y) {
                this.y = y;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }
        }
    }
}
