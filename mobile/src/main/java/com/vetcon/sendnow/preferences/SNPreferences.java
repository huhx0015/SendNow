package com.vetcon.sendnow.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.vetcon.sendnow.R;

/** ------------------------------------------------------------------------------------------------
 *  [SNPreferences] CLASS
 *  PROGRAMMER: Michael Yoon Huh (HUHX0015)
 *  DESCRIPTION: This class contains functionality that pertains to the use and manipulation of
 *  shared preferences data.
 *  ------------------------------------------------------------------------------------------------
 */
public class SNPreferences {

    /** SHARED PREFERENCES FUNCTIONALITY _______________________________________________________ **/

    // getPreferenceResource(): Selects the appropriate resource based on the shared preference type.
    private static int getPreferenceResource() {
        return R.xml.sn_options;
    }

    // initializePreferences(): Initializes and returns the SharedPreferences object.
    public static SharedPreferences initializePreferences(String prefType, Context context) {
        return context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
    }

    // setDefaultPreferences(): Sets the shared preference values to default values.
    public static void setDefaultPreferences(String prefType, Boolean isReset, Context context) {

        // Determines the appropriate resource file to use.
        int prefResource = getPreferenceResource();

        // Resets the preference values to default values.
        if (isReset) {
            SharedPreferences preferences = initializePreferences(prefType, context);
            preferences.edit().clear().apply();
        }

        // Sets the default values for the SharedPreferences object.
        PreferenceManager.setDefaultValues(context, prefType, Context.MODE_PRIVATE, prefResource, true);
    }

    /** GET PREFERENCES FUNCTIONALITY __________________________________________________________ **/

    // getDigitRegistration(): Retrieves the "sn_digits_registration" value from preferences.
    public static Boolean getDigitRegistration(SharedPreferences preferences) {
        return preferences.getBoolean("sn_digits_registration", false);
    }

    /** SET PREFERENCES FUNCTIONALITY __________________________________________________________ **/

    // setDigitRegistration(): Sets the "sn_digits_registration" value to preferences.
    public static void setDigitRegistration(Boolean isRegistered, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putBoolean("sn_digits_registration", isRegistered);
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }
}