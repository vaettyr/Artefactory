package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.widget.AbsListView.*;
import javax.security.auth.*;

public class MainActivity extends Activity
{
	private interface SurfaceTouchHandler
	{
		public void handleTouchStart(MotionEvent event)
		public void handleTouchChange(MotionEvent event)
		public void handleTouchEnd(MotionEvent event)
		public void handleTouchCancel(MotionEvent event)
	}
	
	private class SurfaceTool implements SurfaceTouchHandler
	{
		private volatile int touchCount = 0;
		private volatile float stouchx, stouchy, ctouchx, ctouchy, sspan, cspan = 0f;
		private volatile float soffsetx, soffsety, szoom;
		private boolean touch = false;
		BitmapView view;
		
		
		public SurfaceTool(BitmapView bmview)
		{
			view = bmview;
		}
		
		private float[] getMultitouch(MotionEvent event)
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
			soffsetx = view.offset_x;
			soffsety = view.offset_y;
			szoom = view.offset_zoom;
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
					//view.offset_zoom = cspan;
					break;
			}
			view.offset_x = soffsetx - (stouchx - ctouchx);
			view.offset_y = soffsety - (stouchy - ctouchy);
			view.offset_zoom = szoom *(cspan / sspan);
			
			
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
	}
	
	private class BitmapView extends SurfaceView implements Runnable
	{
		private Bitmap mbitmap;
		private Paint paint;
		private Rect mscreen, mrect;
		private Context context;
		private Thread mthread;
		private SurfaceHolder holder;
		private SurfaceTool tool;
		volatile boolean running = false;
		
		//volatile boolean touched = false;
		//volatile float touch_x, touch_y;
		volatile float offset_x, offset_y, offset_zoom;
		
		public BitmapView(Context ctx)
		{
			super(ctx);
			context = ctx;
			paint = new Paint();
			paint.setDither(false);
			paint.setColor(Color.WHITE);
			paint.setTextSize(20f);
			//mbitmap = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
			mbitmap = BitmapFactory.decodeResource(getResources(),R.drawable.som_gnome);
			holder = getHolder();
			tool = new SurfaceTool(this);
			mrect = new Rect(0,0,mbitmap.getWidth(),mbitmap.getHeight());
			offset_zoom = 1f;
			//getDrawingRect(mscreen);
		}
		
		public void onResumeBitmapView(){
			running = true;
			mthread = new Thread(this);
			mthread.start();
		}

		public void onPauseBitmapView(){
			boolean retry = true;
			running = false;
			while(retry){
				try {
					mthread.join();
					retry = false;
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			mscreen = new Rect();
			getDrawingRect(mscreen);
			//touch_x = mscreen.centerX();
			//touch_y = mscreen.centerY();
			offset_x = mscreen.centerX();
			offset_y = mscreen.centerY();
		}
		
		@Override
		public void run()
		{
			while(running){
				if(holder.getSurface().isValid()){
					Canvas canvas = holder.lockCanvas();
					canvas.drawColor(Color.BLACK);
					Rect drawRect = calculateRect();
					canvas.drawBitmap(mbitmap, null, drawRect, paint);
					canvas.drawText(Float.toString(offset_zoom), offset_x, offset_y, paint);
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
		
		private Rect calculateRect()
		{
			float dimx = (mrect.width()/2) * offset_zoom;
			float dimy = (mrect.height()/2) * offset_zoom;
			return new Rect((int)(offset_x - dimx),
							(int)(offset_y - dimy),
							(int)(offset_x + dimx),
							(int)(offset_y+ dimy));
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			//touch_x = event.getX();
			//touch_y = event.getY();

			int action = event.getAction();
			switch(action){
				case MotionEvent.ACTION_DOWN:
					//touched = true;
					tool.handleTouchStart(event);
					break;
				case MotionEvent.ACTION_MOVE:
					//touched = true;
					tool.handleTouchChange(event);
					break;
				case MotionEvent.ACTION_UP:
					//touched = false;
					tool.handleTouchEnd(event);
					break;
				case MotionEvent.ACTION_CANCEL:
					//touched = false;
					tool.handleTouchCancel(event);
					break;
				case MotionEvent.ACTION_OUTSIDE:
					//touched = false;
					tool.handleTouchCancel(event);
					break;
				default:
			}
			return true; //processed
		}

	}
	
	BitmapView bmap;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        bmap = new BitmapView(this);
		bmap.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setContentView(bmap);
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bmap.onResumeBitmapView();
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		bmap.onPauseBitmapView();
	}
	
}
