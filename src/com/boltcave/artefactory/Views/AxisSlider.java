package com.boltcave.artefactory.Views;

import com.boltcave.artefactory.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AxisSlider extends SurfaceView implements Runnable
{
	public static final int ORIENTATION_HORIZONTAL = 0;
	public static final int ORIENTATION_VERTICAL = 1;
	
	public static final int AXIS_STYLE_RED = 0;
	public static final int AXIS_STYLE_GREEN = 1;
	public static final int AXIS_STYLE_BLUE = 2;
	public static final int AXIS_STYLE_ALPHA = 3;
	public static final int AXIS_STYLE_HUE = 4;
	
	private Thread mthread;
	private SurfaceHolder holder;
	volatile boolean running = false;
	
	private Paint axisPaint;	
	private Drawable axisCursor;
	private int cursorsize;
	private int axisStyle;
	private int orientation;
	
	private Rect viewRect;

	private OnAxisSliderChangeListener callback;
	
	volatile int basecolor;
	volatile float cursor_pos;
	
	public void SetOnAxisSliderChangeListener(OnAxisSliderChangeListener listener)
	{
		callback = listener;
	}

	public void setColor(int color)
	{
		basecolor = color;
		setShader();
		setCursor();
	}
	
	public AxisSlider(Context ctx) 
	{
		super(ctx);
		Init();
	}

	public AxisSlider(Context ctx, AttributeSet attrs)
	{
		super(ctx, attrs);
		TypedArray a = ctx.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.colorfieldview,
				0, 0);
		try {
			axisCursor = ctx.getResources().getDrawable(a.getResourceId(R.styleable.colorfieldview_cursorid, -1));
			cursorsize = a.getInteger(R.styleable.colorfieldview_cursorsize, 20);
		}
		finally {
			a.recycle();
		}
		TypedArray b = ctx.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.axisslider,
				0,0);
		try {
			axisStyle = b.getInteger(R.styleable.axisslider_axisstyle, AXIS_STYLE_HUE);
			orientation = b.getInteger(R.styleable.axisslider_orientation, ORIENTATION_HORIZONTAL);
		} finally {
			b.recycle();
		}
		
		Init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		switch(orientation)
		{
			case ORIENTATION_HORIZONTAL:
				height = cursorsize;
				break;
			case ORIENTATION_VERTICAL:
				width = cursorsize;
				break;
		}
		setMeasuredDimension(width, height);
		getDrawingRect(viewRect);		
		setShader();
		setCursor();
	}
	
	protected void setShader()
	{
		switch(axisStyle)
		{
			case AXIS_STYLE_RED:
				axisPaint.setShader(getGradient(new int[]{Color.BLACK, Color.RED}));
				break;
			case AXIS_STYLE_GREEN:
				axisPaint.setShader(getGradient(new int[]{Color.BLACK, Color.GREEN}));
				break;
			case AXIS_STYLE_BLUE:
				axisPaint.setShader(getGradient(new int[]{Color.BLACK, Color.BLUE}));
				break;
			case AXIS_STYLE_ALPHA:
				int noalpha = Color.rgb(Color.red(basecolor), Color.green(basecolor), Color.blue(basecolor));
				axisPaint.setShader(getGradient(new int[]{0x00000000, noalpha}));
				break;
			case AXIS_STYLE_HUE:
				axisPaint.setShader(getGradient(new int[]{0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 
												0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF, 0xFFFF0000}));
				break;
		}
	}
	
	private LinearGradient getGradient(int[] colors)
	{
		return new LinearGradient(0f,0f,
				(orientation == ORIENTATION_HORIZONTAL)?getWidth():0f,
				(orientation == ORIENTATION_HORIZONTAL)?0f:getHeight(),
				colors,
				null,
				Shader.TileMode.CLAMP);
	}
	
	protected void setCursor()
	{
		switch(axisStyle)
		{
			case AXIS_STYLE_RED:
				cursor_pos = ((orientation == ORIENTATION_HORIZONTAL)?getWidth():getHeight()) * Color.red(basecolor)/255;
				break;
			case AXIS_STYLE_GREEN:
				cursor_pos = ((orientation == ORIENTATION_HORIZONTAL)?getWidth():getHeight()) * Color.green(basecolor)/255;
				break;
			case AXIS_STYLE_BLUE:
				cursor_pos = ((orientation == ORIENTATION_HORIZONTAL)?getWidth():getHeight()) * Color.blue(basecolor)/255;
				break;
			case AXIS_STYLE_ALPHA:
				cursor_pos = ((orientation == ORIENTATION_HORIZONTAL)?getWidth():getHeight()) * Color.alpha(basecolor)/255;
				break;
			case AXIS_STYLE_HUE:
				float[] hsv = new float[3];
				Color.colorToHSV(basecolor, hsv);
				cursor_pos = ((orientation == ORIENTATION_HORIZONTAL)?getWidth():getHeight()) * hsv[0]/360;
				break;
		}
	}
	
	private void Init()
	{
		holder = getHolder();
		axisPaint = new Paint();
		viewRect = new Rect();
		cursor_pos = 0;
	}
	
	@Override
	public void run() {
		while(running){
			if(holder.getSurface().isValid()){
				Canvas canvas = holder.lockCanvas();
				canvas.drawColor(0, PorterDuff.Mode.CLEAR);

				//draw the gradient
				canvas.drawRect(viewRect, axisPaint);
								
				//draw the cursor	
				int halfwidth = cursorsize/2;
				switch(orientation)
				{
					case ORIENTATION_HORIZONTAL:
						axisCursor.setBounds((int)(cursor_pos - halfwidth), 0, 
											(int)(cursor_pos + halfwidth), cursorsize);
						break;
					case ORIENTATION_VERTICAL:
						axisCursor.setBounds(0, (int)(cursor_pos - halfwidth),
								    cursorsize, (int)(cursor_pos + halfwidth));
						break;
				}
				axisCursor.draw(canvas);
				
				holder.unlockCanvasAndPost(canvas);
			}
		}	
	}

	public void OnResumeSliderView()
	{
		running = true;
		mthread = new Thread(this);
		mthread.start();
	}
	
	public void OnPauseSliderView()
	{
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
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch(action){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				switch(orientation)
				{
				case ORIENTATION_HORIZONTAL:
					cursor_pos = Math.max(0,  Math.min(getWidth(), event.getX()));
					break;
				case ORIENTATION_VERTICAL:
					cursor_pos = Math.max(0,  Math.min(getHeight(), event.getY()));
					break;
				}
				if(callback != null)
				{
					callback.onAxisSliderChanged(cursor_pos/
							((orientation == ORIENTATION_HORIZONTAL)?getWidth():getHeight()));
				}
				break;
			/*
			case MotionEvent.ACTION_UP:				
			case MotionEvent.ACTION_CANCEL:				
			case MotionEvent.ACTION_OUTSIDE:
			default:
			*/
		}
		return true; //processed
	}
}
