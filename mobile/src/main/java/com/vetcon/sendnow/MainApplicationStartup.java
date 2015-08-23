package com.vetcon.sendnow;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.parse.Parse;
import com.parse.PushService;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.vetcon.sendnow.activities.SNMainActivity;
import com.vetcon.sendnow.ui.toast.SNToast;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import io.fabric.sdk.android.Fabric;

public class MainApplicationStartup extends Application {

	/** CLASS VARIABLES ________________________________________________________________________ **/

	// LOGGING VARIABLES
	private static final String LOG_TAG = MainApplicationStartup.class.getSimpleName();

	// TWITTER DIGITS VARIABLES:
	private AuthCallback authCallback;
	private DigitsSession currentSession;
	private String TWITTER_KEY = "NO-KEY-FOR-YOU";
	private String TWITTER_SECRET = "ITS-A-SECRET";
	private TwitterAuthConfig authConfig;

	/** APPLICATION METHODS ____________________________________________________________________ **/

	@Override
	public void onCreate() { 
		super.onCreate();

		// PARSE INITIALIZATION:
	    Parse.initialize(this, "nTodQv8Ace13WVn1vIyNMin7dfjV7QZV8Wqj28D8",
				"n5I412Cd2A7MS2K4CdVeXhJSQBlHsTjXlhhdm7dg");
	    PushService.setDefaultPushCallback(this, SNMainActivity.class);

		// TWITTER DIGITS INITIALIZATION:
		setupDigits();
	}

	public AuthCallback getAuthCallback(){
		return authCallback;
	}

	/** TWITTER DIGITS METHODS _________________________________________________________________ **/

	private void setupDigits() {

		// Retrieves the TWITTER KEY and TWITTER SECRET from the secret XML.
		TWITTER_KEY = getString(R.string.twitter_key);
		TWITTER_SECRET = getString(R.string.twitter_secret);

		// Sets up the Twitter Digits configuration.
		authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new TwitterCore(authConfig), new Digits());

		// Handles the callback from the Twitter Digits button.
		authCallback = new AuthCallback() {

			@Override
			public void success(DigitsSession session, String phoneNumber) {

				Log.d(LOG_TAG, "setupDigits(): Twitter Digits session successful. Phone number is: " + phoneNumber);

				SNToast.toastyPopUp("Authentication Successful for " + phoneNumber, getApplicationContext());

				/*
				if (session.getAuthToken() instanceof TwitterAuthToken) {
					final TwitterAuthToken authToken = (TwitterAuthToken) session.getAuthToken();

					Log.d(LOG_TAG, "setupDigits(): AuthToken.token: " + authToken.token);
					Log.d(LOG_TAG, "setupDigits(): AuthToken.secret" + authToken.secret);
				}
				*/

				currentSession = session;

				// TWITTER DIGITS ASYNCTASK INITIALIZATION:
				//SNDigitsConnectionTask task = new SNDigitsConnectionTask();
				//task.execute(""); // Executes the AsyncTask.
			}

			@Override
			public void failure(DigitsException exception) {

				SNToast.toastyPopUp("ERROR: " + exception, getApplicationContext());
			}
		};
	}

	/** SUBCLASSES _____________________________________________________________________________ **/

	/**
	 * --------------------------------------------------------------------------------------------
	 * [SNDigitsConnectionTask] CLASS
	 * DESCRIPTION: This is an AsyncTask-based class that attempts to send the Twitter Digits
	 * authentication to the specified web-backend server.
	 * --------------------------------------------------------------------------------------------
	 */

	public class SNDigitsConnectionTask extends AsyncTask<String, Void, Void> {

		/** ASYNCTASK METHODS __________________________________________________________________ **/

		// onPreExecute(): This method runs on the UI thread just before the doInBackground method
		// executes.
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		// doInBackground(): This method constantly runs in the background while AsyncTask is
		// running.
		@Override
		protected Void doInBackground(final String... params) {

			// Retrieves the authorization token for the DigitsSession.
			TwitterAuthToken authToken = (TwitterAuthToken) currentSession.getAuthToken();
			DigitsOAuthSigning oauthSigning = new DigitsOAuthSigning(authConfig, authToken);

			// Generates authorization headers for a user session.
			Map<String, String> authHeaders = oauthSigning.getOAuthEchoHeadersForVerifyCredentials();

			URL url = null;

			try {
				url = new URL("http://api.yourbackend.com/verify_credentials.json");
			}

			catch (MalformedURLException e) {
				e.printStackTrace();
			}

			HttpsURLConnection connection = null;

			try {
				connection = (HttpsURLConnection) url.openConnection();
			}

			catch (IOException e) {
				e.printStackTrace();
			}

			try {
				connection.setRequestMethod("GET");
			}

			catch (ProtocolException e) {
				e.printStackTrace();
			}

			// Add OAuth Echo headers to request
			for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}

			// Perform request
			// TODO: Not resolving method.
			//connection.openConnection();

			return null;
		}

		// onPostExecute(): This method runs on the UI thread after the doInBackground operation has
		// completed.
		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
		}
	}
}
