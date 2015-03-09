package com.boltcave.artefactory;

import com.boltcave.artefactory.R;
import com.boltcave.artefactory.Fragments.HSVPicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ColorPickerActivity extends Activity
	implements OnColorChangeListener
{
	int mcolor;
	int tcolor;
	
	ColorDrawable mcolordraw;
	ColorDrawable tcolordraw;
	
	int mode;
	int pos;
	
	public static final int EDITMODE = 0;
	public static final int APPENDMODE = 1;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
	        mode = savedInstanceState.getInt("mode", APPENDMODE);
	        mcolor = savedInstanceState.getInt("color", Color.WHITE);
	        pos = savedInstanceState.getInt("position", 0);
        } else {
        	Intent cpIntent = getIntent();
        	mode = cpIntent.getIntExtra("mode", APPENDMODE);
        	mcolor = cpIntent.getIntExtra("color", Color.WHITE);
        	pos = cpIntent.getIntExtra("position", 0);
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
        
        currentColor.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent finalColor = new Intent();
				finalColor.putExtra("mode", mode);
				finalColor.putExtra("color", tcolor);
				finalColor.putExtra("position", pos);
				setResult(RESULT_OK, finalColor);
				finish();
			}       	
        });
        
        startColor.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		setResult(RESULT_CANCELED);
        		finish();
        	}
        });
	}

	@Override
	public void OnColorChanged(int color) {
		tcolor = color;
		tcolordraw.setColor(tcolor);		
	}

	public int getStartColor()
	{
		return mcolor;
	}
}
