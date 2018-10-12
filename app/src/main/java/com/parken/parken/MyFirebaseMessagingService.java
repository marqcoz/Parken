package com.parken.parken;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

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
        Log.d(TAG, "Notification data: " + remoteMessage.getData());
        Log.d(TAG, "Notification data: " + remoteMessage.getNotification());

        try {

            JSONObject data = new JSONObject(remoteMessage.getData());


            if(data.getString("datos").equals("OK")){

                int idNoti = Integer.valueOf(data.getString("idNotification"));
                switch (idNoti){

                    case ParkenActivity.NOTIFICATION_NEW_SPACE:

                        Notificacion.lanzar(getApplicationContext(), idNoti, "MAX", data.getString("espacioParken"));
                        break;

                    case ParkenActivity.NOTIFICATION_NEW_RECEIPT:

                        Notificacion.lanzar(getApplicationContext(), idNoti, "MAX", null);

                        break;

                    case ParkenActivity.NOTIFICATION_CAR_FREE:

                        Notificacion.lanzar(getApplicationContext(), idNoti, "DEFAULT", null);

                        break;

                    case ParkenActivity.NOTIFICATION_INFO:

                        Notificacion.lanzar(this, idNoti, "DEFAULT", remoteMessage.getNotification().getTitle() + "&"+ remoteMessage.getNotification().getBody());
                        break;

                        default:
                            break;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(remoteMessage.getNotification().getBody().equals("No llegaste a tiempo")){

            Intent dialogIntent = new Intent(getApplicationContext(), ParkenActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra("Activity", ParkenActivity.NOTIFICATIONS);
            dialogIntent.putExtra("ActivityStatus", ParkenActivity.NOTIFICATION_EP_BOOKED_CANCELED);
            startActivity(dialogIntent);
        }

        if(remoteMessage.getNotification().getBody().equals("TEST")){
            Notificacion.lanzar(getApplicationContext(), 3466, "MAX", null);
            //createNotification("test");


        }
    }

    // Create notification
    private Notification createNotification(String msg){ //, PendingIntent notificationPendingIntent) {

        Intent i = new Intent(getApplicationContext(),ParkenActivity.class);

        Intent dialogIntent = new Intent(getApplicationContext(), ParkenActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtra("Activity", "TEST");
        dialogIntent.putExtra("ActivityStatus", ParkenActivity.NOTIFICATION_ON_THE_WAY);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "holo");
        notificationBuilder
                .setSmallIcon(R.drawable.ic_parken_notification)
                .setColor(Color.BLACK)
                .setContentTitle(msg)
                .setContentText("Entrando a la geocerca")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_park, "Parken", pendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder.build();
    }

}