package com.w2e.pictures.activity;

import com.w2e.pictures.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ChoosePhotoActivity extends Activity {

	private static final int CHOOSE_PHOTO_REQUEST = 1;
	private Button choosePhotoButton;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_photo);
		setUpViews();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (CHOOSE_PHOTO_REQUEST == requestCode) {
			Uri photoUri = (Uri)intent.getData();
			imageView.setImageURI(photoUri);
		} else {
			super.onActivityResult(requestCode, resultCode, intent);
		}
	}

	protected void choosePhotoFromLibrary() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, CHOOSE_PHOTO_REQUEST);
	}
	
	private void setUpViews() {
		choosePhotoButton = (Button)findViewById(R.id.photo_choose_photo_button);
		imageView = (ImageView)findViewById(R.id.photo_preview);
		choosePhotoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				choosePhotoFromLibrary();
			}
		});
	}
}

