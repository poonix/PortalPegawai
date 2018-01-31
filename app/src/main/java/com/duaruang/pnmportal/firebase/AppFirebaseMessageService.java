package com.duaruang.pnmportal.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by user on 11/21/2017.
 */

public class AppFirebaseMessageService extends FirebaseMessagingService {
    private static final String TAG = AppFirebaseMessageService.class.getSimpleName();
    public static final String TYPE_EVENTEMS        = "event_ems";
    public static final String TYPE_EVENT           = "event_general";
    public static final String TYPE_NEWS_GENERAL    = "news_general";
    public static final String TYPE_NEWS_PRIVATE    = "news_private";
    public static final String TYPE_FORCE_LOGOUT    = "force_logout";

    private static final String HEADER_TAG = "tag";
    private static final String HEADER_TITLE = "title";
    private static final String HEADER_MESSAGE = "message";
    private static final String HEADER_ITEM_EVENT = "content";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            String tag = remoteMessage.getData().get(HEADER_TAG);
            String title = remoteMessage.getData().get(HEADER_TITLE);
            String msg = remoteMessage.getData().get(HEADER_MESSAGE);
            String sJson = remoteMessage.getData().get(HEADER_ITEM_EVENT);
            AppNotificationManager.showNotification(this, tag, title, msg,sJson);

        }
    }
}
