package com.boltcave.artefactory;

import android.content.Context;
import android.graphics.*;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.boltcave.artefactory.R;
import android.os.*;

public class BitmapView extends SurfaceView implements Runnable
{
	public Bitmap mbitmap;
	private Paint fillPaint;
	
	private Paint paint;
	
	public Rect mscreen, mrect;
	private Context context;
	
	private Thread mthread;
	private SurfaceHolder holder;
	private SurfaceTool tool;
	volatile boolean running = false, set_offset = false;
	
	volatile float offset_x, offset_y, offset_zoom;
	
	public BitmapView(Context ctx)
	{
		super(ctx);
		init(ctx);
	}
	
	public BitmapView(Context ctx, AttributeSet attrs)
	{
		super(ctx, attrs);
		init(ctx);
		
	}
	
	public BitmapView(Context ctx, AttributeSet attrs, int defStyle)
	{
		super(ctx, attrs, defStyle);
		init(ctx);
	}
	
	private void init(Context ctx)
	{
		context = ctx;
		paint = new Paint();
		paint.setDither(false);
		
		//mbitmap = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inMutable = true;
		mbitmap = BitmapFactory.decodeResource(getResources(),R.drawable.som_gnome, opts);
		
		Bitmap fillMap = BitmapFactory.decodeResource(getResources(), R.drawable.pattern_fill);
		Shader fillShader = new BitmapShader(fillMap, TileMode.REPEAT, TileMode.REPEAT);
		
		fillPaint = new Paint();
		fillPaint.setShader(fillShader);
		
		holder = getHolder();
		
		//tool = new PanZoomTool(this);
		tool = new PixelTool(this);
		mrect = new Rect(0,0,mbitmap.getWidth(),mbitmap.getHeight());
		mscreen = new Rect();
		offset_zoom = 10f;
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
		getDrawingRect(mscreen);
		if(!set_offset)
		{
			offset_x = mscreen.centerX();
			offset_y = mscreen.centerY();
			set_offset = true;
		}
	}
	
	@Override
	public void run()
	{
		while(running){
			if(holder.getSurface().isValid()){
				Canvas canvas = holder.lockCanvas();
				canvas.drawColor(0, PorterDuff.Mode.CLEAR);
				//canvas.drawColor(Color.BLACK);
				Rect drawRect = calculateRect();
				canvas.drawRect(drawRect, fillPaint);
				canvas.drawBitmap(mbitmap, null, drawRect, paint);
				
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
	protected Parcelable onSaveInstanceState()
	{

		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putFloat("offset_x", this.offset_x);
		bundle.putFloat("offset_y", this.offset_y);
		bundle.putBoolean("set_offset", this.set_offset);
		bundle.putFloat("offset_zoom", this.offset_zoom);
		// ... save everything
		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {

		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			this.offset_x = bundle.getFloat("offset_x");
			this.offset_y = bundle.getFloat("offset_y");
			this.set_offset = bundle.getBoolean("set_offset");
			this.offset_zoom = bundle.getFloat("offset_zoom");
			// ... load everything
			state = bundle.getParcelable("instanceState");
		}
		super.onRestoreInstanceState(state);
	}
	
 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch(action){
			case MotionEvent.ACTION_DOWN:
				tool.handleTouchStart(event);
				break;
			case MotionEvent.ACTION_MOVE:
				tool.handleTouchChange(event);
				break;
			case MotionEvent.ACTION_UP:
				tool.handleTouchEnd(event);
				break;
			case MotionEvent.ACTION_CANCEL:
				tool.handleTouchCancel(event);
				break;
			case MotionEvent.ACTION_OUTSIDE:
				tool.handleTouchCancel(event);
				break;
			default:
		}
		return true; //processed
	}

	public void setTool(SurfaceTool tl)
	{
		this.tool = tl;
	}
}
