package com.boltcave.artefactory;
import android.graphics.*;
import android.view.*;

public class PixelTool extends SurfaceTool
{
	private Canvas canvas;
	private Paint paint;
	public PixelTool(BitmapView view)
	{
		super(view);
		canvas = new Canvas(view.mbitmap);
		paint = new Paint();
		paint.setColor(Color.RED);
		//paint.setAntiAlias(true);
		//paint.setStrokeWidth(2.0f);
	}
	
	protected void setPixel()
	{
		int px, py, cx, cy;
		float dimx = (view.mrect.width()/2) * view.offset_zoom;
		float dimy = (view.mrect.height()/2) * view.offset_zoom;
		px = (int)((this.ctouchx - (view.offset_x - dimx))/view.offset_zoom);
		py = (int)((this.ctouchy - (view.offset_y - dimy))/view.offset_zoom);
		cx = (int)((this.stouchx - (view.offset_x - dimx))/view.offset_zoom);
		cy = (int)((this.stouchy - (view.offset_y - dimy))/view.offset_zoom);
		if (px >= 0 && px <= view.mrect.width() &&
			py >= 0 && py <= view.mrect.height() &&
			cx >= 0 && cx <= view.mrect.width() &&
			cy >= 0 && cy <= view.mrect.height())
			{
				canvas.drawLine(cx, cy, px, py, paint);
			}
						
	}
	
	@Override
	public void handleTouchStart(MotionEvent event)
	{
		super.handleTouchStart(event);
		setPixel();
	}

	@Override
	public void handleTouchChange(MotionEvent event)
	{
		//push the last touch coordinates to the start touch coordinates
		stouchy = ctouchy;
		stouchx = ctouchx;
		super.handleTouchChange(event);
		setPixel();
	}
	

}
