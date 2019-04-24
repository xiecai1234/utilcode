package com.fingerbeat.cloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.fingerbeat.cloud.net.rx.RxManager;
import com.fingerbeat.utilcode.constant.Const;


public abstract class BaseActivity extends AppCompatActivity {
    public RxManager mRxManager;
    protected static final String TAG = Const.TAG;

    protected Context getContext() {
        return this;
    }

    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        mRxManager = new RxManager();
        setContentView();
//        ButterKnife.bind(this);
        initView();
//        mRxManager = new RxManager();
        initData();
        //test
    }


    protected abstract void setContentView();

    protected void initView() {
    }

    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        dismissProgressDialog();
//        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 设置layout前配置
     */
    protected void doBeforeSetcontentView() {
        // 把actvity放到application栈中管理
//        AppManager.getAppManager().addActivity(this);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 默认着色状态栏
        SetStatusBarColor();
    }

    /**
     * 沉浸式状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
//        StatusBarUtil.setStatusBarColor(this, R.color.white);
//        StatusBarUtil.StatusBarLightMode(this, StatusBarUtil.StatusBarLightMode(this));
//        StatusBarUtil.myStatusBar(this);

//        StateBarUtil.customImmerseBar(this);
//        StateBarUtil.setStatusBarLightMode(this, false);
    }

//    private KProgressHUD hud;
//
//    public void showProgressDialog(boolean cancellable) {
//        if (null == hud) {
//            hud = KProgressHUD.create(this)
//                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                    .setDimAmount(0.5f);
//        }
//        hud.setLabel(null).setCancellable(cancellable).show();
//    }
//
//    public void showProgressDialog(boolean cancellable, String lable) {
//        if (null == hud) {
//            hud = KProgressHUD.create(this)
//                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                    .setDimAmount(0.5f);
//        }
//        hud.setLabel(lable).setCancellable(cancellable).show();
//    }
//
//    public void dismissProgressDialog() {
//        if (null != hud && hud.isShowing()) {
//            hud.dismiss();
//        }
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * des:隐藏软键盘,这种方式参数为activity
     *
     * @param activity
     */
    protected void hideInputForce(Activity activity) {
        if (activity == null || activity.getCurrentFocus() == null)
            return;
        ((InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 打开键盘
     **/
    protected void showInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

//    protected void toast(String msg) {
//        ToastUtil.showToast(this, msg);
//    }
//
//    public void showCenterToast(String msg) {
//        ToastUtil.showCenterToast(this, msg, false);
//    }
//
//    public void showCenterToast(String msg, boolean isLong) {
//        ToastUtil.showCenterToast(this, msg, isLong);
//    }
//
//    protected void log(String log) {
//        Logger.e(TAG, log);
//    }

}
