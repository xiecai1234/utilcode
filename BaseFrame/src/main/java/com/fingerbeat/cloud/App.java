package com.fingerbeat.cloud;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.fingerbeat.cloud.base.AppManager;
import com.fingerbeat.utilcode.constant.Const;
import com.fingerbeat.utilcode.utils.AbnormalHandler;
import com.fingerbeat.utilcode.utils.ToastUtil;
import com.fingerbeat.utilcode.utils.log.IConfig;
import com.fingerbeat.utilcode.utils.log.Logger;
import com.squareup.leakcanary.LeakCanary;

public class App extends Application {
    private static final String TAG = Const.TAG;
    private static App mApp;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        StubAppUtils.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e(TAG, "Application onCreate, process = " + android.os.Process.myPid());
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
        mApp = this;
        //初始化 工具Utils
        Utils.init(this);
        AbnormalHandler.getInstance().init(this);
        IConfig.getInstance().isShowLog(true)//是否在logcat中打印log,默认不打印
                .isWriteLog(true)//是否在文件中记录，默认不记录
                .fileSize(5000000L)//日志文件的大小5M，默认0.1M,以bytes为单位
//                .fileSize(10000L)//日志文件的大小10k 调试用
                .tag(TAG);//logcat 日志过滤tag
    }

    public static Context getContext() {
        return mApp.getApplicationContext();
    }

    public static Context getAppContext() {
        return mApp;
    }

    public static synchronized App getInstance() {
        return mApp;
    }

    /**
     * 吐司一哈子
     *
     * @param msg
     */
    public void showToast(String msg) {
        ToastUtil.showToast(this, msg);
    }

    public void showCenterToast(String msg) {
        ToastUtil.showCenterToast(this, msg, false);
    }

    public void showCenterToast(String msg, boolean isLong) {
        ToastUtil.showCenterToast(this, msg, isLong);
    }

    public static Activity currentActivity() {
        return AppManager.getAppManager().currentActivity();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    boolean hasNewNumber;

    public boolean isHasNewNumber() {
        return hasNewNumber;
    }

    public void setHasNewNumber(boolean hasNewNumber) {
        this.hasNewNumber = hasNewNumber;
    }
}
