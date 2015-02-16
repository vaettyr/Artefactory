package com.boltcave.artefactory;

import android.view.MotionEvent;

public class PanZoomTool extends SurfaceTool {

		protected volatile float soffsetx, soffsety, szoom;
		
		public PanZoomTool(BitmapView view) {super(view);}
		
		protected void clipOffset()
		{
			//some part of the image must be visible
			//must be greater than 0 - .5 width and less than max + .5 width
			float width = (view.mrect.width()/2) * view.offset_zoom;
			float height = (view.mrect.height()/2) * view.offset_zoom;
			float wbuffer = view.mscreen.width()/10;
			float hbuffer = view.mscreen.height()/10;
			view.offset_x = Math.min(Math.max(view.offset_x, wbuffer + view.mscreen.left - width),view.mscreen.right + width - wbuffer);
			view.offset_y = Math.min(Math.max(view.offset_y, hbuffer + view.mscreen.top - height), view.mscreen.bottom + height - hbuffer);
		}
		
		@Override
		public void handleTouchStart(MotionEvent event)
		{
			super.handleTouchStart(event);
			soffsetx = view.offset_x;
			soffsety = view.offset_y;
			szoom = view.offset_zoom;
		}
		
		@Override
		public void handleTouchChange(MotionEvent event)
		{
			super.handleTouchChange(event);
			
			view.offset_x = soffsetx - (stouchx - ctouchx);
			view.offset_y = soffsety - (stouchy - ctouchy);
			view.offset_zoom = szoom * (cspan / sspan);	
			
			clipOffset();
		}

		@Override
		public void handleTouchCancel(MotionEvent event)
		{
			touchCount = 0;
			touch = false;
			view.offset_x = soffsetx;
			view.offset_y = soffsety;
			view.offset_zoom = szoom;
		}
}
