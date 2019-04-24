package com.fingerbeat.cloud.net.rx;


/**
 * des:对服务器返回数据成功和失败处理
 * Created by xsf
 * on 2016.09.9:59
 */


import com.fingerbeat.cloud.net.HttpResultCode;
import com.fingerbeat.cloud.net.bean.BaseBean;
import com.fingerbeat.utilcode.utils.log.Logger;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**************使用例子******************/
/*_apiService.login(mobile, verifyCode)
        .compose(RxSchedulersHelper.handleIO())
        .compose(RxResultHelper.handleLoginResult())
        .//省略*/

public class RxHelper {
    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> handleIO() {    //compose简化线程

        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> handleRespose() {   //response统一处理判断结果
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<T, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(T t) {
                        BaseBean<T> response = (BaseBean<T>) t;
                        if (response.getCode() == HttpResultCode.SUCCESS) {
                            return createData(response.getData());
                        } else {
//                            if (response.getCode() == INVALID_CODE) {
//                                UserHelper.getInstance().deleteAll();
//                            }
                            return Flowable.error(new ApiException(response.getCode(), response.getMsg()));
                        }
                    }
                });
            }
        };
    }

    /**
     * 生成Flowable
     *
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t == null ? (T) new Object() : t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    Logger.e(e.toString());
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

}
