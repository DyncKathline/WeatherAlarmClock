/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.kaku.weac.util;

import com.kaku.weac.Listener.HttpCallbackListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http工具类
 *
 * @author 咖枯
 * @version 1.0 2015/8/29
 */
public class HttpUtil {
    private static OkHttpClient sOkHttpClient;

    /**
     * 发送http请求
     *
     * @param address  访问地址
     * @param cityName 城市名
     * @param listener 响应监听
     */
    public static void sendHttpRequest(final String address, final String cityName, final
    HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String address1;
                    if (address == null) {
                        address1 = "http://wthrcdn.etouch.cn/WeatherApi?city=" + URLEncoder
                                .encode(cityName, "UTF-8");
                    } else {
                        address1 = address;
                    }
                    if (sOkHttpClient == null) {
                        OkHttpClient.Builder builder = new OkHttpClient.Builder();
                        builder.readTimeout(6000, TimeUnit.MILLISECONDS);//读取超时
                        builder.connectTimeout(6000, TimeUnit.MILLISECONDS);//连接超时
                        builder.writeTimeout(6000, TimeUnit.MILLISECONDS);//写入超时
                        sOkHttpClient = builder.build();
                    }
                    Request request = new Request.Builder().url(address1).build();
                    Response response = sOkHttpClient.newCall(request).execute();
                    String result = response.body().string();

                    if (listener != null) {
                        // 加载完成返回
                        listener.onFinish(result);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 加载失败
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }

}
