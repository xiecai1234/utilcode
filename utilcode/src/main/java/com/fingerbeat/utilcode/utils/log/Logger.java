package com.fingerbeat.utilcode.utils.log;


import android.util.Log;

/**
 * 作者：XieCaibao
 * 时间：2019/4/16
 * 邮箱：825302814@qq.com
 */

public class Logger {
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    public static void d(String msg) {
        d(null, msg);
    }

    public static void d(String tag, String msg) {
        printLog(DEBUG, tag, msg, null);
    }

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(String tag, String msg) {
        printLog(INFO, tag, msg, null);
    }

    public static void w(String msg) {
        w(null, msg);
    }

    public static void w(String tag, String msg) {
        printLog(WARN, tag, msg, null);
    }

    public static void e(String msg) {
        e(null, msg);
    }

    public static void e(String tag, String msg) {
        printLog(ERROR, tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tb) {
        printLog(ERROR, tag, msg, tb);
    }

    private static String formatString(String msg) {
        return msg == null ? "" : msg;
    }

    private static void printLog(int priority, String label, String msg, Throwable tb) {
        String tag = null == label ? IConfig.getInstance().getTag() : label;
        String str = formatString(msg);
        if (IConfig.getInstance().getIsShowLog()) {
            if (VERBOSE == priority) Log.v(tag, str);
            if (DEBUG == priority) Log.d(tag, str);
            if (INFO == priority) Log.i(tag, str);
            if (WARN == priority) Log.w(tag, str);
            if (ERROR == priority) Log.e(tag, str, tb);
        }
        if (IConfig.getInstance().getIsWriteLog()) {
            FileUtils.writeLogFile(str);
        }
    }
}
