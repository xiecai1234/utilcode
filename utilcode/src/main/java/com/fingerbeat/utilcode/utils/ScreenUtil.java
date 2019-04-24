package com.fingerbeat.utilcode.utils;

import android.content.Context;

/**
 * 作者：XieCaibao
 * 时间： 2019/4/24
 * 邮箱：825302814@qq.com
 */
public class ScreenUtil {
    //返回值就是状态栏的高度,得到的值单位px
    public float getStatusBarHeight(Context context) {
        float result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimension(resourceId);
        }
        return result;
    }
    //返回值就是导航栏的高度,得到的值单位px
    public float getNavigationBarHeight(Context context) {
        float result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimension(resourceId);
        }
        return result;
    }
}
