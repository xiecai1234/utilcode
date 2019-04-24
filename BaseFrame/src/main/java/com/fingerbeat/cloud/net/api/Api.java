package com.fingerbeat.cloud.net.api;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;

import com.fingerbeat.cloud.App;
import com.fingerbeat.cloud.net.CustomConverterFactory;
import com.fingerbeat.cloud.net.HttpConfig;
import com.fingerbeat.cloud.net.HttpService;
import com.fingerbeat.cloud.net.JsonFormatterImpl;
import com.fingerbeat.cloud.net.RequestInterceptor;
import com.fingerbeat.utilcode.utils.NetWorkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import tech.linjiang.pandora.Pandora;

/**
 * des:retorfit api
 * Created by xsf
 * on 2016.06.15:47
 */
public class Api {
    /**
     * 缓存
     */
    public static final String CACHE = "cache";
    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 7676;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 7676;
    public Retrofit retrofit;
    public HttpService httpService;

    private static SparseArray<Api> sRetrofitManager = new SparseArray<>(HostType.TYPE_COUNT);

    /*************************缓存设置*********************/

    /*
    1. noCache 不使用缓存，全部走网络

    2. noStore 不使用缓存，也不存储缓存

    3. onlyIfCached 只使用缓存

    4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

    5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合 感觉这两个类似 还没怎么弄清楚，清楚的同学欢迎留言

    6. minFresh 设置有效时间，依旧如上

    7. FORCE_NETWORK 只走网络

    8. FORCE_CACHE 只走缓存*/

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    /**
     * if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示用户可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么用户可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";


    //构造方法私有
    private Api(final int hostType) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(CustomConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(HttpConfig.BASE_URL)
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    /**
     * 获取 OkHttpClient
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {

        //开启Log
        final HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //缓存
        File cacheFile = new File(App.getAppContext().getCacheDir(), CACHE);
        final Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        //增加头部信息
        //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置

        /**
         * 云端响应头拦截器，用来配置缓存策略
         * Dangerous interceptor that rewrites the server's cache-control header.
         */
        final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                String cacheControl = request.cacheControl().toString();
                if (!NetWorkUtils.isNetConnected(App.getAppContext())) {
                    request = request.newBuilder()
                            .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                            .build();
                }
                Response originalResponse = chain.proceed(request);
                if (NetWorkUtils.isNetConnected(App.getAppContext())) {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                            .removeHeader("Pragma")
                            .build();
                }
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.sslSocketFactory(Utils.getSslSocketFactory());
        builder.readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
        builder.addInterceptor(mRewriteCacheControlInterceptor);
        builder.addNetworkInterceptor(mRewriteCacheControlInterceptor);
        Pandora.get().getInterceptor().setJsonFormatter(new JsonFormatterImpl());
        builder.addInterceptor(new RequestInterceptor());
        builder.addInterceptor(logInterceptor);
        builder.addInterceptor(Pandora.get().getInterceptor());
//        if (BuildConfig.DEBUG && Utils.isEmulator(App.getAppContext())) {
//            builder.dns(new WkDns());
//        }
        builder.cache(cache);
        return builder.build();
    }


    /**
     * @param hostType LOGIN：用户登录
     *                 YG_BASE：应用内数据请求
     *                 PAY：支付
     */
    public static HttpService getService(int hostType) {
        Api retrofitManager = sRetrofitManager.get(hostType);
        if (retrofitManager == null) {
            retrofitManager = new Api(hostType);
            sRetrofitManager.put(hostType, retrofitManager);
        }
        return retrofitManager.httpService;
    }

    /**
     * 根据网络状况获取缓存的策略
     */
    @NonNull
    public static String getCacheControl() {
        return NetWorkUtils.isNetConnected(App.getAppContext()) ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }
}