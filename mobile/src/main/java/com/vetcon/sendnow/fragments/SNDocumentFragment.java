package com.vetcon.sendnow.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.vetcon.sendnow.ui.list.MessageAdapter;
import com.vetcon.sendnow.data.ParseConstants;
import com.vetcon.sendnow.R;
import com.vetcon.sendnow.activities.ViewImageActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SNDocumentFragment extends ListFragment {

	public static final String TAG = SNDocumentFragment.class.getSimpleName();
	
	protected List<ParseObject> mMessages;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_inbox,
				container, false);
		
		LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mMessageReceiver,
			      new IntentFilter("updateMessages"));
		
		return rootView;
	}
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    
			 updateMessagesList();
			  
		     Log.d("receiver", "Update Messages Received");
		  }
	};

	@Override
	public void onDestroy() {
	  // Unregister since the activity is about to be closed.
	  // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:] 
	  LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mMessageReceiver);
	  
	  super.onDestroy();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		updateMessagesList();
	}
	
	public void updateMessagesList() {
		if (ParseUser.getCurrentUser() == null) {
            return;
        }

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
		query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
		query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		query.findInBackground(new FindCallback<ParseObject>() {
				
			@Override
			public void done(List<ParseObject> messages, ParseException e) {
				
				if (e == null) {
					
					  // We found messages!
					  mMessages = messages;
						
					  if (isFragmentUIActive())
					  {
							if (getListView().getAdapter() == null) {
								MessageAdapter adapter = new MessageAdapter(
										getListView().getContext(), 
										mMessages);
								setListAdapter(adapter);
							}
							else {
								// refill the adapter!
								((MessageAdapter)getListView().getAdapter()).refill(mMessages);
							}
					   }
				  }
			  }
		 });
	}
	
	public boolean isFragmentUIActive() {
	    return isAdded() && isVisible() && !isDetached() && !isRemoving();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ParseObject message = mMessages.get(position);
		
		String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
		String messageTime = message.getString(ParseConstants.KEY_FILE_TIME);
		
		ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
		Uri fileUri = Uri.parse(file.getUrl());
		
		if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
			
			// view the image
			Intent intent = new Intent(getActivity(), ViewImageActivity.class);
			intent.setData(fileUri);
			intent.putExtra("messageTime", messageTime);
			startActivity(intent);
		}
		else {
			

		}

        if (messageTime.equals("0"))
        {
            List<String> readStatus = message.getList(ParseConstants.KEY_RECIPIENT_READ_IDS);

            if (readStatus != null) {

                if (readStatus.contains(ParseUser.getCurrentUser().getObjectId())) {

                }
                else
                {
                    if (messageType.equals(ParseConstants.TYPE_IMAGE)) {

                        String pushMessage = ParseUser.getCurrentUser().getUsername() + " just viewed your photo!";

                        // Notification for Android & iOS users
                        JSONObject data = new JSONObject();
                        try {
                            data.put("alert", pushMessage);
                            data.put("badge", "Increment");
                            data.put("sound", "message.wav");

                            ParsePush push = new ParsePush();
                            push.setChannel("user_" + message.getString(ParseConstants.KEY_SENDER_ID)); // Notice we use setChannel not setChannels
                            push.setData(data);
                            push.sendInBackground();

                        } catch (JSONException e1) {

                            e1.printStackTrace();
                        }
                    }
                    else
                    {
                        String pushMessage = ParseUser.getCurrentUser().getUsername() + " just viewed your video!";

                        // Notification for Android & iOS users
                        JSONObject data = new JSONObject();
                        try {
                            data.put("alert", pushMessage);
                            data.put("badge", "Increment");
                            data.put("sound", "message.wav");

                            ParsePush push = new ParsePush();
                            push.setChannel("user_" + message.getString(ParseConstants.KEY_SENDER_ID)); // Notice we use setChannel not setChannels
                            push.setData(data);
                            push.sendInBackground();

                        } catch (JSONException e1) {

                            e1.printStackTrace();
                        }
                    }

                    ArrayList<String> idsToAdd = new ArrayList<String>();
                    idsToAdd.add(ParseUser.getCurrentUser().getObjectId());

                    message.addAll(ParseConstants.KEY_RECIPIENT_READ_IDS, idsToAdd);
                    message.saveInBackground();
                }
            }
            else
            {
                if (messageType.equals(ParseConstants.TYPE_IMAGE)) {

                    String pushMessage = ParseUser.getCurrentUser().getUsername() + " just viewed your photo!";

                    // Notification for Android & iOS users
                    JSONObject data = new JSONObject();
                    try {
                        data.put("alert", pushMessage);
                        data.put("badge", "Increment");
                        data.put("sound", "message.wav");

                        ParsePush push = new ParsePush();
                        push.setChannel("user_" + message.getString(ParseConstants.KEY_SENDER_ID)); // Notice we use setChannel not setChannels
                        push.setData(data);
                        push.sendInBackground();

                    } catch (JSONException e1) {

                        e1.printStackTrace();
                    }
                }
                else
                {
                    String pushMessage = ParseUser.getCurrentUser().getUsername() + " just viewed your video!";

                    // Notification for Android & iOS users
                    JSONObject data = new JSONObject();
                    try {
                        data.put("alert", pushMessage);
                        data.put("badge", "Increment");
                        data.put("sound", "message.wav");

                        ParsePush push = new ParsePush();
                        push.setChannel("user_" + message.getString(ParseConstants.KEY_SENDER_ID)); // Notice we use setChannel not setChannels
                        push.setData(data);
                        push.sendInBackground();

                    } catch (JSONException e1) {

                        e1.printStackTrace();
                    }
                }

                ArrayList<String> idsToAdd = new ArrayList<String>();
                idsToAdd.add(ParseUser.getCurrentUser().getObjectId());

                message.addAll(ParseConstants.KEY_RECIPIENT_READ_IDS, idsToAdd);
                message.saveInBackground();
            }

            return;
        }

        if (messageType.equals(ParseConstants.TYPE_IMAGE)) {

            String pushMessage = ParseUser.getCurrentUser().getUsername() + " just viewed your photo!";

            // Notification for Android & iOS users
            JSONObject data = new JSONObject();
            try {
                data.put("alert", pushMessage);
                data.put("badge", "Increment");
                data.put("sound", "message.wav");

                ParsePush push = new ParsePush();
                push.setChannel("user_" + message.getString(ParseConstants.KEY_SENDER_ID)); // Notice we use setChannel not setChannels
                push.setData(data);
                push.sendInBackground();

            } catch (JSONException e1) {

                e1.printStackTrace();
            }
        }
        else {

            String pushMessage = ParseUser.getCurrentUser().getUsername() + " just viewed your video!";

            // Notification for Android & iOS users
            JSONObject data = new JSONObject();
            try {
                data.put("alert", pushMessage);
                data.put("badge", "Increment");
                data.put("sound", "message.wav");

                ParsePush push = new ParsePush();
                push.setChannel("user_" + message.getString(ParseConstants.KEY_SENDER_ID)); // Notice we use setChannel not setChannels
                push.setData(data);
                push.sendInBackground();

            } catch (JSONException e1) {

                e1.printStackTrace();
            }
        }

        // Delete it!
        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);

        if (ids.size() == 1) {
            // last recipient - delete the whole thing!
            message.deleteInBackground();
        }
        else {
            // remove the recipient and save
            ids.remove(ParseUser.getCurrentUser().getObjectId());

            ArrayList<String> idsToRemove = new ArrayList<String>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());

            message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
            message.saveInBackground();
        }
    }
}








