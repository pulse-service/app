package com.electrocraft.nirzo.pluse.model.deserialization;

/**
 * Created by nirzo on 3/7/2018.
 */

public class Country {

    private Result[] result;
    private String mss;

    public Result[] getResult() {
        return result;
    }

    public void setResult(Result[] result) {
        this.result = result;
    }

    public String getMss() {
        return mss;
    }

    public void setMss(String mss) {
        this.mss = mss;
    }

    private class Result {
        public Result() {
        }

        private String shortName;
        private String accessCode;

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getAccessCode() {
            return accessCode;
        }

        public void setAccessCode(String accessCode) {
            this.accessCode = accessCode;
        }
    }
}
