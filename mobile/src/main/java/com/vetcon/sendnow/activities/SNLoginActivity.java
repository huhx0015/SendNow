package com.vetcon.sendnow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.vetcon.sendnow.R;
import com.vetcon.sendnow.ui.layout.SNUnbind;
import com.vetcon.sendnow.ui.toast.SNToast;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */
public class SNLoginActivity extends AppCompatActivity {

    private String TWITTER_KEY = "NO-KEY-FOR-YOU";
    private String TWITTER_SECRET = "ITS-A-SECRET";

    // ACTIVITY VARIABLES
    private Boolean isFinished = false; // Used to determine if this activity can be finished or not.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.sn_username_field) EditText usernameField;
    @Bind(R.id.sn_password_field) EditText passwordField;
    @Bind(R.id.sn_login_button) Button loginButton;
    @Bind(R.id.sn_signup_btn) Button signUpButton;
    @Bind(R.id.sn_skip_text) TextView skipText;

    /** ACTIVITY METHODS _______________________________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDigits(); // Sets up the Twitter Digits authentication.

        setupLayout();
        setupButtons();
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

    private void setupLayout() {
        setContentView(R.layout.sn_login_activity_layout);
        ButterKnife.bind(this); // ButterKnife view injection initialization.
    }

    private void setupButtons() {

        // LOGIN BUTTON:
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SNToast.toastyPopUp("LOGIN BUTTON disabled.", SNLoginActivity.this);
            }
        });

        // SIGNUP BUTTON:
        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SNToast.toastyPopUp("SIGNUP BUTTON disabled.", SNLoginActivity.this);
            }
        });

        // SKIP TEXT:
        skipText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                isFinished = true; // Indicates that this activity is ready to be finished.

                // Creates an intent to the SNMainActivity.
                Intent i = new Intent("com.vetcon.sendnow.MAINACTIVITY");
                startActivityForResult(i, 0); // Launches the activity class.
            }
        });
    }

    /** TWITTER DIGITS METHODS _________________________________________________________________ **/

    private void setupDigits() {

        // Retrieves the TWITTER KEY and TWITTER SECRET from the secret XML.
        TWITTER_KEY = getString(R.string.twitter_key);
        TWITTER_SECRET = getString(R.string.twitter_secret);

        // Sets up the Twitter Digits configuration.
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
    }

    /** RECYCLE METHODS ________________________________________________________________________ **/

    // recycleMemory(): Recycles all View objects to clear up memory resources prior to Activity
    // destruction.
    private void recycleMemory() {

        // Unbinds all Drawable objects attached to the current layout.
        try { SNUnbind.unbindDrawables(findViewById(R.id.sn_login_activity_layout)); }
        catch (NullPointerException e) { e.printStackTrace(); } // Prints error message.
    }
}
