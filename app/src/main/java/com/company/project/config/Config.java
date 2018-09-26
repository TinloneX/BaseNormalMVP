package com.company.project.config;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public interface Config {

    interface BaseUrls {
        String BASE_URL = "https://www.company.com/api/";
    }

    /**
     * 数字常量
     */
    interface Numbers {
        /**
         * 单页数据量
         */
        int PAGE_SIZE = 10;

        int TIME_TEXT_LENGTH = 8;

        int HEADER_REWARD = 0;

        int TAIL_REWARD = 80;

        int SCREEN_VALVE = 980;

        long CLICK_LIMITED = 666L;
    }

    /**
     * 字符常量
     */
    interface Strings {
        String SUCCESS = "success";

        String SERVER_ERROR = "服务器开了小差~";

        String RESPONSE_OK = "0";

    }

    interface ScreenType {
        String TYPE_NORMAL = "101";
        String TYPE_LARGE = "102";
    }

}
