package com.vetcon.sendnow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity {
	
	public static final String TAG = MainActivity.class.getSimpleName();
	
	public static final int TAKE_PHOTO_REQUEST = 0;
	public static final int PICK_PHOTO_REQUEST = 2;

	public static final int MEDIA_TYPE_IMAGE = 5;


    protected Uri mMediaUri;

	private ParseUser mCurrentUser;

	private InboxFragment inboxFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();


		inboxFragment = (InboxFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

		mCurrentUser = ParseUser.getCurrentUser();
		if (mCurrentUser == null) {
			login();
		}
		else {
			Log.i(TAG, mCurrentUser.getUsername());
			
			ParseInstallation.getCurrentInstallation().addUnique("channels", "user_" + mCurrentUser.getObjectId());
			ParseInstallation.getCurrentInstallation().saveInBackground();
			
			String appName = MainActivity.this.getString(R.string.title_activity_view_image);
			actionBar.setTitle(appName); 
			
			ParseAnalytics.trackAppOpened(getIntent());
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
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
                sendPicture();
			}
		}
		else if (resultCode == RESULT_CANCELED) {
			
		}
	}

    public void sendPicture() {
		setProgressBarIndeterminateVisibility(true);
		ParseObject po = createMessage();
		send(po);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
		switch(itemId) {
		
			case R.id.action_camera:

                final QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(MainActivity.this);

                ListView cameraOptions = new ListView(MainActivity.this);
                cameraOptions.setBackgroundColor(Color.WHITE);
                cameraOptions.setSelector(R.drawable.list_item_selector);

                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,
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
                                    Toast.makeText(MainActivity.this, R.string.error_external_storage,
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

                    private Uri getOutputMediaFileUri(int mediaType) {
                        // To be safe, you should check that the SDCard is mounted
                        // using Environment.getExternalStorageState() before doing this.
                        if (isExternalStorageAvailable()) {
                            // get the URI

                            // 1. Get the external storage directory
                            String appName = MainActivity.this.getString(R.string.app_name);
                            File mediaStorageDir = new File(
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                    appName);

                            // 2. Create our subdirectory
                            if (! mediaStorageDir.exists()) {
                                if (! mediaStorageDir.mkdirs()) {
                                    Log.e(TAG, "Failed to create directory.");
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

                            Log.d(TAG, "File: " + Uri.fromFile(mediaFile));

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
                });

            break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	protected ParseObject createMessage() {
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

	protected void send(ParseObject message) {
		message.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				setProgressBarIndeterminateVisibility(false);

				if (e == null) {
					// success!
					Toast.makeText(MainActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();

					inboxFragment.updateMessagesList();

					String pushMessage = mCurrentUser.getUsername() + " store a " + ParseConstants.TYPE_IMAGE + "!";

					// Notification for Android & iOS users
					JSONObject data = new JSONObject();
					try {
						data.put("action", "com.vetcon.sendnow.UPDATE_MESSAGES");
						data.put("alert", pushMessage);
						data.put("badge", "Increment");
						data.put("sound", "shutterClick.wav");

						ParsePush push = new ParsePush();
						push.setChannels(getPushRecipientIds()); // Notice we use setChannels not setChannel
						push.setData(data);
						push.sendInBackground();

					} catch (JSONException e1) {
						e1.printStackTrace();
					}

					// System.out.println(getRecipientNames());
				}
				else {
					Toast.makeText(MainActivity.this, R.string.error_sending_message, Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	protected ArrayList<String> getPushRecipientIds() {
		ArrayList<String> pushRecipientIds = new ArrayList<String>();
		pushRecipientIds.add("user_" + mCurrentUser.getObjectId());

		return pushRecipientIds;
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

					inboxFragment.updateMessagesList();
				}
				else {

					Toast.makeText(MainActivity.this, R.string.login_error_title, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
