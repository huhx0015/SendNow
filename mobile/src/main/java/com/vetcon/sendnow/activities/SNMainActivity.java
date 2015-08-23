package com.vetcon.sendnow.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.simplify.android.sdk.Simplify;
import com.vetcon.sendnow.FileHelper;
import com.vetcon.sendnow.InboxFragment;
import com.vetcon.sendnow.ParseConstants;
import com.vetcon.sendnow.QustomDialogBuilder;
import com.vetcon.sendnow.R;
import com.vetcon.sendnow.data.SNTwitterUserModel;
import com.vetcon.sendnow.fragments.SNCalculateFragment;
import com.vetcon.sendnow.fragments.SNMapFragment;
import com.vetcon.sendnow.fragments.SNPayFragment;
import com.vetcon.sendnow.fragments.SNProfileFragment;
import com.vetcon.sendnow.interfaces.OnFragmentUpdateListener;
import com.vetcon.sendnow.ui.actionbar.SNToolbar;
import com.vetcon.sendnow.ui.fragments.SNFragmentView;
import com.vetcon.sendnow.ui.layout.SNUnbind;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SNMainActivity extends AppCompatActivity implements OnFragmentUpdateListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // ACTIVITY VARIABLES
    private static WeakReference<AppCompatActivity> weakRefActivity = null; // Used to maintain a weak reference to the activity.

    // FRAGMENT VARIABLES
    private String currentFragment = ""; // Used to determine which fragment is currently active.

    // PROFILE VARIABLES
    private SNTwitterUserModel userProfile; // References the Twitter user's profile data model.

    // LAYOUT VARIABLES
    private ActionBarDrawerToggle drawerToggle; // References the toolbar drawer toggle button.

    // LOGGING VARIABLES
    private static final String LOG_TAG = SNMainActivity.class.getSimpleName();

    // VIEW INJECTION VARIABLES
    @Bind(R.id.sn_fragment_container) FrameLayout fragmentContainer;
    @Bind(R.id.sn_action_button) FloatingActionButton actionButton;
    @Bind(R.id.sn_drawer_background_image) ImageView drawerProfile;
    @Bind(R.id.sn_drawer_row_1) LinearLayout drawerHomeButton;
    @Bind(R.id.sn_drawer_row_2) LinearLayout drawerWalletButton;
    @Bind(R.id.sn_drawer_row_3) LinearLayout drawerDocumentsButton;
    @Bind(R.id.sn_drawer_row_4) LinearLayout drawerFilesButton;
    @Bind(R.id.sn_drawer_row_5) LinearLayout drawerSearchButton;
    @Bind(R.id.sn_drawer_row_6) LinearLayout drawerNotificationsButton;
    @Bind(R.id.sn_drawer_row_7) LinearLayout drawerPendingButton;
    @Bind(R.id.sn_drawer_row_8) LinearLayout drawerAccountButton;
    @Bind(R.id.sn_toolbar) Toolbar sn_main_toolbar;

    /** ACTIVITY METHODS _______________________________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Not working
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        weakRefActivity = new WeakReference<AppCompatActivity>(this); // Creates a weak reference of this activity.

        // LAYOUT INITIALIZATION:
        setContentView(R.layout.sn_main_activity_layout);
        ButterKnife.bind(this); // ButterKnife view injection initialization.

        setupParse();

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

    // onConfigurationChanged(): If the screen orientation changes, this function loads the proper
    // layout, as well as updating all layout-related objects.
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        setupLayout(); // Updates the layout for the activity.

        // Drawer toggle status is updated when the screen orientation changes.
        drawerToggle.onConfigurationChanged(newConfig);
    }

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
        } else if (handleOnActivityResultCamera(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // onPostCreate(): Synchronizes the toggle state after onRestoreInstanceState() has occurred.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /** PHYSICAL BUTTON FUNCTIONALITY __________________________________________________________ **/

    // BACK KEY:
    // onBackPressed(): Defines the action to take when the physical back button key is pressed.
    @Override
    public void onBackPressed() {

        // If current fragment view is a fragment other than SNProfileFragment, the focus is
        // returned to the SNProfileFragment.
        if ( !(currentFragment.equals("PROFILE")) ) {
            removeFragment();
            SNToolbar.updateToolbar(sn_main_toolbar, getString(R.string.app_name));
            actionButton.setVisibility(View.INVISIBLE); // Hides the floating action button.
        }

        else { finish(); }  // Finishes the activity.
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void setupLayout() {
        setupButtons(); // Sets up the button listeners for the activity.
        setupFragment(); // Sets up the fragment view for the activity.
        setUpToolbar(); // Sets up the toolbar for the activity.
    }

    private void setupButtons() {

        // DRAWER HOME BUTTON:
        drawerHomeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                displayFragment("PROFILE", 0);
            }
        });

        // DRAWER WALLET BUTTON:
        drawerWalletButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //displayFragment("WALLET");
            }
        });

        // DRAWER DOCUMENTS BUTTON:
        drawerDocumentsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //displayFragment("DOCUMENTS");
            }
        });

        // DRAWER FILES BUTTON:
        drawerFilesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //displayFragment("FILES");
            }
        });

        // DRAWER SEARCH BUTTON:
        drawerSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //displayFragment("SEARCH");
            }
        });

        // DRAWER NOTIFICATIONS BUTTON:
        drawerNotificationsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //displayFragment("NOTIFICATIONS");
            }
        });

        // DRAWER PENDING BUTTON:
        drawerPendingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //displayFragment("PENDING");
            }
        });

        // DRAWER ACCOUNT BUTTON:
        drawerAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //displayFragment("ACCOUNT");
            }
        });

        // FLOATING ACTION BUTTON:
        actionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                handleSelectPicture();
            }
        });
    }

    /** FRAGMENT METHODS _______________________________________________________________________ **/

    // changeFragment(): Changes the fragment views.
    private void changeFragment(Fragment frag, String fragToAdd, Boolean isAnimated) {

        Log.d(LOG_TAG, "changeFragment(): Fragment changed.");

        // Adds the fragment to the primary fragment container.
        SNFragmentView.addFragment(frag, fragmentContainer, R.id.sn_fragment_container, fragToAdd, isAnimated, weakRefActivity);

        currentFragment = fragToAdd; // Sets the current active fragment.
    }

    // setupFragment(): Initializes the fragment view for the layout.
    private void setupFragment() {

        SNProfileFragment fragment = new SNProfileFragment(); // Initializes the SNProfileFragment class.

        // TODO: Uncomment this when the Twitter Digits feature is ready.
        //fragment.initializeFragment(userProfile);

        SNFragmentView.addFragment(fragment, fragmentContainer, R.id.sn_fragment_container, "PROFILE", false, weakRefActivity);
    }

    /** TOOLBAR FUNCTIONALITY __________________________________________________________________ **/

    // setUpToolbar(): Sets up the Material Design style toolbar for the activity.
    private void setUpToolbar() {

        // Sets the references for the Drawer-related objects.
        DrawerLayout snDrawerLayout = (DrawerLayout) findViewById(R.id.sn_drawer_layout);

        // Initializes the Material Design style Toolbar object for the activity.
        if (sn_main_toolbar != null) {
            sn_main_toolbar.setTitle(R.string.app_name);
            setSupportActionBar(sn_main_toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        // Sets the Drawer toggle button for the Toolbar object.
        drawerToggle = new ActionBarDrawerToggle(this, snDrawerLayout, sn_main_toolbar, R.string.drawer_open, R.string.drawer_close) {

            // onDrawerClosed(): Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // Creates a call to onPrepareOptionsMenu() method.
            }

            // onDrawerOpened(): Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // Creates a call to onPrepareOptionsMenu() method.
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true); // Draws the toggle button indicator.
        snDrawerLayout.setDrawerListener(drawerToggle); // Sets the listener for the toggle button.

        // Retrieves the DrawerLayout to set the status bar color. This only takes effect on Lollipop,
        // or when using translucentStatusBar on KitKat.
        snDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.sn_toolbar_dark_color));
    }

    /** RECYCLE METHODS ________________________________________________________________________ **/

    // recycleMemory(): Recycles all View objects to clear up memory resources prior to Activity
    // destruction.
    private void recycleMemory() {

        // Unbinds all Drawable objects attached to the current layout.
        try { SNUnbind.unbindDrawables(findViewById(R.id.sn_main_activity_layout)); }
        catch (NullPointerException e) { e.printStackTrace(); } // Prints error message.
    }

    /** INTERFACE METHODS ______________________________________________________________________ **/

    @Override
    public void displayFragment(String fragType, double value) {

        // SNMapsFragment:
        if (fragType.equals("MAPS")) {
            Fragment fragment = new SNMapFragment(); // Initializes the SNMapFragment class.
            changeFragment(fragment, fragType, true);
            SNToolbar.updateToolbar(sn_main_toolbar, "Location");
            actionButton.setVisibility(View.INVISIBLE); // Hides the floating action button.
        }

        // SNCalculateFragment:
        else if (fragType.equals("CALCULATE")) {
            Fragment fragment = new SNCalculateFragment(); // Initializes the SNCalculateFragment class.
            changeFragment(fragment, fragType, true);
            SNToolbar.updateToolbar(sn_main_toolbar, "Funding");
            actionButton.setVisibility(View.INVISIBLE); // Hides the floating action button.
        }

        // InboxFragment:
        else if (fragType.equals("DOCUMENTS")) {
            mInboxFragment = new InboxFragment();
            changeFragment(mInboxFragment, fragType, true);
            SNToolbar.updateToolbar(sn_main_toolbar, "Documents");
            actionButton.setVisibility(View.VISIBLE); // Displays the floating action button.
        }

        // SNPayFragment:
        else if (fragType.equals("PAY")) {
            SNPayFragment fragment = new SNPayFragment();
            fragment.initializeFragment(value);
            changeFragment(fragment, fragType, true);
            SNToolbar.updateToolbar(sn_main_toolbar, "Payment");
            actionButton.setVisibility(View.INVISIBLE); // Hides the floating action button.
        }

        else {
            Fragment fragment = new SNProfileFragment();
            changeFragment(fragment, fragType, true);
            SNToolbar.updateToolbar(sn_main_toolbar, getString(R.string.app_name));
            actionButton.setVisibility(View.INVISIBLE); // Hides the floating action button.
        }

        // TODO: Add more functionality here later.
    }

    @Override
    public void removeFragment() {

        // TODO: Add functionality to remove the fragment from view and return focus to the PROFILE fragment.
        Fragment fragment = new SNProfileFragment(); // Initializes the SNProfileFragment class.
        changeFragment(fragment, "PROFILE", true);
    }

    /** PARSE METHODS __________________________________________________________________________ **/

    private ParseUser mCurrentUser;

    private void setupParse() {

        mCurrentUser = ParseUser.getCurrentUser();
        if (mCurrentUser == null) {
            login();
        }
        else {
            //Log.i(TAG, mCurrentUser.getUsername());

            ParseInstallation.getCurrentInstallation().addUnique("channels", "user_" + mCurrentUser.getObjectId());
            ParseInstallation.getCurrentInstallation().saveInBackground();

            String appName = this.getString(R.string.title_activity_view_image);
            //actionBar.setTitle(appName);

            ParseAnalytics.trackAppOpened(getIntent());
        }
    }

    private void login() {
        String username = "fytest2";
        String password = "Power88";

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                setProgressBarIndeterminateVisibility(false);

                if (e == null) {
                    // Success!

                    ParseInstallation.getCurrentInstallation().addUnique("channels", "user_" + user.getObjectId());
                    ParseInstallation.getCurrentInstallation().saveInBackground();

                    //inboxFragment.updateMessagesList();
                }
                else {

                    Toast.makeText(SNMainActivity.this, R.string.login_error_title, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private static final int TAKE_PHOTO_REQUEST = 0;
    private static final int PICK_PHOTO_REQUEST = 2;
    private static final int MEDIA_TYPE_IMAGE = 5;
    protected Uri mMediaUri;
    private InboxFragment mInboxFragment;

    private boolean handleOnActivityResultCamera(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO_REQUEST) {
                if (data == null) {
                    Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
                }
                else {
                    mMediaUri = data.getData();
                }
            }
            else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }

            if (requestCode == PICK_PHOTO_REQUEST || requestCode == TAKE_PHOTO_REQUEST)
            {
                uploadPicture();
            }
            return true;
        }
        return false;
    }

    private void handleSelectPicture() {
        final QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(SNMainActivity.this);

        ListView cameraOptions = new ListView(SNMainActivity.this);
        cameraOptions.setBackgroundColor(Color.GRAY);
        cameraOptions.setSelector(R.drawable.list_item_selector_light);

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(SNMainActivity.this, android.R.layout.simple_list_item_1,
                android.R.id.text1, getResources().getStringArray(R.array.camera_choices));

        cameraOptions.setAdapter(modeAdapter);
        qustomDialogBuilder.setView(cameraOptions);

        final AlertDialog customDialog = qustomDialogBuilder.create();
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.show();

        cameraOptions.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                customDialog.cancel();

                switch(position) {
                    case 0: // Take picture

                        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        if (mMediaUri == null) {
                            // display an error
                            Toast.makeText(SNMainActivity.this, R.string.error_external_storage,
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                        }
                        break;
                    case 1: // Choose picture
                        Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        choosePhotoIntent.setType("image/*");
                        startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                        break;
                }
            }
        });
    }

    private Uri getOutputMediaFileUri(int mediaType) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (isExternalStorageAvailable()) {
            // get the URI

            // 1. Get the external storage directory
            String appName = SNMainActivity.this.getString(R.string.app_name);
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    appName);

            // 2. Create our subdirectory
            if (! mediaStorageDir.exists()) {
                if (! mediaStorageDir.mkdirs()) {
                    Log.e(LOG_TAG, "Failed to create directory.");
                    return null;
                }
            }

            // 3. Create a file name
            // 4. Create the file
            File mediaFile;
            Date now = new Date();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

            String path = mediaStorageDir.getPath() + File.separator;
            if (mediaType == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            }
            else {
                return null;
            }

            Log.d(LOG_TAG, "File: " + Uri.fromFile(mediaFile));

            // 5. Return the file's URI
            return Uri.fromFile(mediaFile);
        }
        else {
            return null;
        }
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        else {
            return false;
        }
    }

    private void uploadPicture() {
        ParseObject po = createPictureMessage();
        uploadPictureHelper(po);
    }

    protected ParseObject createPictureMessage() {
        ParseFile profileImage = mCurrentUser.getParseFile("profileImage");
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_FILE_TYPE, ParseConstants.TYPE_IMAGE);
        message.put(ParseConstants.KEY_FILE_TIME, "0");
        message.put(ParseConstants.KEY_RECIPIENT_IDS, Collections.singletonList(mCurrentUser.getObjectId()));
        message.put(ParseConstants.KEY_RECIPIENT_NAMES, Collections.singletonList(mCurrentUser.getUsername()));
        message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        if (profileImage != null) {
            message.put("profileImage", profileImage);
        }

        byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);

        if (fileBytes == null) {
            return null;
        }
        else {
            //if (mFileType.equals(ParseConstants.TYPE_IMAGE)) {
            fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            //}

            String fileName = FileHelper.getFileName(this, mMediaUri, ParseConstants.TYPE_IMAGE);
            ParseFile file = new ParseFile(fileName, fileBytes);
            message.put(ParseConstants.KEY_FILE, file);

            return message;
        }
    }

    protected void uploadPictureHelper(ParseObject message) {
        setProgressBarIndeterminateVisibility(false);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                setProgressBarIndeterminateVisibility(false);

                if (e == null) {
                    // success!
                    Toast.makeText(SNMainActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();

                    if (mInboxFragment != null) {
                        mInboxFragment.updateMessagesList();
                    }
                } else {
                    Toast.makeText(SNMainActivity.this, R.string.error_sending_message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
