package com.acterics.healthmonitor.services.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.ui.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import timber.log.Timber;

/**
 * Created by oleg on 15.02.17.
 */

public class HMFirebaseMessagingService extends FirebaseMessagingService {

    private static final int MESSAGE_NOTIFICATION = 0;
    private static final int DAILY_CHECKINS_NOTIFICATION = 1;
    private static final int DEFAULT_NOTIFICATION = 2;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        
        super.onMessageReceived(remoteMessage);
        Timber.e("onMessageReceived: ");
        sendDefaultNotification(remoteMessage.getData());



    }

    private void sendDefaultNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_favorite_white_36dp)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("body"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DEFAULT_NOTIFICATION, notificationBuilder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e("DEBUG_LOG", "onDeletedMessages: ");
    }


}
