package com.fingerbeat.cloud.base;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public abstract class BaseDialog extends Dialog {

    public BaseDialog(Activity activity, int theme) {
        super(activity, theme);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View dialogView = inflater.inflate(getLayoutId(), null);
//        initVixx(dialogView);
//        setListener(dialogView);
    }

    protected abstract void initView();

    //    protected void initVixx(View dialogView) {}
//    protected abstract int getLayoutId();
//    protected void setListener(View dialogView){}
    public interface OnConfirmListener {
        void onClick();
    }

    public interface OnCancelListener {
        void onClick();
    }


    protected OnConfirmListener onConfirmListener;

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.onConfirmListener = listener;
    }

    //这个也是确定按钮，当有2个确定按钮的时候才需要用到
    protected OnConfirmListener onOkListener;

    public void setOnOkListener(OnConfirmListener listener) {
        this.onOkListener = listener;
    }

    protected OnCancelListener onCancelListener;

    public void setOnCancelListener(OnCancelListener listener) {
        this.onCancelListener = listener;
    }

    //    protected void setClick(View view) {
//        view.setOnClickListener(view1 -> {
//            if (null != onConfirmListener) {
//                onConfirmListener.onClick();
//            }
//            dismiss();
//        });
//    }

    public void display() {
        setCanceledOnTouchOutside(true); // 点击Dialog之外的区域对话框消失
        Window window =getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.y = -64; // 设置竖直方向的偏移量
        lp.dimAmount = 0f; // 弹出对话框的时候背景不变暗
        show();
    }

    public void display(boolean cancelable) {
        setCanceledOnTouchOutside(cancelable); // 点击Dialog之外的区域对话框消失
        setCancelable(cancelable);
        Window window =getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.y = -64; // 设置竖直方向的偏移量
        lp.dimAmount = 0f; // 弹出对话框的时候背景不变暗
        show();
    }
}





