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
                    Log.e( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );


                    if( activity.getConfidence() >= 75 ) {
                        //changeMovement(DetectedActivity.IN_VEHICLE);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "holo");
                        builder.setContentText( "Are you in vehicle?" );
                        builder.setSmallIcon( R.drawable.ic_parken_notification );
                        builder.setContentTitle( getString( R.string.app_name ) );
                        NotificationManagerCompat.from(this).notify(0, builder.build());}
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.e( "ActivityRecogition", "On Bicycle: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "holo");
                        builder.setContentText( "Are you on bike?" );
                        builder.setSmallIcon( R.mipmap.ic_launcher );
                        builder.setContentTitle( getString( R.string.app_name ) );
                        NotificationManagerCompat.from(this).notify(0, builder.build());}
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.e( "ActivityRecogition", "On Foot: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.IN_VEHICLE);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "holo");
                        builder.setContentText( "Are you parado?" );
                        builder.setSmallIcon( R.mipmap.ic_launcher );
                        builder.setContentTitle( getString( R.string.app_name ) );
                        NotificationManagerCompat.from(this).notify(0, builder.build());}
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.IN_VEHICLE);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"holo");
                        builder.setContentText( "Are you stilling?" );
                        builder.setSmallIcon( R.drawable.ic_parken_notification);
                        builder.setColor(Color.BLACK);
                        builder.setContentTitle( getString( R.string.app_name ) );
                        NotificationManagerCompat.from(this).notify(0, builder.build());}
                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.e( "ActivityRecogition", "Tilting: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );

                    if( activity.getConfidence() >= 75 ) {
                        changeMovement(DetectedActivity.IN_VEHICLE);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "holo");
                        builder.setContentText( "Are you walking?" );
                        builder.setSmallIcon( R.mipmap.ic_launcher );
                        builder.setContentTitle( getString( R.string.app_name ) );
                        NotificationManagerCompat.from(this).notify(0, builder.build());
                    }

                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
                    break;
                }
            }
        }
    }

    private void changeMovement(int estatus){

        /*
        Intent dialogIntent = new Intent(getApplicationContext(), ParkenActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        dialogIntent.putExtra("Activity", ParkenActivity.MOVEMENTS);
        dialogIntent.putExtra("ActivityStatus", estatus);
        startActivity(dialogIntent);
        */
        //Se modificará DRIVING a TRUE
        session.setDriving(true);
    }
}
