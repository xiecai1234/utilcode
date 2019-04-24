package com.fingerbeat.cloud.net.rx;


import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.AsyncProcessor;

import static android.R.attr.tag;


/**
 * ----------BigGod be here!----------/
 * *     ┏┓            ┏┓
 * *┏━┛┻━━━━━━┛┻━━┓*******
 * *┃                          ┃*******
 * *┃           ━━━         ┃*******
 * *┃                          ┃*******
 * *┃      ━┳┛   ┗┳━     ┃*******
 * *┃                          ┃*******
 * *┃          ━┻━          ┃*******
 * *┃                          ┃*******
 * *┗━━━┓          ┏━━━┛*******
 * *   *****┃          ┃神兽保佑*****
 * *   *****┃          ┃代码无BUG！***
 * *   *****┃          ┗━━━━━━━━┓*****
 * *   *****┃                            ┣┓****
 * *   *****┃                            ┏┛****
 * *   *****┗━┓┓┏━━━━┳┓┏━━━┛*****
 * *     *******┃┫┫    ****┃┫┫********
 * *     *******┗┻┛    ****┗┻┛*********
 * *       ━━━━━━神兽出没━━━━━━
 * 版权所有：
 * 作者：Created by JimBo
 * 创建时间：2017/6/6
 * Email：jimbo0826@foxmail.com
 * 修订历史：1.0
 * 描述：
 */

public class RxBus {
    private static RxBus instance;
    private ConcurrentHashMap<Object, List<AsyncProcessor>> mSubjectMapper = new ConcurrentHashMap<>();

    private RxBus() {}

    public static synchronized RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    /**
     * 订阅事件源
     * @param mObservable
     * @param consumer
     * @return
     */
    public RxBus onEvent(Flowable<?> mObservable, Consumer<? super Object> consumer) {
        mObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(consumer, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
        return instance;
    }

    /**
     * 注册事件源
     * @param key
     * @param <T>
     * @return
     */
    public <T> Flowable<T> register(@NonNull Object key) {
        List<AsyncProcessor > subjectList = mSubjectMapper.get(key);
        if (subjectList == null) {
            subjectList = new ArrayList<>();
            mSubjectMapper.put(key, subjectList);
        }
        AsyncProcessor <T> subject;
        subjectList.add(subject = AsyncProcessor.create());
        return subject;
    }

    /**
     * 取消监听
     * @param key
     */
    public RxBus unRegister(@NonNull Object key) {
        List<AsyncProcessor> subjectList = mSubjectMapper.get(key);
        if (subjectList != null) {
            mSubjectMapper.remove(key);
            LogUtils.d("unregister"+ tag + "  size:" + subjectList.size());
        }
        return instance;
    }

    /**
     * 触发事件
     * @param object
     */
    public void post(@NonNull Object object) {
        post(object.getClass().getName(), object);
    }

    public void post(@NonNull Object key, Object object) {
        LogUtils.d("post" + "eventName: " + tag);
        List<AsyncProcessor> subjects = mSubjectMapper.get(key);
        if (!collectionIsEmpty(subjects)) {
            for (AsyncProcessor subject : subjects) {
                subject.onNext(object);
                LogUtils.d("onEvent"+ "eventName: " + tag);
            }
        }
    }

    /**
     * 判断集合是否为空
     * @param collection
     * @return
     */
    public static boolean collectionIsEmpty(Collection<AsyncProcessor> collection) {
        return collection == null || collection.isEmpty();
    }
}
