package com.w2e.firehose.layout;

import com.w2e.firehose.R;
import com.w2e.firehose.resources.Status;
import com.w2e.firehose.resources.User;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatusListItem extends RelativeLayout {

	final private Handler handler = new Handler();
	private ImageView avatarView;
	private TextView screenName;
	private TextView statusText;
	protected Bitmap avatar;

	private Runnable finishedLoadingTask = new Runnable() {
		public void run() {
			finishedLoadingUserAvatar();
		}
	};

	public StatusListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setStatus(Status status) {
		final User user = status.getUser();
		findViews();
		avatarView.setImageBitmap(null);
		screenName.setText(user.getScreenName());
		statusText.setText(status.getText());
		Thread loadUserAvatarThread = new Thread() {
			public void run() {
				avatar = user.getAvatar();
				handler.post(finishedLoadingTask);
			}
		};
		loadUserAvatarThread.start();
	}

	protected void finishedLoadingUserAvatar() {
		avatarView.setImageBitmap(avatar);
	}

	private void findViews() {
		avatarView = (ImageView)findViewById(R.id.user_avatar);
		screenName = (TextView)findViewById(R.id.status_user_name_text);
		statusText = (TextView)findViewById(R.id.status_text);
	}

}
