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
	
	volatile int currentColor;
	volatile float cursor_x, cursor_y;
	volatile boolean running = false;

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
			currentColor = a.getColor(R.styleable.colorfieldview_color, Color.BLACK);
			mCursor = ctx.getResources().getDrawable(a.getResourceId(R.styleable.colorfieldview_cursorid, -1));
			cursorsize = a.getInteger(R.styleable.colorfieldview_cursorsize, 20);
		}
		finally {
			a.recycle();
		}
		
		Init();
	}

	private void Init()
	{
		holder = getHolder();
		satPaint = new Paint();
		valPaint = new Paint();
		viewRect = new Rect();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		getDrawingRect(viewRect);
		setShader();
	}
	
	protected void setShader()
	{
		float[] hsv = new float[3];
		Color.colorToHSV(currentColor, hsv);
		int rootColor = Color.HSVToColor(new float[]{hsv[0],1,1});
		satPaint.setShader(new LinearGradient(viewRect.left,0,viewRect.right,0,Color.WHITE, rootColor,Shader.TileMode.CLAMP));
		valPaint.setShader(new LinearGradient(0, viewRect.top, 0, viewRect.bottom, 0x00000000, Color.BLACK, Shader.TileMode.CLAMP));
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
				mCursor.setBounds(0, 0, 10, 10);
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
}
