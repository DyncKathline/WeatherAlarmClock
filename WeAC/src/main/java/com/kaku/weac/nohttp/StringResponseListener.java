package com.kaku.weac.nohttp;

import android.app.Activity;
import android.content.Intent;

import com.kaku.weac.R;
import com.kaku.weac.activities.BaseActivity;
import com.kaku.weac.util.ToastUtil;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by KathLine on 2017/12/22.
 */

public abstract class StringResponseListener extends HttpResponseListener<String> {

    private Activity mActivity;
    /**
     * Request.
     */
    private Request<?> mRequest;
    /**
     * 结果回调.
     */
    private HttpListener<String> callback;

//    public StringResponseListener(BaseActivity activity) {
//        this.mActivity = activity;
//    }

    public StringResponseListener(Activity activity) {
        this(activity, null);
    }

    public StringResponseListener(Activity activity, Request<?> request) {
        super(activity, request);
        mActivity = activity;
        mRequest = request;
    }

    @Override
    public void onStart(int what) {
        super.onStart(what);
    }

    /**
     * 成功回调.
     */
    @Override
    public void onSucceed(int what, Response<String> response) {
        super.onSucceed(what, response);
        if (callback != null) {
            // 这里判断一下http响应码，这个响应码问下你们的服务端你们的状态有几种，一般是200成功。
            // w3c标准http响应码：http://www.w3school.com.cn/tags/html_ref_httpmessages.asp
            callback.onSucceed(what, response);
        }
        if(response.responseCode() == 401) {

        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {
        super.onFailed(what, response);
        Exception exception = response.getException();
        if (exception instanceof NetworkError) {// 网络不好
            ToastUtil.showShortToast(mActivity, R.string.error_please_check_network);
        } else if (exception instanceof TimeoutError) {// 请求超时
            ToastUtil.showShortToast(mActivity, R.string.error_timeout);
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            ToastUtil.showShortToast(mActivity, R.string.error_not_found_server);
        } else if (exception instanceof URLError) {// URL是错的
            ToastUtil.showShortToast(mActivity, R.string.error_url_error);
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            // 没有缓存一般不提示用户，如果需要随你。
        } else {
            ToastUtil.showShortToast(mActivity, R.string.error_unknow);
        }
        Logger.e("错误：" + exception.getMessage());
        Logger.e("错误：" + exception.getMessage());
        if (callback != null)
            callback.onFailed(what, response);
    }

    @Override
    public void onFinish(int what) {
        super.onFinish(what);
    }
}
