package com.company.project.util;

import com.company.project.config.Config;

/**
 * @author EDZ
 * @date 2018/3/26.
 */

public class TypeCalculator {

    public static String forScreenType() {
        int screenWidth = DensityUtil.getScreenWidth();
        return screenWidth >= Config.Numbers.SCREEN_VALVE ? Config.ScreenType.TYPE_LARGE : Config.ScreenType.TYPE_NORMAL;
    }


}
