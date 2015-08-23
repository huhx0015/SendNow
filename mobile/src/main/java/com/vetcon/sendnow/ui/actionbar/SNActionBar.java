package com.vetcon.sendnow.ui.actionbar;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.vetcon.sendnow.R;

/** -----------------------------------------------------------------------------------------------
 *  [SNActionBar] CLASS
 *  PROGRAMMER: Michael Yoon Huh (Huh X0015)
 *  DESCRIPTION: SNActionBar class contains methods for modifying the actionbar properties.
 *  -----------------------------------------------------------------------------------------------
 */

public class SNActionBar {

    // setupActionBar(): Sets up the action bar attributes for the activity.
    public static void setupActionBar(String actionType, String subtitle,
                                      AppCompatActivity activity) {

        ActionBar actionBar = activity.getSupportActionBar(); // References the action bar.

        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name); // Sets the title of the action bar.
            actionBar.setSubtitle(null); // Disables the subtitles of the action bar.
            actionBar.setDisplayHomeAsUpEnabled(false); // Disables the back button in the action bar.
        }
    }
}
