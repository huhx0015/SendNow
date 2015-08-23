package com.vetcon.sendnow.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.digits.sdk.android.DigitsAuthButton;
import com.vetcon.sendnow.application.SNApplication;
import com.vetcon.sendnow.R;
import com.vetcon.sendnow.interfaces.OnTwitterDigitListener;
import com.vetcon.sendnow.preferences.SNPreferences;
import com.vetcon.sendnow.ui.layout.SNUnbind;
import com.vetcon.sendnow.ui.toast.SNToast;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */
public class SNLoginActivity extends AppCompatActivity implements OnTwitterDigitListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // ACTIVITY VARIABLES
    private Boolean isFinished = false; // Used to determine if this activity can be finished or not.

    // PREFERENCE VARIABLES
    private Boolean isDigitRegistered = false; // Used to determine if the user has been registered via Twitter Digits.
    private SharedPreferences SN_prefs; // Main SharedPreferences objects that store settings for the application.
    private static final String SN_OPTIONS = "sn_options"; // Used to reference the name of the preference XML file.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.sn_username_field) EditText usernameField;
    @Bind(R.id.sn_password_field) EditText passwordField;
    @Bind(R.id.sn_login_button) Button loginButton;
    @Bind(R.id.sn_real_login_button) Button realLoginButton;
    @Bind(R.id.sn_signup_btn) Button signUpButton;
    @Bind(R.id.sn_login_buttons_container) LinearLayout loginButtonContainer;
    @Bind(R.id.sn_login_field_container) LinearLayout loginFieldContainer;
    @Bind(R.id.sn_skip_text) TextView skipText;

    /** ACTIVITY METHODS _______________________________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // LAYOUT INITIALIZATION:
        setContentView(R.layout.sn_login_activity_layout);
        ButterKnife.bind(this); // ButterKnife view injection initialization.

        setupButtons(); // Sets up the button listeners for the activity.
        loadPreferences(); // Loads SharedPreference values.
    }

    // onPause(): This method runs when the activity is suspended.
    @Override
    protected void onPause() {
        super.onPause();

        if (isFinished) { finish(); } // Finishes the activity.
    }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    // Calls recycleMemory() to free up memory allocation.
    @Override
    public void onDestroy() {
        recycleMemory(); // Recycles all View objects to free up memory resources.
        super.onDestroy();
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void setupButtons() {

        // TWITTER DIGITS AUTH BUTTON:
        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.sn_digits_button);
        digitsButton.setCallback(((SNApplication) getApplication()).getAuthCallback(this));
        digitsButton.setAuthTheme(android.R.style.Theme_Material);

        // LOGIN BUTTON: Handles the display of the login field container.
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginButtonContainer.setVisibility(View.GONE); // Hides the login button container.
                loginFieldContainer.setVisibility(View.VISIBLE); // Displays the login field container.
            }
        });

        // REAL LOGIN BUTTON: Processes the username and password fields for login.
        realLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SNToast.toastyPopUp("Wrong user name or password. Please verify and try again.", SNLoginActivity.this);
            }
        });

        // SIGNUP BUTTON:
        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SNToast.toastyPopUp("Signup has been disabled for this demonstration.", SNLoginActivity.this);
            }
        });

        // SKIP TEXT:
        skipText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchIntent();
            }
        });
    }

    private void launchIntent() {

        isFinished = true; // Indicates that this activity is ready to be finished.

        // Creates an intent to the SNMainActivity.
        Intent i = new Intent("com.vetcon.sendnow.MAINACTIVITY");
        startActivityForResult(i, 0); // Launches the activity class.
    }

    /** PREFERENCES FUNCTIONALITY ______________________________________________________________ **/

    // loadPreferences(): Loads the SharedPreference values from the stored SharedPreferences object.
    private void loadPreferences() {

        // Initializes the SharedPreferences object.
        SN_prefs = SNPreferences.initializePreferences(SN_OPTIONS, this);
        isDigitRegistered = SNPreferences.getDigitRegistration(SN_prefs);

        // If the user has already registered for Twitter Digits / Parse, the user is logged in
        // automatically.
        if (isDigitRegistered) {
            launchIntent();
        }
    }

    /** RECYCLE METHODS ________________________________________________________________________ **/

    // recycleMemory(): Recycles all View objects to clear up memory resources prior to Activity
    // destruction.
    private void recycleMemory() {

        // Unbinds all Drawable objects attached to the current layout.
        try { SNUnbind.unbindDrawables(findViewById(R.id.sn_login_activity_layout)); }
        catch (NullPointerException e) { e.printStackTrace(); } // Prints error message.
    }

    /** INTERFACE METHODS ______________________________________________________________________ **/

    @Override
    public void processLogin(Boolean isSuccess) {

        // If Twitter Digits & Parse registration was successful, an intent to the SNMainActivity
        // class is launched.
        if (isSuccess) {
            launchIntent();
        }

        // Displays an error message.
        else {
            SNToast.toastyPopUp("Registration failure. Please try again.", this);
        }
    }
}
