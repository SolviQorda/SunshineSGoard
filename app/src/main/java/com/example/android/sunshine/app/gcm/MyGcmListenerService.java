package com.example.android.sunshine.app.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sorengoard on 10/10/16.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    private static final String EXTRA_DATA = "data";
    private static final String EXTRA_WEATHER = "weather";
    private static final String EXTRA_LOCATION = "location";

    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        //unparcelling the bundle

        if (!data.isEmpty()) {
            String senderId = getString(R.string.gcm_defaultSenderId);
            if(senderId.length() == 0) {
                Toast.makeText(this, "senderID string needsd to be set", Toast.LENGTH_LONG).show();
            }
            //check message is coming from your server
            if((senderId).equals(from)) {
                //process message and then post a notification of the received message
                try {
                    JSONObject jsonObject = new JSONObject(data.getString(EXTRA_DATA));
                    String weather = jsonObject.getString(EXTRA_WEATHER);
                    String location = jsonObject.getString(EXTRA_LOCATION);
                    String alert =
                            String.format(getString(R.string.gcm_weather_alert), weather, location);
                    sendNotification(alert);
                } catch(JSONException e){
                    //json parszing failed, so we let this message go - gcm ain't a critical feature
                }
            }
            Log.i(TAG, "received: " + data.toString());
        }
    }
    //puts the message into a notification and posts it
    private void sendNotification(String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        //need to create a bmp from resource id
        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.art_storm);
        NotificationCompat.Builder bobTheBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.art_clear)
                .setLargeIcon(largeIcon)
                .setContentTitle("HOLD UP! Weather Alert!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        bobTheBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, bobTheBuilder.build());
    }
}
