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

    // launchTwitterIntent(): Launches Twitter.
    public static void launchTwitterIntent(Context con) {

        Intent intent = null;

        // Launches the Android version of Twitter.
        try {

            // Launches an Intent to Twitter app.
            con.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=SendNowApp"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        // If no Twitter app, the browser is launched.
        catch (Exception e) {

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/SendNowApp"));
        }
        con.startActivity(intent);
    }

    // launchDialerIntent(): Launches the Dialer.
    public static void launchDialerIntent(String telephone, Context con) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(telephone));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        con.startActivity(intent);
    }

    // launchEmailIntent(): Launches intent to email app.
    public static void launchEmailIntent(String address, String subject, Context con) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Uri.encode(address)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        con.startActivity(Intent.createChooser(emailIntent, "Send email with: "));
    }
}
