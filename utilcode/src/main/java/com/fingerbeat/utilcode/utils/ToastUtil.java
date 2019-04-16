package com.fingerbeat.utilcode.utils;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {

	private static Toast mToast;

	/**
	 * 弹出Toast
	 *
     * 缺点就是Looper.loop();后面的方法得不到执行，当然也可以调用Looper.myLooper().quit();然而退出也没用 TODO
     *
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, CharSequence msg) {
//		if (ToastUtil.mToast == null) {
//			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
//		} else {
//			mToast.setText(msg);
//		}
		if(Looper.myLooper() == Looper.getMainLooper()){
			buildToast(context, msg, false);
			mToast.show();
		}else{
			Looper.prepare();
			buildToast(context, msg, false);
			mToast.show();
	        Looper.loop();
            Looper.myLooper().quitSafely();
		}
	}

    private static void buildToast(Context context, CharSequence msg, boolean isLong) {
        if (ToastUtil.mToast == null) {
            int duration = isLong? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
    }

	public static void showToast(Context context, int resId) {
		showToast(context, context.getString(resId));
	}

	public static void showCenterToast(Context context, CharSequence msg, boolean isLong) {
		if(Looper.myLooper() == Looper.getMainLooper()){
			buildToast(context, msg, isLong);
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mToast.show();
		}else{
			Looper.prepare();
			buildToast(context, msg, isLong);
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mToast.show();
			Looper.loop();
            Looper.myLooper().quitSafely();
		}
	}

	/**
	 * 消失Toast
	 */
	public static void cancelToast() { 
		if (ToastUtil.mToast != null) {
			if(Looper.myLooper() == Looper.getMainLooper()){
				mToast.cancel();
			}else{
				Looper.prepare();
				mToast.cancel();
		        Looper.loop();
                Looper.myLooper().quitSafely();
			}
		}
    }
}
