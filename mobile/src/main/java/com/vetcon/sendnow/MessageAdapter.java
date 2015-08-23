package com.vetcon.sendnow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class MessageAdapter extends ArrayAdapter<ParseObject> {
	
	protected Context mContext;
	protected List<ParseObject> mMessages;
	
	public MessageAdapter(Context context, List<ParseObject> messages) {
		super(context, R.layout.message_item, messages);
		mContext = context;
		mMessages = messages;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
			holder = new ViewHolder();
			holder.profileImageView = (ParseImageView) convertView.findViewById(R.id.profileImage);
			holder.iconImageView2 = (ImageView)convertView.findViewById(R.id.messageIcon);
			holder.nameLabel = (TextView)convertView.findViewById(R.id.senderLabel);
			holder.nameLabel2 = (TextView)convertView.findViewById(R.id.messageTime);
			convertView.setTag(holder);
		}
		else {

            holder = (ViewHolder)convertView.getTag();
		}
		
		ParseObject message = mMessages.get(position);

        ParseFile profileImage = message.getParseFile("profileImage");

        if (profileImage != null) {

            Picasso.with(mContext).load(profileImage.getUrl()).into(holder.profileImageView);
        }
        else
        {
            holder.profileImageView.setImageResource(R.drawable.ic_profile);
        }

		if (message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
			
			holder.iconImageView2.setImageResource(R.drawable.ic_action_picture);
		}
		else {
			
			holder.iconImageView2.setImageResource(R.drawable.ic_action_play_over_video);
		}
		
		Date date = message.getCreatedAt();
		
		String timestamp = new SimpleDateFormat("EEE MMM dd, yyyy, hh:mm a", Locale.US).format(date);

//		holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));
		holder.nameLabel.setText("I.D. " + position);

		holder.nameLabel2.setText(timestamp);


        List<String> readStatus = message.getList(ParseConstants.KEY_RECIPIENT_READ_IDS);

		holder.nameLabel.setTextColor(Color.WHITE);
		holder.nameLabel2.setTextColor(Color.WHITE);
		holder.nameLabel.setBackgroundColor(Color.TRANSPARENT);
		holder.nameLabel2.setBackgroundColor(Color.TRANSPARENT);

        if (readStatus != null) {

            if (readStatus.contains(ParseUser.getCurrentUser().getObjectId()))
            {
//                holder.nameLabel.setTextColor(Color.GRAY);
//                holder.nameLabel2.setTextColor(Color.GRAY);
            }
        }
        else
        {
            holder.nameLabel.setTextColor(Color.BLACK);
            holder.nameLabel2.setTextColor(Color.BLACK);
        }
		
		return convertView;
	}
	
	private static class ViewHolder {
		ParseImageView profileImageView;
		ImageView iconImageView2;
		TextView nameLabel;
		TextView nameLabel2;
	}
	
	public void refill(List<ParseObject> messages) {
		
		// System.out.println("Messages Refill");
		
		mMessages.clear();
		mMessages.addAll(messages);
		notifyDataSetChanged();
	}
}






