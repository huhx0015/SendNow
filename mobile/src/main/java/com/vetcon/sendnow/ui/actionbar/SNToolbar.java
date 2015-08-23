package com.vetcon.sendnow.ui.actionbar;

import android.support.v7.widget.Toolbar;

/** -----------------------------------------------------------------------------------------------
 *  [SNToolbar] CLASS
 *  PROGRAMMER: Michael Yoon Huh (Huh X0015)
 *  DESCRIPTION: SNToolbar class contains methods for modifying the Toolbar properties.
 *  -----------------------------------------------------------------------------------------------
 */

public class SNToolbar {

    // updateToolBar(): Updates the properties of the Toolbar.
    public static void updateToolbar(Toolbar bar, String title) {

        if (bar != null) {
            bar.setTitle(title); // Sets the title of the Toolbar.
        }
    }
}
