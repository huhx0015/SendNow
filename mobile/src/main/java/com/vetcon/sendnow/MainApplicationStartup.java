package com.vetcon.sendnow;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.PushService;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.vetcon.sendnow.activities.SNLoginActivity;
import com.vetcon.sendnow.activities.SNMainActivity;
import com.vetcon.sendnow.interfaces.OnFragmentUpdateListener;
import com.vetcon.sendnow.interfaces.OnTwitterDigitListener;
import com.vetcon.sendnow.ui.toast.SNToast;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import io.fabric.sdk.android.Fabric;

public class MainApplicationStartup extends Application {

	/** CLASS VARIABLES ________________________________________________________________________ **/

	// ACTIVITY VARIABLES
	private SNLoginActivity loginActivity; // Referernces the login activity.

	// LOGGING VARIABLES
	private static final String LOG_TAG = MainApplicationStartup.class.getSimpleName();

	// TWITTER DIGITS VARIABLES:
	private AuthCallback authCallback;
	private String TWITTER_KEY = "NO-KEY-FOR-YOU";
	private String TWITTER_SECRET = "ITS-A-SECRET";

	/** APPLICATION METHODS ____________________________________________________________________ **/

	@Override
	public void onCreate() { 
		super.onCreate();

		// PARSE INITIALIZATION:
	    Parse.initialize(this,
				"nTodQv8Ace13WVn1vIyNMin7dfjV7QZV8Wqj28D8",
				"n5I412Cd2A7MS2K4CdVeXhJSQBlHsTjXlhhdm7dg");

	    PushService.setDefaultPushCallback(this, SNMainActivity.class);

		// TWITTER DIGITS INITIALIZATION:
		setupDigits();
	}

	public AuthCallback getAuthCallback(SNLoginActivity activity){

		this.loginActivity = activity;
		return authCallback;
	}

	/** TWITTER DIGITS METHODS _________________________________________________________________ **/

	private void setupDigits() {

		// Retrieves the TWITTER KEY and TWITTER SECRET from the secret XML.
		TWITTER_KEY = getString(R.string.twitter_key);
		TWITTER_SECRET = getString(R.string.twitter_secret);

		// Sets up the Twitter Digits configuration.
		final TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new TwitterCore(authConfig), new Digits());

		// Handles the callback from the Twitter Digits button.
		authCallback = new AuthCallback() {

			@Override
			public void success(DigitsSession session, String phoneNumber) {

				Log.d(LOG_TAG, "setupDigits(): Twitter Digits session successful. Phone number is: " + phoneNumber);
				SNToast.toastyPopUp("Authentication Successful for " + phoneNumber, getApplicationContext());

				// Retrieves the authorization token for the DigitsSession.
				TwitterAuthToken authToken = (TwitterAuthToken) session.getAuthToken();
				DigitsOAuthSigning oauthSigning = new DigitsOAuthSigning(authConfig, authToken);

				// Generates authorization headers for a user session.
				Map<String, Object> authHeaders = new HashMap<String, Object>();
				authHeaders.put("token", session.getAuthToken().toString());
				authHeaders.put("userId", "" + session.getId());

				Log.d(LOG_TAG, "setupDigits(): Token: " + session.getAuthToken().toString());
				Log.d(LOG_TAG, "setupDigits(): UserID: " + "" + session.getId());

				ParseCloud.callFunctionInBackground("digitsLogin", authHeaders, new FunctionCallback<String>() {

					@Override
					public void done(String sessionToken, ParseException e) {
						Log.d(LOG_TAG, "sessionToken: " + sessionToken);
						Log.d(LOG_TAG, "Exception: " + e);
						Log.d(LOG_TAG, "setupDigits(): DONE!");

						if (sessionToken != null) {
							registerParseUser(sessionToken); // Registers the user onto Parse.
						}
					}
				});
			}

			@Override
			public void failure(DigitsException exception) {
				SNToast.toastyPopUp("ERROR: " + exception, getApplicationContext());
			}
		};
	}

	/** PARSE METHODS __________________________________________________________________________ **/

	private void registerParseUser(String sessionToken) {

		ParseUser.becomeInBackground(sessionToken, new LogInCallback() {

			public void done(ParseUser user, ParseException e) {

				Log.d(LOG_TAG, "registerParseUser(): User: " + user + " | Exception: " + e);

				if (user != null) {
					Log.d(LOG_TAG, "registerParseUser(): SUCCESS!");
					processLogin(true);
				}

				else {
					Log.d(LOG_TAG, "registerParseUser(): FAILURE!");
					processLogin(false);
				}
			}
		});
	}

	/** INTERFACE METHODS ______________________________________________________________________ **/

	// processLogin(): Launches an intent or displays an error message if registration was
	// successful.
	private void processLogin(Boolean isSuccess) {
		try { ((OnTwitterDigitListener) loginActivity).processLogin(isSuccess); }
		catch (ClassCastException cce) {} // Catch for class cast exception errors.
	}
}
