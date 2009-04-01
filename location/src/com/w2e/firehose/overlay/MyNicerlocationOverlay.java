package com.w2e.firehose.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Projection;

public class MyNicerlocationOverlay extends MyLocationOverlay {

	private static final int CONTAINER_WIDTH				= 120;
	private static final int CONTAINER_HEIGHT				= 80;
	private static final int CONTAINER_RADIUS				= 8;
	private static final int CONTAINER_PADDING				= 2;
	private static final int CONTAINER_ORIGIN_OFFSET		= 20;
	private static final int CONTAINER_SHADOW_OFFSET		= 2;
	private static final int CONTAINER_POINTER_BASE			= 20;
	private static final int CONTAINER_POINTER_RIGHT_OFFSET	= 8;
	
	private final Paint labelTextPaint		=  new Paint();
	private final Paint containerPaint		=  new Paint();
	private final Paint shadowPaint			=  new Paint();

	public MyNicerlocationOverlay(Context context, MapView mapView) {
		super(context, mapView);
		containerPaint.setARGB(240, 39, 61, 78);
		containerPaint.setAntiAlias(true);
		shadowPaint.setARGB(100, 0, 0, 0);
		shadowPaint.setAntiAlias(true);
		labelTextPaint.setAntiAlias(true);
		labelTextPaint.setColor(Color.WHITE);
		labelTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		labelTextPaint.setTextSize(18);
	}

	@Override
	public synchronized boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
    	super.draw(canvas, mapView, shadow, when);
    	Point locationPoint = new Point();
    	Projection projection = mapView.getProjection();
    	GeoPoint geoPoint = getMyLocation();
    	if (null != geoPoint) {
        	projection.toPixels(getMyLocation(), locationPoint);
        	final int containerLeft = locationPoint.x - CONTAINER_WIDTH/2;
        	final int containerTop = locationPoint.y - CONTAINER_HEIGHT - CONTAINER_ORIGIN_OFFSET;
        	final int containerRight = locationPoint.x + CONTAINER_WIDTH/2;
        	final int containerBottom = locationPoint.y - CONTAINER_ORIGIN_OFFSET;
        	final Point pointerLeftIntersection = new Point(locationPoint.x - (CONTAINER_POINTER_BASE/2) + CONTAINER_POINTER_RIGHT_OFFSET, locationPoint.y - CONTAINER_ORIGIN_OFFSET);
        	final Point pointerRightIntersection = new Point(locationPoint.x + (CONTAINER_POINTER_BASE/2) + CONTAINER_POINTER_RIGHT_OFFSET, locationPoint.y - CONTAINER_ORIGIN_OFFSET);
        	Path frontPointer = new Path();
        	frontPointer.moveTo(locationPoint.x, locationPoint.y);
        	frontPointer.lineTo(pointerLeftIntersection.x, pointerLeftIntersection.y);
        	frontPointer.lineTo(pointerRightIntersection.x, pointerRightIntersection.y);
        	frontPointer.close();
            if (!shadow) {
            	final int textStartLeft = containerLeft + CONTAINER_RADIUS + CONTAINER_PADDING*3;
            	final float loginTextStartTop = containerTop + labelTextPaint.getFontSpacing() + CONTAINER_PADDING;
                canvas.drawRoundRect(new RectF(containerLeft, containerTop, containerRight, containerBottom), CONTAINER_RADIUS, CONTAINER_RADIUS, containerPaint);
                canvas.drawPath(frontPointer, containerPaint);
                
                canvas.drawText("Hey, You!", textStartLeft, loginTextStartTop, labelTextPaint);
            } else {
            	final int containerShadowLeft = containerLeft + CONTAINER_SHADOW_OFFSET;
            	final int containerShadowTop = containerTop + CONTAINER_SHADOW_OFFSET;
            	final int containerShadowRight = containerRight + CONTAINER_SHADOW_OFFSET;
            	final int containerShadowBottom = containerBottom + CONTAINER_SHADOW_OFFSET;
            	final Path shadowPointer = new Path(frontPointer);
            	shadowPointer.offset(CONTAINER_SHADOW_OFFSET, CONTAINER_SHADOW_OFFSET);
            	canvas.drawRoundRect(new RectF(containerShadowLeft, containerShadowTop, containerShadowRight, containerShadowBottom), CONTAINER_RADIUS, CONTAINER_RADIUS, shadowPaint);
            	canvas.drawPath(shadowPointer, shadowPaint);
            }
    	}
        return true;
	}

}
