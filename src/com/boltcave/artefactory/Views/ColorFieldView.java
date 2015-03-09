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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.*;

public class ColorFieldView extends SurfaceView implements Runnable
{
	private Thread mthread;
	private SurfaceHolder holder;
	private Paint satPaint;
	private Paint valPaint;
	
	private Drawable mCursor;
	private int cursorsize;
	
	private Rect viewRect;

	private OnColorFieldChangeListener callback;
	
	volatile int currentColor;
	volatile float cursor_x, cursor_y;
	volatile boolean running = false;
	
	public void setOnColorFieldChangeListener(OnColorFieldChangeListener listener)
	{
		callback = listener;
	}
	
	public ColorFieldView(Context ctx) {
		super(ctx);
		currentColor = Color.WHITE;
		mCursor = new ColorDrawable();
		cursorsize = 20;
		Init();
	}
	
	public ColorFieldView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		TypedArray a = ctx.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.colorfieldview,
				0, 0);
		try {
			mCursor = ctx.getResources().getDrawable(a.getResourceId(R.styleable.colorfieldview_cursorid, -1));
			cursorsize = a.getInteger(R.styleable.colorfieldview_cursorsize, 20);
		}
		finally {
			a.recycle();
		}
		currentColor = Color.WHITE;
		Init();
	}

	public void setColor(int color)
	{		
		currentColor = color;
		setShader();
		setCursor();
	}
	
	public void setHue(float hue)
	{
		currentColor = Color.HSVToColor(new float[]{hue, cursor_x/getWidth(), cursor_y/getHeight()});
		setShader();
	}
	
	private void Init()
	{
		holder = getHolder();
		satPaint = new Paint();
		valPaint = new Paint();
		viewRect = new Rect();
		cursor_x = cursor_y = 0;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		getDrawingRect(viewRect);
		setShader();
		setCursor();
	}
	
	protected void setShader()
	{
		float[] hsv = new float[3];
		Color.colorToHSV(currentColor, hsv);
		int rootColor = Color.HSVToColor(new float[]{hsv[0],1,1});
		satPaint.setShader(new LinearGradient(viewRect.left,0,viewRect.right,0,Color.WHITE, rootColor,Shader.TileMode.CLAMP));
		valPaint.setShader(new LinearGradient(0, viewRect.top, 0, viewRect.bottom, 0x00000000, Color.BLACK, Shader.TileMode.CLAMP));
	}
	
	protected void setCursor()
	{
		float[] hsv = new float[3];
		Color.colorToHSV(currentColor, hsv);
		cursor_x = hsv[1] * getWidth();
		cursor_y = (1 - hsv[2]) * getHeight();
	}
	
	@Override
	public void run() {
		while(running){
			if(holder.getSurface().isValid()){
				Canvas canvas = holder.lockCanvas();
				canvas.drawColor(0, PorterDuff.Mode.CLEAR);

				//draw the gradient
				canvas.drawRect(viewRect, satPaint);
				canvas.drawRect(viewRect, valPaint);
				//draw the cursor	
				int halfwidth = cursorsize/2;
				mCursor.setBounds((int)(cursor_x - halfwidth), (int)(cursor_y - halfwidth), 
								  (int)(cursor_x + halfwidth), (int)(cursor_y + halfwidth));
				//mCursor.setBounds(0,0,getWidth(),getHeight());
				mCursor.draw(canvas);
				
				holder.unlockCanvasAndPost(canvas);
			}
		}	
	}
		
 	public void OnResumeColorFieldView()
	{
		running = true;
		mthread = new Thread(this);
		mthread.start();
	}
	
	public void OnPauseColorFieldView()
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
				cursor_x = Math.max(0, Math.min(getWidth(),event.getX()));
				cursor_y = Math.max(0, Math.min(getHeight(), event.getY()));
				if(callback != null)
				{
					callback.onColorFieldChanged(cursor_x/getWidth(), cursor_y/getHeight());
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
