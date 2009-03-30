package com.w2e.pictures.activity;

import com.w2e.pictures.R;

import android.app.Activity;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {
	
	protected static final String LOG_TAG = CameraActivity.class.getName();
	
	private Camera camera;
	private SurfaceView surface;
	private SurfaceHolder surfaceHolder;
	protected Bitmap	currentBitmap;
	protected Bitmap	scaledBitmap;
	protected ImageView	previewImageView;

	private boolean inPreviewMode;

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
		surface.setVisibility(View.GONE);
		previewImageView.setVisibility(View.VISIBLE);
		previewImageView.setImageBitmap(currentBitmap);
	}
	
	private void showCameraPreview() {
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
        surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
