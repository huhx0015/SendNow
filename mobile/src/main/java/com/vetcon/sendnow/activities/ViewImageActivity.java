package com.vetcon.sendnow.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vetcon.sendnow.R;

public class ViewImageActivity extends Activity {

    public static final String TAG = ViewImageActivity.class.getSimpleName();

	CountDownTimer countDownTimer = null;
	
	private int secondsLeft = 0;
	private String messageTime;
	private ImageView imageView;
	private ImageView clockBg;
	private TextView countDownText;
	private ImageView closeBtn;
	private ProgressBar progressBar;

    protected Uri mOutputUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_view_image);

		Uri imageUri = getIntent().getData();
		messageTime = getIntent().getExtras().getString("messageTime");
		imageView = (ImageView) findViewById(R.id.imageView);
		clockBg = (ImageView) findViewById(R.id.clockBg);
		countDownText = (TextView) findViewById(R.id.counterTextView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		Picasso.with(getApplicationContext()).load(imageUri)

				.into(imageView, new Callback() {

					@Override
					public void onSuccess() {

						imageView.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);

						closeBtn = (ImageView) findViewById(R.id.closeBtn);
						closeBtn.setImageResource(R.drawable.closebtn);
						closeBtn.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {

								finish();
							}
						});

						if (messageTime.equals("0")) {
							clockBg.setImageDrawable(null);

							countDownTimer = new CountDownTimer(30000, 100) {
								public void onTick(long ms) {
									if (Math.round((float) ms / 1000.0f) != secondsLeft) {
										secondsLeft = Math.round((float) ms / 1000.0f);
									}

									// Log.i("test","ms="+ms+" till finished="+secondsLeft);
								}

								public void onFinish() {

									finish();
								}
							}.start();
						} else {
							clockBg.setImageResource(R.drawable.clockbg);

							int secondsToDestruct = Integer.valueOf(messageTime.toString());

							countDownTimer = new CountDownTimer(secondsToDestruct, 100) {
								public void onTick(long ms) {
									if (Math.round((float) ms / 1000.0f) != secondsLeft) {
										secondsLeft = Math.round((float) ms / 1000.0f);
										countDownText.setText("" + secondsLeft);
									}

									// Log.i("test","ms="+ms+" till finished="+secondsLeft);
								}

								public void onFinish() {

									countDownText.setText("0");

									finish();
								}
							}.start();
						}
					}

					@Override
					public void onError() {

						imageView.setVisibility(View.INVISIBLE);
					}
				});
	}
}
