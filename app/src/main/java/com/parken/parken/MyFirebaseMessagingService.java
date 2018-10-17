package com.parken.parken;
import android.content.Intent;
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
  //      Log.d(TAG, "Notification body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification data: " + remoteMessage.getData());
//        Log.d(TAG, "Notification data: " + remoteMessage.getNotification());

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

                        Notificacion.lanzar(this, idNoti, "DEFAULT", "Notificacion" + "&"+ "Ejemplo");
                        break;

                        default:
                            break;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}