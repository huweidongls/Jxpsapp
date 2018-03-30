package com.example.zsccpsapp.jxpsapp.Bean;

/**
 * Created by 99zan on 2018/2/7.
 */

public class DaytjBean {

    /**
     * error : false
     * msg : {"shuliang":2,"tccost":14}
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
         * shuliang : 2
         * tccost : 14
         */

        private int shuliang;
        private int tccost;

        public int getShuliang() {
            return shuliang;
        }

        public void setShuliang(int shuliang) {
            this.shuliang = shuliang;
        }

        public int getTccost() {
            return tccost;
        }

        public void setTccost(int tccost) {
            this.tccost = tccost;
        }
    }
}
