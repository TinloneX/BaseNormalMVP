package com.company.project.config;

import android.os.Environment;

import com.company.project.BuildConfig;

/**
 * @author Tinlone
 * @date 2018/3/23.
 */

public interface Config {

    interface BaseUrls {
        String BASE_URL = "https://www.tinlone.com/api/";
    }

    /**
     * 数字常量
     */
    interface Numbers {
        /**
         * 单页数据量
         */
        int PAGE_SIZE = 10;
        /**
         * 开屏页保持时间
         */
        int ADVERTISEMENT_TIME = 4;
        /**
         * 验证码长度
         */
        int VERIFY_CODE_LENGTH = 6;
        /**
         * 时间格式化长度
         */
        int TIME_TEXT_LENGTH = 8;
        /**
         * 密码长度下限
         */
        int MIN_PWD_LENGTH = 6;
        /**
         * 电话号码长度
         */
        int PHONE_LENGTH = 11;
        /**
         * 密码长度上限
         */
        int MAX_PWD_LENGTH = 16;
        /**
         * 网页刷新等待时间
         */
        int WEB_WAITING_TIME = 30;
        /**
         * 验证码等待时间
         */
        int VERIFY_CODE_WAIT_TIME = 60;
        /**
         * 网页加载成功
         */
        int WEB_LOAD_100 = 95;
        /**
         * 连续时间点击限制
         */
        long CLICK_LIMIT = 666L;
    }


    /**
     * 字符常量
     */
    interface Strings {
        String SERVER_ERROR = "服务器开了小差~";

        String RESPONSE_OK = "0";
    }

    /**
     * 文件目录
     */
    interface Path {
        String PRIVATE = String.format("%s/%s", Environment.getExternalStorageState(), BuildConfig.APP_NAME);
        String FILE = PRIVATE + "/files/";
        String CACHE = PRIVATE + "/cache/";
        String MEDIA = PRIVATE + "/media/";
    }

}
