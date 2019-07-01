package com.kaku.weac.service;

import android.os.Looper;
import android.util.Log;

import com.sunfusheng.daemon.AbsHeartBeatService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author sunfusheng on 2018/8/3.
 */
public class HeartBeatService extends AbsHeartBeatService {
    private static final String TAG = "---> HeartBeatService";
    private static final android.os.Handler mainThreadHandler = new android.os.Handler(Looper.getMainLooper());

    int i =0;
    @Override
    public void onStartService() {
        Log.d(TAG, "onStartService()");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "服务开启中  " + i);
                i++;
            }
        },0,1000*3);
    }

    @Override
    public void onStopService() {
        Log.e(TAG, "onStopService()");
    }

    @Override
    public long getDelayExecutedMillis() {
        return 0;
    }

    @Override
    public long getHeartBeatMillis() {
        return 30 * 1000;
    }

    @Override
    public void onHeartBeat() {
        Log.d(TAG, "onHeartBeat()");
    }
}
