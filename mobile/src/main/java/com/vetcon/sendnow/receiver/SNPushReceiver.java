package com.vetcon.sendnow.receiver;

import android.content.Context;
import android.content.Intent;
import com.parse.ParsePushBroadcastReceiver;

public class SNPushReceiver extends ParsePushBroadcastReceiver {

    /** PUSH RECEIVER METHODS __________________________________________________________________ **/

    @Override
    protected void onPushReceive(Context mContext, Intent intent) {
        super.onPushReceive(mContext, intent);

        Intent i = new Intent("com.vetcon.sendnow.RECEIVEACTIVITY");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }
}