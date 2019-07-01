package com.kaku.weac.nohttp;

import com.google.gson.Gson;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.StringRequest;

/**
 * <p>自定义JavaBean请求。</p>
 * Created by Yan Zhenjie on 2016/10/15.
 */
public class JavaBeanRequest<T> extends Request<T> {

    /**
     * 更多如何请求JavaBean，List，Map等复杂对象，请看这篇博客：
     *
     * http://blog.csdn.net/yanzhenjie1003/article/details/70158030
     */

    /**
     * 要解析的JavaBean。
     */
    private Class<T> clazz;

    public JavaBeanRequest(String url, Class<T> clazz) {
        this(url, RequestMethod.GET, clazz);
    }

    public JavaBeanRequest(String url, RequestMethod requestMethod, Class<T> clazz) {
        super(url, requestMethod);
        this.clazz = clazz;
    }

    @Override
    public T parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String response = StringRequest.parseResponseString(responseHeaders, responseBody);

        // 这里如果数据格式错误，或者解析失败，会在失败的回调方法中返回 ParseError 异常。
        return new Gson().fromJson(response, clazz);
    }
}
