package com.vetcon.sendnow.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.Simplify;
import com.vetcon.sendnow.R;
import com.vetcon.sendnow.ui.SNUnbind;
import java.lang.ref.WeakReference;
import butterknife.ButterKnife;

public class SNMainActivity extends AppCompatActivity implements Simplify.AndroidPayCallback {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private static final String LOG_TAG = SNMainActivity.class.getSimpleName();
    private Simplify mSimplify;
    private CardEditor mCardEditor;

    // ACTIVITY VARIABLES
    private static WeakReference<SNMainActivity> weakRefActivity = null; // Used to maintain a weak reference to the activity.

    /** ACTIVITY METHODS _______________________________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weakRefActivity = new WeakReference<SNMainActivity>(this); // Creates a weak reference of this activity.
        setupLayout(); // Sets up the layout for the activity.
    }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    // Calls recycleMemory() to free up memory allocation.
    @Override
    public void onDestroy() {
        recycleMemory(); // Recycles all View objects to free up memory resources.
        super.onDestroy();
    }

    /** ACTIVITY EXTENSION METHODS _____________________________________________________________ **/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sn_main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // to get back MaskedWallet using call back method.
        if (Simplify.handleAndroidPayResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void setupLayout() {

        setContentView(R.layout.sn_main_activity_layout);
        ButterKnife.bind(this); // ButterKnife view injection initialization.

    }


    /** SIMPLIFY METHODS _______________________________________________________________________ **/


    @Override
    public void onReceivedMaskedWallet(MaskedWallet maskedWallet) {

    }

    @Override
    public void onReceivedFullWallet(FullWallet fullWallet) {

    }

    @Override
    public void onAndroidPayCancelled() {

    }

    @Override
    public void onAndroidPayError(int i) {

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
