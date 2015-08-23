package com.vetcon.sendnow;

import android.app.Application;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.parse.Parse;
import com.parse.PushService;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.vetcon.sendnow.activities.SNMainActivity;

import io.fabric.sdk.android.Fabric;

public class MainApplicationStartup extends Application {

	private String TWITTER_KEY = "NO-KEY-FOR-YOU";
	private String TWITTER_SECRET = "ITS-A-SECRET";

	private AuthCallback authCallback;

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
		TwitterAuthConfig authConfig =  new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new TwitterCore(authConfig), new Digits());

		authCallback = new AuthCallback() {
			@Override
			public void success(DigitsSession session, String phoneNumber) {
				// Do something with the session
			}

			@Override
			public void failure(DigitsException exception) {
				// Do something on failure
			}
		};
	}
}
