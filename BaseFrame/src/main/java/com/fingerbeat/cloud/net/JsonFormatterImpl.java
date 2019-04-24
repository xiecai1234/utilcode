package com.fingerbeat.cloud.net;

import com.google.gson.Gson;

import tech.linjiang.pandora.network.JsonFormatter;

/**
 * Created by xiecaibao on 2018/12/26
 */
public class JsonFormatterImpl implements JsonFormatter {
    @Override
    public String format(String result) {
        return new Gson().toJson(result);
//        return JSON.toJSONString(JSON.parse(result));
    }
}
