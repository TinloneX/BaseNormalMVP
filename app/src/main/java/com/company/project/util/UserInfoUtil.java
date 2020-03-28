package com.company.project.util;

import com.company.project.MyApplication;
import com.company.project.bean.UserInfoBean;
import com.google.gson.Gson;


/**
 * Created by Administrator on 2017/11/22.
 * 管理用户信息
 */

public class UserInfoUtil {

    static UserInfoBean mUserInfoBean;

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static UserInfoBean getUserInfo() {
        synchronized (UserInfoUtil.class) {
            if (mUserInfoBean == null) {
                Gson gson = new Gson();
                String json = (String) SharedPreferencesUtil.getParam(MyApplication.getAppContext(), SharedPreferencesUtil.USER_INFO, "{}");
                try {
                    mUserInfoBean = gson.fromJson(json, UserInfoBean.class);
                } catch (Exception e) {
                    mUserInfoBean = new UserInfoBean();
                }
                if (mUserInfoBean == null) {
                    mUserInfoBean = new UserInfoBean();
                }
            }
        }
        return mUserInfoBean;
    }

    /**
     * 更新用户信息
     *
     * @param info info
     */
    public static void updateUserInfo(UserInfoBean info) {
        synchronized (UserInfoUtil.class) {
            Gson gson = new Gson();
            String json = gson.toJson(info);
            TLog.w("OkHttp", json);
            SharedPreferencesUtil.setParam(MyApplication.getAppContext(), SharedPreferencesUtil.USER_INFO, json);
            mUserInfoBean = info;
        }
    }

    public static void clearUserInfo() {
        TLog.w("OkHttp - clearUserInfo");
        updateUserInfo(new UserInfoBean());
    }

    public static boolean isLogin() {
        return Check.hasContent(getUserInfo().getToken());
    }

}
