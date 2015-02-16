package com.boltcave.artefactory;

import android.view.MotionEvent;

public interface SurfaceTouchHandler
{
	public void handleTouchStart(MotionEvent event);
	public void handleTouchChange(MotionEvent event);
	public void handleTouchEnd(MotionEvent event);
	public void handleTouchCancel(MotionEvent event);
}