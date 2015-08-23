package com.vetcon.sendnow.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

/**
 * Created by Michael Yoon Huh on 8/23/2015.
 */

public class MyCustomReceiver extends BroadcastReceiver {

    /** CLASS VARIABLES __________________________________________________________________________ **/

    // LOGGING VARIABLES
    private static final String LOG_TAG = "MyCustomReceiver";

    /** BROADCAST RECEIVER METHODS _______________________________________________________________ **/

    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            String action = intent.getAction();

            if (action.equals("com.vetcon.sendnow.UPDATE_MESSAGES")) {
                Intent newMessage = new Intent("updateMessages");
                LocalBroadcastManager.getInstance(context).sendBroadcast(newMessage);
            }

            else if (action.equals("com.vetcon.sendnow.UPDATE_REQUESTS")) {
                Intent newRequest = new Intent("updateRequests");
                LocalBroadcastManager.getInstance(context).sendBroadcast(newRequest);
            }

            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.d(LOG_TAG, "got action " + action + " on channel " + channel + " with:");
            @SuppressWarnings("rawtypes")
            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                Log.d(LOG_TAG, "..." + key + " => " + json.getString(key));
            }
        }

        catch (JSONException e) {
            Log.d(LOG_TAG, "JSONException: " + e.getMessage());
        }
    }
}
