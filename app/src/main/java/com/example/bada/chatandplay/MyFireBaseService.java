package com.example.bada.chatandplay;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Debug;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Bada on 10/12/2017.
 */

public class MyFireBaseService extends FirebaseMessagingService {

    public MyFireBaseService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notificatio_title = remoteMessage.getNotification().getTitle();
        String notification_message = remoteMessage.getNotification().getBody();
        String  from_user_id = remoteMessage.getData().get("userID");
        Log.d(from_user_id,"ok");
        String  click_action = remoteMessage.getNotification().getClickAction();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.bada)
                        .setContentTitle(notificatio_title)
                        .setContentText(notification_message);

             // Sets an ID for the notification
        int mNotificationId = (int) System.currentTimeMillis();
        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("userID",from_user_id);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
            // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
