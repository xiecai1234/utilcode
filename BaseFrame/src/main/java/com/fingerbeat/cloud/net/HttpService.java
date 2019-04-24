package com.fingerbeat.cloud.net;

import io.reactivex.Flowable;
import retrofit2.http.POST;

/**
 * Created by xiecaibao on 2018/9/6
 */
public interface HttpService {
    //版本检测
    @POST("api/download/apk")
    Flowable<Object> checkAppUpdateInfo();
}
