package com.kaku.weac.nohttp;

import android.content.Context;

import com.yanzhenjie.nohttp.rest.Interceptor;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestHandler;
import com.yanzhenjie.nohttp.rest.Response;

public class HeaderInterceptor implements Interceptor {

    private Context context;

    public HeaderInterceptor(Context context) {
        this.context = context;
    }


    @Override
    public <T> Response<T> intercept(RequestHandler handler, Request<T> request) {
//        String token = SpUtil.getString(ConstantRequest.token, "");
//        request.addHeader("token", token);
        return handler.handle(request);
    }
}
