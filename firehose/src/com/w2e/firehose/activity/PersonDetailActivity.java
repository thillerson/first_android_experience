package com.w2e.firehose.activity;

import com.w2e.firehose.R;
import com.w2e.firehose.resources.User;
import com.w2e.firehose.twitter.TwitterAPI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonDetailActivity extends Activity {
	
	public static final String PERSON_ID_EXTRA = "person_id";
	
	final private Handler handler = new Handler();
	final private TwitterAPI api = new TwitterAPI();
	
	private User user;
	private Bitmap avatar;
	private ImageView avatarView;
	private TextView locationText;
	private TextView nameText;
	private TextView urlText;
	private TextView screenNameText;

	private Runnable finishedLoadingTask = new Runnable() {
		public void run() {
			finishedLoadingUser();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.person_detail);
		findViews();
		Intent intent = getIntent();
		final long personId = intent.getLongExtra(PERSON_ID_EXTRA, 0);
		Thread loadUserThread = new Thread() {
			public void run() {
				setProgressBarIndeterminateVisibility(true);
				user = api.getUser(personId);
				avatar = user.getAvatar();
				handler.post(finishedLoadingTask);
			}
		};
		loadUserThread.start();

	}

	protected void finishedLoadingUser() {
		setTitle(user.getScreenName());
		screenNameText.setText(user.getScreenName());
		nameText.setText(user.getName());
		locationText.setText(user.getLocation());
		urlText.setText(user.getUrl());
		avatarView.setImageBitmap(avatar);
		setProgressBarIndeterminateVisibility(false);
	}

	private void findViews() {
		avatarView = (ImageView)findViewById(R.id.user_avatar);
		screenNameText = (TextView)findViewById(R.id.user_screen_name_text);
		nameText = (TextView)findViewById(R.id.user_name_text);
		locationText = (TextView)findViewById(R.id.user_location_text);
		urlText = (TextView)findViewById(R.id.user_url_text);
	}

}
