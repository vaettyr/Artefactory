package com.boltcave.artefactory.Tools;

import android.view.MotionEvent;

public interface SurfaceTouchHandler
{
	public void handleTouchStart(MotionEvent event);
	public void handleTouchChange(MotionEvent event);
	public void handleTouchEnd(MotionEvent event);
	public void handleTouchCancel(MotionEvent event);
	public void handleColorChanged(int color);
}