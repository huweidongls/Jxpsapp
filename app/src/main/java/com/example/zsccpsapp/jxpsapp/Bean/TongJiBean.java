package com.example.zsccpsapp.jxpsapp.Bean;

/**
 * Created by 99zan on 2018/2/7.
 */

public class TongJiBean {

    /**
     * error : false
     * msg : {"weekshuliang":0,"weektccost":0,"monthshuliang":0,"monthccost":0,"dayshuliang":0,"dayccost":0}
     */

    private boolean error;
    private MsgBean msg;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * weekshuliang : 0
         * weektccost : 0
         * monthshuliang : 0
         * monthccost : 0
         * dayshuliang : 0
         * dayccost : 0
         */

        private int weekshuliang;
        private int weektccost;
        private int monthshuliang;
        private int monthccost;
        private int dayshuliang;
        private int dayccost;

        public int getWeekshuliang() {
            return weekshuliang;
        }

        public void setWeekshuliang(int weekshuliang) {
            this.weekshuliang = weekshuliang;
        }

        public int getWeektccost() {
            return weektccost;
        }

        public void setWeektccost(int weektccost) {
            this.weektccost = weektccost;
        }

        public int getMonthshuliang() {
            return monthshuliang;
        }

        public void setMonthshuliang(int monthshuliang) {
            this.monthshuliang = monthshuliang;
        }

        public int getMonthccost() {
            return monthccost;
        }

        public void setMonthccost(int monthccost) {
            this.monthccost = monthccost;
        }

        public int getDayshuliang() {
            return dayshuliang;
        }

        public void setDayshuliang(int dayshuliang) {
            this.dayshuliang = dayshuliang;
        }

        public int getDayccost() {
            return dayccost;
        }

        public void setDayccost(int dayccost) {
            this.dayccost = dayccost;
        }
    }
}
