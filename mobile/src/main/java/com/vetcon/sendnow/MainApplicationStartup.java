package com.vetcon.sendnow;

import android.app.Application;

import com.parse.Parse;
import com.parse.PushService;

public class MainApplicationStartup extends Application {
	
	@Override
	public void onCreate() { 
		super.onCreate();
	    Parse.initialize(this, "nTodQv8Ace13WVn1vIyNMin7dfjV7QZV8Wqj28D8",
				               "n5I412Cd2A7MS2K4CdVeXhJSQBlHsTjXlhhdm7dg");
	    
	    PushService.setDefaultPushCallback(this, MainActivity.class);
	}
}
