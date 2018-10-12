package com.parken.parken;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    /**
     * Gets the action from the incoming broadcast intent and responds accordingly
     * @param context Context of the app when the broadcast is received.
     * @param intent The broadcast intent containing the action.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){

            case "TEST":
                //startActivity(new Intent(context,ParkenActivity.class)) ;

                break;
            case ParkenActivity.ACTION_NAVIGATE_ON_THE_WAY:
                //startActivity(new Intent(ParkenActivity.this,ParkenActivity.class)) ;
                //collapseStatusBar();
                //abrirGPSBrowser(latitudDestino, longitudDestino, nombreDestino);
                //abrirGPSBrowser(session.getLatDestino(), session.getLngDestino(), session.getNombreDestino());


                break;
            case ParkenActivity.ACTION_CANCEL_ON_THE_WAY:

                //startActivity(new Intent(getApplicationContext(),ParkenActivity.class).addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
                //collapseStatusBar();
                //parken();
                //cancelNotificationOnTheWay();
                //dialogConfirmEspacioParkenOut().show();
                //unregisterReceiver(mReceiver);
                //cancelarSolicitudEspacioParken();

                break;
        }
        return;

    }
}

