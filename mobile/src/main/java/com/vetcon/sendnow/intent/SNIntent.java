package com.vetcon.sendnow.intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Michael Yoon Huh on 8/23/2015.
 */
public class SNIntent {

    // launchWebIntent(): Launches an intent to a web browser to display a webpage.
    public static void launchWebIntent(String url, Context con) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        con.startActivity(i);
    }
}
