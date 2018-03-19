package cn.droidlover.xdroidmvp.converter;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by ronaldo on 2017/5/3.
 */

public class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Override
    public RequestBody convert(T value) throws IOException {
        return FormBody.create(MEDIA_TYPE, JSON.toJSONBytes(value));
    }
}
