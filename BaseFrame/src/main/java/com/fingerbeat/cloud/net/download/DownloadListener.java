package com.fingerbeat.cloud.net.download;

/**
 * Created by xiecaibao on 2018/9/14
 */
public interface DownloadListener {
    void onProgress(long max, long progress);

    void onFail(String errMsg);
}
