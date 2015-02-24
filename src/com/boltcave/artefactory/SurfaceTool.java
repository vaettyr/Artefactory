package com.boltcave.artefactory;

import android.view.MotionEvent;

public  class SurfaceTool implements SurfaceTouchHandler
{
	protected volatile int touchCount = 0;
	protected volatile float stouchx, stouchy, ctouchx, ctouchy, sspan, cspan = 0f;
	protected boolean touch = false;
	BitmapView view;
	
	public SurfaceTool(BitmapView bmview)
	{
		view = bmview;
	}
	
	protected float[] getMultitouch(MotionEvent event)
	{
		float[] mstats = new float[3];
		float p1x, p1y, p2x, p2y = 0f;
		p1x = event.getX(0);
		p1y = event.getY(0);
		p2x = event.getX(1);
		p2y = event.getY(1);
		mstats[0] = (p1x + p2x)/2.0f;
		mstats[1] = (p1y + p2y)/2.0f;
		float dx = p2x - p1x;
		float dy = p2y - p1y;
		mstats[2] = (float)Math.sqrt(dx*dx+dy*dy);
		return mstats;
	}
	
	@Override
	public void handleTouchStart(MotionEvent event)
	{
		touch = true;
		touchCount = event.getPointerCount();
		switch(touchCount)
		{
			case 1:
				stouchx = ctouchx = event.getX();
				stouchy = ctouchy = event.getY();
				sspan = cspan = 1f;
				break;
			case 2: 
				float[] touchstats = getMultitouch(event);
				stouchx = ctouchx = touchstats[0];
				stouchy = ctouchy = touchstats[1];
				sspan = cspan = touchstats[2];
				break;
		}
	}

	@Override
	public void handleTouchChange(MotionEvent event)
	{
		if (event.getPointerCount() != touchCount)
			handleTouchStart(event);
		switch(touchCount)
		{
			case 1:
				ctouchx = event.getX();
				ctouchy = event.getY();
				break;
			case 2:
				float[] touchstats = getMultitouch(event);
				ctouchx = touchstats[0];
				ctouchy = touchstats[1];
				cspan = touchstats[2];
				break;
		}		
	}

	@Override
	public void handleTouchEnd(MotionEvent event)
	{
		touchCount = 0;
		touch = false;
	}

	@Override
	public void handleTouchCancel(MotionEvent event)
	{
		touchCount = 0;
		touch = false;
	}
	
	@Override
	public void handleColorChanged(int color){}
}