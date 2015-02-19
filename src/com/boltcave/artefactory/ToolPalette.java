package com.boltcave.artefactory;

import android.content.*;
import android.util.*;
import android.widget.*;
import android.view.*;
import android.view.View.OnClickListener;

public class ToolPalette extends LinearLayout implements OnClickListener 
{
	private Context ctx;
	public ToolPalette(Context ctx)
	{
		super(ctx);
		this.ctx = ctx;
	}
	
	public ToolPalette(Context ctx, AttributeSet attrs)
	{
		super(ctx, attrs);
		this.ctx = ctx;
	}
	
	@Override
	public void onFinishInflate()
	{
		super.onFinishInflate();
		//add the click event listener
		int buttonCount = getChildCount();
		for(int i=0; i<buttonCount; i++)
		{
			getChildAt(i).setOnClickListener(this);
		}
	}
	
	@Override
	public void onClick(View v)
	{	
		//Log.d("Artefactory", "Button pressed");
		Toast.makeText(this.ctx, "you pressed a button", Toast.LENGTH_SHORT).show();
	}
}
