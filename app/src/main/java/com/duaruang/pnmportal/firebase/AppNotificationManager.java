package com.duaruang.pnmportal.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.activity.MainActivity;
import com.duaruang.pnmportal.activity.MainDrawerActivity;
import com.duaruang.pnmportal.data.News;
import com.google.gson.Gson;

/**
 * Created by user on 11/21/2017.
 */

public class AppNotificationManager {
    public static final int INFO_NOTIFICATION_ID = 12345;
    public static final String EXTRA_TAG = "extra_tag";
    public static final String EXTRA_DATA = "extra_news";

    public static void showNotification(Context context, String tag, String title, String message,String sJson) {
        Intent notificationIntent = new Intent(context, MainDrawerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAG, tag);

        try {
            if (!TextUtils.isEmpty(sJson)) {
                News model = new Gson().fromJson(sJson, News.class);
                bundle.putParcelable(EXTRA_DATA, model);
                Log.e("", "cetak json"+sJson);
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
        notificationIntent.putExtras(bundle);
        PendingIntent contentIntent = PendingIntent.getActivity(context, INFO_NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (TextUtils.isEmpty(title)) {
            title = tag;
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.icon_notif_ref)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setTicker(message)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(contentIntent)
                    .setPriority(Notification.PRIORITY_HIGH) // this is will pop up immediately the notification
                    .setDefaults(Notification.DEFAULT_ALL);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(INFO_NOTIFICATION_ID, builder.build());
        }else
        {
            android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setTicker(message)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(contentIntent)
                    .setPriority(Notification.PRIORITY_HIGH) // this is will pop up immediately the notification
                    .setDefaults(Notification.DEFAULT_ALL);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(INFO_NOTIFICATION_ID, builder.build());
        }
    }

}
