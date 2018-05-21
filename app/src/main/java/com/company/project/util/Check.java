package com.company.project.util;

import com.company.project.base.BaseResponse;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author EDZ
 * @date 2018/3/26.
 */

public final class Check {

    private Check() {
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
     * 判断集合
     *
     * @param list 集合
     * @return 集合状态
     */
    public static boolean hasContent(Collection list) {
        return list != null && list.size() > 0;
    }

    /**
     * 判断响应
     * @param response 响应
     * @return  响应实体状态
     */
    public static boolean hasContent(BaseResponse response) {
        return response != null && response.getResultData() != null;
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

}
