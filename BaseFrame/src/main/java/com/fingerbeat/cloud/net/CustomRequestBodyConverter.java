package com.fingerbeat.cloud.net;

import com.fingerbeat.utilcode.constant.Const;
import com.fingerbeat.utilcode.utils.encrypt.EncryptUtil;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Created by xiecaibao on 2019/2/26
 */
final class CustomRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        //只有这几种类型才会走到本方法 {@link Body @Body}, {@link Part @part}, and {@link PartMap @PartMap}
//        String json = new Gson().toJson(value);
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = gson.newJsonWriter(writer);
        adapter.write(jsonWriter, value);
        jsonWriter.close();
        //进行加密
//        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        String content = buffer.readUtf8();
//        String data = EncryptUtil.encrypt(content, CommFun.getDefaultKey(App.getContext()), Const.BM);//TODO
        return RequestBody.create(MEDIA_TYPE, content);
    }
}
