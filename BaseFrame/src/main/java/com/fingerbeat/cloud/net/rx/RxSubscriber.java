package com.fingerbeat.cloud.net.rx;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.LogUtils;
import com.fingerbeat.cloud.App;
import com.fingerbeat.cloud.R;
import com.fingerbeat.cloud.base.AppManager;
import com.fingerbeat.cloud.net.HttpResultCode;
import com.fingerbeat.utilcode.constant.Const;
import com.fingerbeat.utilcode.utils.NetWorkUtils;
import com.fingerbeat.utilcode.utils.log.Logger;
import com.mingxiu.dialog.dialog.IOSLoad;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * des:订阅封装
 * Created by xsf
 * on 2016.09.10:16
 */

/********************使用例子********************/
/*_apiService.login(mobile, verifyCode)
        .//省略
        .subscribe(new RxSubscriber<UserBean user>(mContext,false) {
@Override
public void _onNext(UserBean user) {
        // 处理user
        }

@Override
public void _onError(String msg) {
        ToastUtil.showShort(mActivity, msg);
        });*/
public abstract class RxSubscriber<T> extends ResourceSubscriber<T> {
    private static final String TAG = Const.TAG;
    private Context mContext;
    private String msg;
    private boolean showDialog = true;
    SwipeRefreshLayout mSwiperefresh;

    public RxSubscriber(SwipeRefreshLayout swiperefresh) {
        mSwiperefresh = swiperefresh;
        showDialog = false;
    }

    /**
     * 是否显示浮动dialog
     */
    public void showDialog() {
        this.showDialog = true;
    }

    public void hideDialog() {
        this.showDialog = true;
    }

    public RxSubscriber(Context context) {
        this(context, "", true);
    }

    public RxSubscriber() {
        this(null, "", false);
    }

    public RxSubscriber(boolean showDialog) {
        this(null, "", showDialog);
    }

    public RxSubscriber(Context context, boolean showDialog) {
        this(context, "", showDialog);
    }

    public RxSubscriber(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    public RxSubscriber(String msg, boolean showDialog) {
        this.msg = msg;
        this.showDialog = showDialog;
    }

    @Override
    public void onComplete() {
        if (showDialog) {
            IOSLoad.cancelDialog();
        }
        if (mSwiperefresh != null) {
            mSwiperefresh.setRefreshing(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mSwiperefresh != null) {
            mSwiperefresh.setRefreshing(true);
        }
        if (showDialog) {
            new IOSLoad.Builder(AppManager.getAppManager().currentActivity())
                    .setMsg(msg)
                    .setCancelable(true)
                    .show();
        }
    }

    @Override
    public void onNext(T t) {
        try {
            _onNext(t);
            _onComplete();
        } catch (Exception e) {
            _onError(e.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        _onComplete();
        Logger.e(TAG, "onError:" + e.toString(), e);
        //网络
        if (!NetWorkUtils.isNetConnected(App.getAppContext())) {
            LogUtils.d(R.string.net_error);
            _onError(App.getAppContext().getString(R.string.net_error));
        } else if (e instanceof ApiException) {
            //请求成功错误信息
            Logger.e(TAG, "get ApiException:" + e.toString());
            switch (((ApiException) e).getCode()) {
                //统一处理通用异常
                case HttpResultCode.FAIL_SRV_COMMON:
                    _onError(e.getMessage());
                    break;
                default:
                    //业务逻辑处理
                    _onError((ApiException) e);
                    break;
            }
        } else {
            //服务器异常
            LogUtils.d(e.getMessage());
            _onError(e.getMessage());
        }

    }

    protected abstract void _onNext(T t);

    protected void _onError(String message) {
        App.getInstance().showToast(message);
    }

    protected void _onError(ApiException e) {
        Logger.e(TAG, "parent _onError");
        App.getInstance().showToast(e.getMessage());
    }

    protected void _onComplete() {
        onComplete();
    }

}
