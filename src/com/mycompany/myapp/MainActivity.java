package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.widget.AbsListView.*;

public class MainActivity extends Activity
{
	private class BitmapView extends SurfaceView implements Runnable
	{
		private Bitmap mbitmap;
		private Paint paint;
		private Rect mscreen, mrect;
		private Context context;
		private Thread mthread;
		private SurfaceHolder holder;
		volatile boolean running = false;
		volatile boolean touched = false;
		volatile float touch_x, touch_y;
		
		public BitmapView(Context ctx)
		{
			super(ctx);
			context = ctx;
			paint = new Paint();
			paint.setDither(false);
			//mbitmap = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
			mbitmap = BitmapFactory.decodeResource(getResources(),R.drawable.som_gnome);
			holder = getHolder();
			mrect = new Rect(0,0,mbitmap.getWidth(),mbitmap.getHeight());
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
		}
		
		@Override
		public void run()
		{
			while(running){
				if(holder.getSurface().isValid()){
					Canvas canvas = holder.lockCanvas();
					Rect drawRect = calculateRect();
					canvas.drawBitmap(mbitmap, null, drawRect, paint);
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
		
		private Rect calculateRect()
		{
			return new Rect(mscreen.centerX() - mrect.width(),
							mscreen.centerY() - mrect.height(),
							mscreen.centerX() + mrect.width(),
							mscreen.centerY() + mrect.height());
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {

			touch_x = event.getX();
			touch_y = event.getY();

			int action = event.getAction();
			switch(action){
				case MotionEvent.ACTION_DOWN:
					touched = true;
					break;
				case MotionEvent.ACTION_MOVE:
					touched = true;
					break;
				case MotionEvent.ACTION_UP:
					touched = false;
					break;
				case MotionEvent.ACTION_CANCEL:
					touched = false;
					break;
				case MotionEvent.ACTION_OUTSIDE:
					touched = false;
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
