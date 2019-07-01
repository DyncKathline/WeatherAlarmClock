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
package com.kaku.weac.broadcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.kaku.weac.R;
import com.kaku.weac.activities.AlarmClockOntimeActivity;
import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.common.WeacStatus;
import com.kaku.weac.db.AlarmClockOperate;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;

/**
 * 闹钟响起广播
 *
 * @author 咖枯
 * @version 1.0 2015/06
 */
public class AlarmClockBroadcast extends BroadcastReceiver {

    /**
     * Log tag ：AlarmClockBroadcast
     */
    private static final String LOG_TAG = "AlarmClockBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmClock alarmClock = intent
                .getParcelableExtra(WeacConstants.ALARM_CLOCK);
        if (alarmClock != null) {
            // 单次响铃
            if (alarmClock.getWeeks() == null) {
                AlarmClockOperate.getInstance().updateAlarmClock(false,
                        alarmClock.getId());

                Intent i = new Intent("com.kaku.weac.AlarmClockOff");
                context.sendBroadcast(i);
            } else {
                // 重复周期闹钟
                MyUtil.startAlarmClock(context, alarmClock);
            }
        }

        // 小睡已执行次数
        int napTimesRan = intent.getIntExtra(WeacConstants.NAP_RAN_TIMES, 0);
        // 当前时间
        long now = SystemClock.elapsedRealtime();
        // 当上一次闹钟响起时间等于0
        if (WeacStatus.sLastStartTime == 0) {
            // 上一次闹钟响起时间等于当前时间
            WeacStatus.sLastStartTime = now;
            // 当上一次响起任务距离现在小于3秒时
        } else if ((now - WeacStatus.sLastStartTime) <= 3000) {

            LogUtil.d(LOG_TAG, "进入3秒以内再次响铃 小睡次数：" + napTimesRan + "距离时间毫秒数："
                    + (now - WeacStatus.sLastStartTime));
            LogUtil.d(LOG_TAG, "WeacStatus.strikerLevel："
                    + WeacStatus.sStrikerLevel);
//            LogUtil.d(LOG_TAG, "闹钟名：" + alarmClock.getTag());

            // 当是新闹钟任务并且上一次响起也为新闹钟任务时，开启了时间相同的多次闹钟，只保留一个其他关闭
            if ((napTimesRan == 0) & (WeacStatus.sStrikerLevel == 1)) {
                return;
            }
        } else {
            // 上一次闹钟响起时间等于当前时间
            WeacStatus.sLastStartTime = now;
        }

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            intentNotification(context, alarmClock, napTimesRan);
        }else {
            intentActivity(context, alarmClock, napTimesRan);
        }
    }

    private void intentActivity(Context context, AlarmClock alarmClock, int napTimesRan) {
        Intent it = new Intent(context, AlarmClockOntimeActivity.class);

        // 新闹钟任务
        if (napTimesRan == 0) {
            // 设置响起级别为闹钟
            WeacStatus.sStrikerLevel = 1;
            // 小睡任务
        } else {
            // 设置响起级别为小睡
            WeacStatus.sStrikerLevel = 2;
            // 小睡已执行次数
            it.putExtra(WeacConstants.NAP_RAN_TIMES, napTimesRan);
        }
        it.putExtra(WeacConstants.ALARM_CLOCK, alarmClock);
        // 清除栈顶的Activity
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(it);
    }

    private void intentNotification(Context context, AlarmClock alarmClock, int napTimesRan) {
        Log.e(LOG_TAG, "intentNotification");
        Intent fullScreenIntent = new Intent(context, AlarmClockOntimeActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);

        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(CHANNEL_ID);
        }

        builder.setSmallIcon(R.drawable.ic_nap_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nap_notification))
                .setContentTitle(name)
                .setContentText(Description)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)//DEFAULT_ALL
                .setTicker("悬浮通知")
                .setFullScreenIntent(fullScreenPendingIntent, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }else {
            notificationManager.notify(NOTIFICATION_ID, builder.getNotification());
        }
    }
}
