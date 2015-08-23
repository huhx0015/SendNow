package com.vetcon.sendnow.ui.toast;

import android.content.Context;
import android.widget.Toast;

/** -----------------------------------------------------------------------------------------------
 *  [SNToast] CLASS
 *  PROGRAMMER: Michael Yoon Huh (Huh X0015)
 *  DESCRIPTION: SNToast contains functions that utilize the Toast message functionality.
 *  -----------------------------------------------------------------------------------------------
 */

public class SNToast {

    /** TOAST FUNCTIONALITY ____________________________________________________________________ **/

    // toastyPopUp(): Creates and displays a Toast popup.
    public static void toastyPopUp(String message, Context con) {
        Toast.makeText(con, message, Toast.LENGTH_SHORT).show();
    }
}
