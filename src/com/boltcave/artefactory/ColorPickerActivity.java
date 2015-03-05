package com.boltcave.artefactory;

import com.boltcave.artefactory.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ColorPickerActivity extends Activity
{
	int mcolor;
	int tcolor;
	
	ColorDrawable mcolordraw;
	ColorDrawable tcolordraw;
	
	int mode;
	
	public static final int EDITMODE = 0;
	public static final int APPENDMODE = 1;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
	        mode = savedInstanceState.getInt("mode", APPENDMODE);
	        mcolor = savedInstanceState.getInt("color", Color.WHITE);
        } else {
        	Intent cpIntent = getIntent();
        	mode = cpIntent.getIntExtra("mode", APPENDMODE);
        	mcolor = cpIntent.getIntExtra("color", Color.WHITE);
        }
        tcolor = mcolor;	       
        setContentView(R.layout.colorpicker);
        
        ImageView startColor = (ImageView)findViewById(R.id.colorpicker_startcolor);
        ImageView currentColor = (ImageView)findViewById(R.id.colorpicker_currentcolor);
        
        mcolordraw = new ColorDrawable();
        tcolordraw = new ColorDrawable();
        mcolordraw.setColor(mcolor);
        tcolordraw.setColor(tcolor);
        
        startColor.setImageDrawable(mcolordraw);
        currentColor.setImageDrawable(tcolordraw);
	}
}
