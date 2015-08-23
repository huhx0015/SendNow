package com.vetcon.sendnow.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import com.vetcon.sendnow.R;
import com.vetcon.sendnow.ui.layout.SNUnbind;

/**
 * Created by Michael Yoon Huh on 8/2/2015.
 */
public class SNVoiceActivity extends Activity {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SPEECH VARIABLES
    private TextToSpeech speechInstance; // Used to reference the TTS instance for the class.

    /** LIFECYCLE METHODS ______________________________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only
    // runs when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpLayout(); // Sets up the layout for the activity.

        startSpeech("Someone needs your help!", this); // Starts the TTS speech alert.

        // Creates a new timer thread for temporarily pausing the app for the TTS speech
        // to process.
        Thread timer = new Thread() {

            public void run() {
                try { sleep(10000); } // Time to sleep in milliseconds.
                catch (InterruptedException e) { e.printStackTrace(); } // Prints error code.
                finally { launchMainIntent();  } // Launches the next activity.
            }
        };

        timer.start(); // Starts the thread.
    }

    // onStop(): This function runs when screen is no longer visible and the activity is in a
    // state prior to destruction.
    @Override
    protected void onStop() {
        super.onStop();
        finish(); // The activity is terminated at this point.
    }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    // Calls recycleMemory() to free up memory allocation.
    @Override
    protected void onDestroy() {

        super.onDestroy();

        // Recycles all View objects to free up memory resources.
        SNUnbind.unbindDrawables(findViewById(R.id.sn_voice_activity_layout));
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the activity.
    private void setUpLayout() {
        setContentView(R.layout.sn_voice_activity_layout);
    }

    /** NARRATION METHODS ______________________________________________________________________ **/

    // startSpeech(): Activates voice functionality to say something.
    private void startSpeech(final String script, Context context) {

        // Creates a new instance to begin TextToSpeech functionality.
        speechInstance = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                speechInstance.speak(script, TextToSpeech.QUEUE_FLUSH, null); // The script is spoken by the TTS system.
            }
        });
    }

    /** INTENT METHODS _________________________________________________________________________ **/

    // launchMainIntent(): Launches a Intent to return to the SNMainActivity.
    private void launchMainIntent() {
        Intent intent = new Intent(this, SNMainActivity.class);
        startActivity(intent);
    }
}
