package com.boltcave.artefactory;
import android.graphics.*;
import android.view.*;

public class PixelTool extends SurfaceTool
{
	public PixelTool(BitmapView view){super(view);}
	
	protected void setPixel()
	{
		int px, py;
		float dimx = (view.mrect.width()/2) * view.offset_zoom;
		float dimy = (view.mrect.height()/2) * view.offset_zoom;
		px = (int)((this.ctouchx - (view.offset_x - dimx))/view.offset_zoom);
		py = (int)((this.ctouchy - (view.offset_y - dimy))/view.offset_zoom);
		if (px >= 0 && px <= view.mrect.width() &&
			py >= 0 && py <= view.mrect.height())
			{
				view.mbitmap.setPixel(px, py, Color.RED);
			}
						
	}

	@Override
	public void handleTouchStart(MotionEvent event)
	{
		// TODO: Implement this method
		super.handleTouchStart(event);
		setPixel();
	}

	@Override
	public void handleTouchChange(MotionEvent event)
	{
		// TODO: Implement this method
		super.handleTouchChange(event);
		setPixel();
	}
	

}
