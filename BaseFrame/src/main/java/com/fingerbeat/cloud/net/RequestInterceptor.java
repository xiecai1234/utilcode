package com.fingerbeat.cloud.net;

import com.blankj.utilcode.util.AppUtils;
import com.fingerbeat.cloud.App;
import com.fingerbeat.utilcode.constant.Const;
import com.fingerbeat.utilcode.utils.log.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiecaibao on 2018/9/7
 * 在代码里添加header，需要使用拦截器
 */
public class RequestInterceptor implements Interceptor {
    public RequestInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json");
//        if (UserHelper.getInstance().isLogin()) {
//            String accessToken = UserHelper.getInstance().getAccessToken();
//            String tokenType = UserHelper.getInstance().getTokenType();
//            builder.addHeader("Authorization", tokenType + " " + accessToken);
//            Logger.e(Const.TAG, "token:" + accessToken);
//        }TODO
        builder.addHeader("FBVersion", String.valueOf(AppUtils.getAppVersionCode()));
        return chain.proceed(builder.build());
    }
}
