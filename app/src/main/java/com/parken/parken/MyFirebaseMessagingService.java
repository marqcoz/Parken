package com.parken.parken;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification body: " + remoteMessage.getNotification().getBody());
        NotificationManager notificatioMng =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificatioMng.notify(
                12,
        createNotification(remoteMessage.getNotification().getBody()));

        if(remoteMessage.getNotification().getBody().equals("No llegaste a tiempo")){

            Intent dialogIntent = new Intent(getApplicationContext(), ParkenActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra("Activity", ParkenActivity.NOTIFICATIONS);
            dialogIntent.putExtra("ActivityStatus", ParkenActivity.NOTIFICATION_EP_BOOKED_CANCELED);
            startActivity(dialogIntent);
        }

        if(remoteMessage.getNotification().getBody().equals("TEST")){

            Intent dialogIntent = new Intent(getApplicationContext(), ParkenActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra("Activity", "TEST");
            dialogIntent.putExtra("ActivityStatus", ParkenActivity.NOTIFICATION_EP_BOOKED_CANCELED);
            startActivity(dialogIntent);
        }
    }

    // Create notification
    private Notification createNotification(String msg){ //, PendingIntent notificationPendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setColor(Color.RED)
                .setContentTitle(msg)
                .setContentText("Entrando a la geocerca")
                //.setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder.build();
    }

}