package com.company.project.util;

import android.widget.TextView;

import com.company.project.base.BaseResponse;
import com.company.project.mvp.IView;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Tinlone
 * @date 2018/3/26.
 */

public final class Check {

    private Check() {
    }

    public static int size(Collection list) {
        if (empty(list)) {
            return 0;
        }
        return list.size();
    }

    /**
     * 判断字符
     *
     * @param text 字符
     * @return 值状态
     */
    public static boolean hasContent(CharSequence text) {
        return text != null && text.toString().trim().length() > 0;
    }

    /**
     * 判断字符
     *
     * @param text 字符
     * @return 值状态
     */
    public static boolean empty(CharSequence text) {
        return text == null || text.length() == 0;
    }


    public static boolean maxThen(String text, int size) {
        return Check.hasContent(text) && text.length() > size;
    }

    public static boolean maxThen(Collection list, int size) {
        return Check.hasContent(list) && list.size() > size;
    }

    /**
     * 判断集合
     *
     * @param list 集合
     * @return 集合状态
     */
    public static boolean hasContent(Collection list) {
        return list != null && list.size() > 0;
    }

    /**
     * 判断集合
     *
     * @param list 集合
     * @return 集合状态
     */
    public static boolean empty(Collection list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断是否有输入
     */
    public static boolean hasContent(TextView view) {
        return view != null && hasContent(view.getText());
    }

    /**
     * 判断是否有输入
     */
    public static boolean empty(TextView view) {
        return view == null || empty(view.getText());
    }

    /**
     * 判断响应
     *
     * @param response 响应
     * @return 响应实体状态
     */
    public static boolean hasContent(BaseResponse response) {
        return response != null && response.getResultData() != null;
    }

    /**
     * 判断响应
     *
     * @param response 响应
     * @return 响应实体状态
     */
    public static boolean hasContent(BaseResponse response, IView view) {
        return response != null
//                && response.getData() != null
                && view != null;
    }

    /**
     * 是否是合法的网址
     *
     * @param url url
     * @return url合法否
     */
    public static boolean isLegalWebSite(String url) {
        if (!hasContent(url)) {
            return false;
        }
        String regExp = "\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/)))";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(url);
        return m.matches();
    }

    /**
     * 是否是路径格式
     */
    public static boolean isPath(String path) {
        if (empty(path)) {
            return false;
        }
        String regExp = "[/\\\\]+(\\w+\\.)+[a-zA-Z]+$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(path);
        return m.matches();
    }

    /**
     * 比较文字信息
     *
     * @param v1 文字控件
     * @param v2 文字控件
     * @return 类似否
     */
    public static boolean equals(TextView v1, TextView v2) {
        return v1 != null && v2 != null && v1.getText().toString().equals(v2.getText().toString());
    }

    /**
     * 检测IP和掩码DNS等格式文本
     */
    public static boolean ipLike(String text) {
        String regExp = "^\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}$";
        Pattern compile = Pattern.compile(regExp);
        Matcher matcher = compile.matcher(text);
        return matcher.matches();
    }


}
