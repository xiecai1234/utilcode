package com.fingerbeat.cloud.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.apkfuns.logutils.LogUtils;

import java.util.Stack;

public class AppManager {
    private static Stack<Activity> mActivityStack;
    private volatile static AppManager instance;

    private AppManager() {

    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                    mActivityStack = new Stack<>();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        try {
            Activity activity = mActivityStack.lastElement();
            return activity;
        } catch (Exception e) {
            LogUtils.e(new IllegalStateException("当前Activity栈为空"));
            return null;
        }
    }

    /**
     * 当前activity是否是指定的activity
     *
     * @param cls
     * @return
     */
    public boolean isSpecifyActvity(Class<?> cls) {
        if (cls == currentActivity().getClass()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前Activity的前一个Activity
     */
    public Activity preActivity() {
        int index = mActivityStack.size() - 2;
        if (index < 0) {
            throw new IllegalStateException("当前Activity栈长度小于或等于1");
        }
        Activity activity = mActivityStack.get(index);
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        try {
            for (Activity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 打开 一个指定的Activity
     */
    public void startActivity(Class<?> cls) {
        startActivity(new Intent(currentActivity(), cls));
    }

    /**
     * 打开 一个指定的Activity
     */
    public void startActivity(Intent intent) {
        currentActivity().startActivity(intent);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(Class<?> cls) {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                if (mActivityStack.peek().getClass() == cls) {
                    break;
                }
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 返回到指定的activity
     *
     * @param cls
     */
    public void returnToActivity(Class<?> cls) {
        while (mActivityStack.size() != 0) {
            if (mActivityStack.peek().getClass() == cls) {
                break;
            } else {
                finishActivity(mActivityStack.peek());
            }
        }
    }


    /**
     * 是否已经打开指定的activity
     *
     * @param cls
     * @return
     */
    public boolean isOpenActivity(Class<?> cls) {
        if (mActivityStack != null) {
            for (int i = 0, size = mActivityStack.size(); i < size; i++) {
                if (cls == mActivityStack.peek().getClass()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得 指定activity
     *
     * @param cls
     * @return
     */
    public Activity getActivity(Class<?> cls) {
        if (mActivityStack != null) {
            for (int i = 0, size = mActivityStack.size(); i < size; i++) {
                if (cls == mActivityStack.get(i).getClass()) {
                    return mActivityStack.get(i);
                }
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     *
     * @param context      上下文
     * @param isBackground 是否开开启后台运行
     */
    public void AppExit(Context context, Boolean isBackground) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
        } catch (Exception e) {

        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
            if (!isBackground) {
                System.exit(0);
            }
        }
    }
}
