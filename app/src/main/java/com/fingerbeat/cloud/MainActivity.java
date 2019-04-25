package com.fingerbeat.cloud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.fingerbeat.utilcode.constant.Const;
import com.fingerbeat.utilcode.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：XieCaibao
 * 时间： 2019/4/25
 * 邮箱：825302814@qq.com
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = Const.TAG;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Utils.init(this);
    }

    @OnClick({R.id.btn1, R.id.btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                float statusBarHeight = ScreenUtil.getStatusBarHeight(getApplicationContext());
                Log.e(TAG, "statusBarHeight:" + statusBarHeight);
                float navigationBarHeight = ScreenUtil.getNavigationBarHeight(getApplicationContext());
                Log.e(TAG, "navigationBarHeight:" + navigationBarHeight);

                int screenWidth = ScreenUtils.getScreenWidth();
                int screenHeight = ScreenUtils.getScreenHeight();
                float dencity = ScreenUtils.getScreenDensity();
                int dencityDpi = ScreenUtils.getScreenDensityDpi();

                Log.e(TAG, "screenWidth:" + screenWidth);
                Log.e(TAG, "screenHeight:" + screenHeight);
                Log.e(TAG, "dencity:" + dencity);
                Log.e(TAG, "dencityDpi:" + dencityDpi);
                break;
            case R.id.btn2:

                break;
        }
    }
}
