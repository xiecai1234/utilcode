package com.fingerbeat.cloud.net;

import android.text.TextUtils;

import com.fingerbeat.cloud.net.bean.BaseBean;
import com.fingerbeat.utilcode.constant.Const;
import com.fingerbeat.utilcode.utils.encrypt.EncryptUtil;
import com.fingerbeat.utilcode.utils.log.Logger;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * Created by xiecaibao on 2019/2/22
 */
public class CustomResponseConverter<T> implements Converter<ResponseBody, BaseBean<T>> {
    private static final String TAG = Const.TAG;
    private final Gson gson;
    private Type type;

    public CustomResponseConverter(Gson gson, Type type)
    {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public BaseBean<T> convert(ResponseBody responseBody) throws IOException {
        BaseBean<T> result = new BaseBean<>();
        String oriJsonStr = responseBody.string();
//        Logger.e("convert oriJsonStr:" + oriJsonStr);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(oriJsonStr);
            result.setCode(jsonObject.getInt("code"));
            result.setMsg(jsonObject.getString("msg"));
            String encryptData = jsonObject.optString("data");
//            Logger.e("encryptData:" + encryptData);
//            if (!TextUtils.isEmpty(encryptData)) {TODO
//                String decryptData = EncryptUtil.decrypt(encryptData, CommFun.getDefaultKey(App.getContext()), Const.BM);
//                if (!TextUtils.isEmpty(decryptData)) {
//                    try {
//                        T data = gson.fromJson(decryptData, type);
//                        result.setData(data);
//                    } catch (Exception e) {
//                        Logger.e(TAG, e.toString(), e);
//                    }
//                }
//            }
        } catch (JSONException e) {
            Logger.e(e.toString());
        } finally {
            responseBody.close();
        }
        Logger.e(result.toString());
        return result;

    }
}
