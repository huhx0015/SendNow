package com.vetcon.sendnow.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.vetcon.sendnow.R;
import com.vetcon.sendnow.ui.SNUnbind;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */
public class SNLoginActivity extends AppCompatActivity {

    // VIEW INJECTION VARIABLES
    @Bind(R.id.sn_username_field) EditText usernameField;
    @Bind(R.id.sn_password_field) EditText passwordField;
    @Bind(R.id.sn_login_button) Button loginButton;
    @Bind(R.id.sn_signup_btn) Button signUpButton;

    /** ACTIVITY METHODS _______________________________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout();
        setupButtons();
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

            }
        });

        // SIGNUP BUTTON:
        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

    }

    /** RECYCLE METHODS ________________________________________________________________________ **/

    // recycleMemory(): Recycles all View objects to clear up memory resources prior to Activity
    // destruction.
    private void recycleMemory() {

        // Unbinds all Drawable objects attached to the current layout.
        try { SNUnbind.unbindDrawables(findViewById(R.id.sn_main_activity_layout)); }
        catch (NullPointerException e) { e.printStackTrace(); } // Prints error message.
    }
}
