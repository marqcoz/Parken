package com.parken.parken;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

public class ActivityRecognizedService extends IntentService {

    private ShPref session = new ShPref(ParkenActivity.activityParken);

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {

        for( DetectedActivity activity : probableActivities ) {

            Log.e( "ActivityRecogition", "Actividad: " + activity.getType() );

            switch( activity.getType() ) {

                case DetectedActivity.IN_VEHICLE: {

                    Log.e("ActivityRecogition", "In Vehicle: " + activity.getConfidence());

                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.IN_VEHICLE);
                    }
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.e( "ActivityRecogition", "On Bicycle: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.ON_BICYCLE);
                    }
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.e( "ActivityRecogition", "On Foot: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.ON_FOOT);
                    }
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.RUNNING);
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.STILL);
                    }
                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.e( "ActivityRecogition", "Tilting: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.TILTING);
                    }
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.WALKING);
                    }
                    break;
                }

                case DetectedActivity.UNKNOWN: {
                    Log.e( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.UNKNOWN);
                    }
                    break;
                }
            }
        }
    }

    private void changeMovement(int act){

        if(act == DetectedActivity.STILL || act == DetectedActivity.WALKING || act == DetectedActivity.IN_VEHICLE){
            session.setDriving(true);
            Log.e( "ActivityRecogition", "Actividad: " + act + ". DRIVE ON");
            //Notificacion.lanzar(getApplicationContext(),
              //      ParkenActivity.NOTIFICATION_INFO,
                //    "DEFAULT", "DetectedActivity&Actividad: STILL");
        }
    }
}
