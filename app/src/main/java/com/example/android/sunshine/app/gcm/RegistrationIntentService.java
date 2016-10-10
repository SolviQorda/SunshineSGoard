package com.example.android.sunshine.app.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by sorengoard on 10/10/16.
 */
public class RegistrationIntentService extends IntentService{

    public static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                sendRegistrationToServer(token);

                prefs.edit().putBoolean(MainActivity.SENT_TOKEN_TO_SERVER, true).apply();
            }
        } catch (Exception e){
            Log.d(TAG, "Failed to complete token refresh", e);
            prefs.edit().putBoolean(MainActivity.SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void sendRegistrationToServer(String token){
        Log.i(TAG, "GCM registration token: " + token);
    }
}
