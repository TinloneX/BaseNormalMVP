package com.company.project.config;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public interface Config {

    interface BaseUrls{
        String BASE_URL = "https://www.company.com/api/";
        String TEST_URL = "http://tingapi.ting.baidu.com/";
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

//--------------------------------------------------------------------------------------------------

    /**
     * 布局类型
     */
    interface ViewType {
        /**
         * 正常布局
         */
        int VIEWTYPE_NORMAL = 0;
        /**
         * 加载更多
         */
        int VIEWTYPE_LOAD_MORE = 1;
        /**
         * 没有更多
         */
        int VIEWTYPE_NO_MORE = 2;
    }

}
