package com.sunny.test.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.sunny.test.R;

/**
 * Created by Sunny on 2017/8/22 0022.
 */

public class MyNotifyUtil {
    private final Context context;
    private final int notifyId;
    private final NotificationManager manager;
    private NotificationCompat.Builder builder;

    public static MyNotifyUtil newInstance(@NonNull final Context context, int notifyId) {
        return new MyNotifyUtil(context, notifyId);
    }

    private MyNotifyUtil(@NonNull Context context, int notifyId) {
        this.context = context;
        this.notifyId = notifyId;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
    }

    public MyNotifyUtil setCompatBuilder(NotificationCompat.Builder builder) {
        this.builder = builder;
        return this;
    }


    public void show() {
        if(builder == null)
            return;
        manager.notify(notifyId, builder.build());
    }

    public static final class NormalNotificationBuilder {
        private final Context context;
        private final NotificationCompat.Builder builder;
        private boolean sound = false;
        private boolean vibrate = false;
        private boolean lights = false;

        public NormalNotificationBuilder(@NonNull final Context context) {
            this.context = context;
            builder = new NotificationCompat.Builder(context);
        }

        public NormalNotificationBuilder contentIntent(final PendingIntent val) {
            builder.setContentIntent(val);
            return this;
        }

        public NormalNotificationBuilder smallIcon(@DrawableRes final int val) {
            builder.setSmallIcon(val);
            return this;
        }

        public NormalNotificationBuilder largerIcon(@DrawableRes final int val) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), val);
            builder.setLargeIcon(bitmap);
            return this;
        }

        public NormalNotificationBuilder ticker(final String val) {
            builder.setTicker(val);
            return this;
        }

        public NormalNotificationBuilder title(final String val) {
            builder.setContentTitle(val);
            return this;
        }

        public NormalNotificationBuilder content(final String val) {
            builder.setContentText(val);
            return this;
        }

        public NormalNotificationBuilder isSound(boolean val) {
            sound = val;
            return this;
        }

        public NormalNotificationBuilder isVibrate(boolean val) {
            vibrate = val;
            return this;
        }

        public NormalNotificationBuilder isLight(boolean val) {
            lights = val;
            return this;
        }

        public NormalNotificationBuilder autoCancel(boolean val) {
            builder.setAutoCancel(val);
            return this;
        }

        public NotificationCompat.Builder build() {
            int defaults = 0;

            if (sound) {
                defaults |= Notification.DEFAULT_SOUND;
            }
            if (vibrate) {
                defaults |= Notification.DEFAULT_VIBRATE;
            }
            if (lights) {
                defaults |= Notification.DEFAULT_LIGHTS;
            }

            builder.setWhen(System.currentTimeMillis());
            builder.setPriority(NotificationCompat.PRIORITY_MAX);

            builder.setDefaults(defaults);
            return builder;
        }

        /**
         * 设置在顶部通知栏中的各种信息
         *
         * @param pendingIntent
         * @param smallIcon
         * @param ticker
         */
        private NotificationCompat.Builder
            createCompatBuilder(final Context context, final PendingIntent pendingIntent, final @DrawableRes int smallIcon, String ticker,
                            final String title, final String content, final boolean sound, final boolean vibrate,
                            final boolean lights) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(pendingIntent);// 该通知要启动的Intent
            builder.setSmallIcon(R.mipmap.ic_launcher);// 设置顶部状态栏的小图标
            builder.setTicker(ticker);// 在顶部状态栏中的提示信息
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);
            builder.setLargeIcon(bitmap);
            builder.setContentTitle(title); // 设置通知中心的标题
            builder.setContentText(content);// 设置通知中心中的内容
            builder.setWhen(System.currentTimeMillis());

		/*
         * 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失,
		 * 不设置的话点击消息后也不清除，但可以滑动删除
		 */
            builder.setAutoCancel(false);
            // 将Ongoing设为true 那么notification将不能滑动删除
            // notifyBuilder.setOngoing(true);
        /*
         * 从Android4.1开始，可以通过以下方法，设置notification的优先级，
		 * 优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
		 */
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
        /*
         * Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
		 * Notification.DEFAULT_SOUND：系统默认铃声。
		 * Notification.DEFAULT_VIBRATE：系统默认震动。
		 * Notification.DEFAULT_LIGHTS：系统默认闪光。
		 * notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
		 */
            int defaults = 0;

            if (sound) {
                defaults |= Notification.DEFAULT_SOUND;
            }
            if (vibrate) {
                defaults |= Notification.DEFAULT_VIBRATE;
            }
            if (lights) {
                defaults |= Notification.DEFAULT_LIGHTS;
            }

            builder.setDefaults(defaults);
            return builder;
        }
    }

}
