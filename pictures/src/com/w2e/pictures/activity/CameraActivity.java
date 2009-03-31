package com.w2e.pictures.activity;

import com.w2e.pictures.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {
	
	protected static final String LOG_TAG = CameraActivity.class.getName();
	
	private Camera camera;
	private SurfaceView surface;
	private SurfaceHolder surfaceHolder;
	protected Bitmap	currentBitmap;
	protected ImageView	previewImageView;
	private Button savePhotoButton;
	private Button discardPhotoButton;
	private boolean inPreviewMode;
	private View buttonContainer;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        
        setUpViews();
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (KeyEvent.KEYCODE_BACK == keyCode) { // let back work;
    		return super.onKeyDown(keyCode, event);
    	}
        if (KeyEvent.KEYCODE_SPACE == keyCode || KeyEvent.KEYCODE_DPAD_CENTER == keyCode || KeyEvent.KEYCODE_CAMERA == keyCode) {
            camera.takePicture(null, null, jpegPictureCallback);
            return true;
        }

        return false;
    }

    protected void takePicture(byte[] jpegEncodedData) {
    	camera.stopPreview();
		currentBitmap = BitmapFactory.decodeByteArray(jpegEncodedData, 0, jpegEncodedData.length);
		showBitmapPreview();
	}

	private void showBitmapPreview() {
		surface.setVisibility(View.VISIBLE);
		previewImageView.setVisibility(View.VISIBLE);
		previewImageView.setImageBitmap(currentBitmap);
		buttonContainer.setVisibility(View.VISIBLE);
	}
	
	private void showCameraPreview() {
		buttonContainer.setVisibility(View.GONE);
		surface.setVisibility(View.GONE);
		previewImageView.setVisibility(View.GONE);
		surface.setVisibility(View.VISIBLE);
		previewImageView.setImageBitmap(null);
		if (null != currentBitmap) {
			currentBitmap.recycle();
			currentBitmap = null;
			System.gc();
		}
		camera.startPreview();
	}

    protected void saveCurrentPhoto() {
		String photoPath = MediaStore.Images.Media.insertImage(
				getContentResolver(), currentBitmap, "A Sweet Picture", "A Sweet Picture"
			);
		if (photoPath == null) {
			Log.e(getClass().getName(),
			        "Error saving photo to image library");
			return;
		} else {
			Uri photoURI = Uri.parse(photoPath);
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, photoURI));
			Toast.makeText(this, "Saved Photo", Toast.LENGTH_LONG).show();
		}
	}

    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(LOG_TAG, "opening camera");
        camera = Camera.open();
    }
    
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.e(LOG_TAG, "surfaceChanged");

        if (inPreviewMode) {
            camera.stopPreview();
        }

        Camera.Parameters p = camera.getParameters();
        p.setPreviewSize(w, h);
        camera.setParameters(p);
        camera.setPreviewDisplay(holder);
        camera.startPreview();
        inPreviewMode = true;
    }
    
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(LOG_TAG, "surfaceDestroyed");
        cleanUpCamera();
    }

    private void cleanUpCamera() {
        camera.stopPreview();
        inPreviewMode = false;
        camera.release();
        camera = null;
    }

	protected void setUpViews() {
		previewImageView = (ImageView)findViewById(R.id.photo_preview);
        surface = (SurfaceView)findViewById(R.id.photo_camera_surface);
        savePhotoButton = (Button)findViewById(R.id.photo_save_photo_button);
        discardPhotoButton = (Button)findViewById(R.id.photo_discard_photo_button);
        buttonContainer = findViewById(R.id.photo_button_container);
        surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        savePhotoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveCurrentPhoto();
			}
        });
        
        discardPhotoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				discardPhoto();
			}
        });
	}
	
	protected void discardPhoto() {
    	showCameraPreview();
    }
    
	private Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera c) {
            Log.e(LOG_TAG, "PICTURE CALLBACK: data.length = " + data.length);
            takePicture(data);
        }
    };

}
