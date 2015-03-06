package com.boltcave.artefactory.Fragments;

import com.boltcave.artefactory.OnColorChangeListener;
import com.boltcave.artefactory.R;
import com.boltcave.artefactory.Views.*;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HSVPicker extends Fragment
{
	private ColorFieldView swatch;
	private AxisSlider hueslider;
	
	private volatile float hue, saturation, value;

	private OnColorChangeListener callback;
	
	@Override
 	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.hsvpicker, container, false);
		
		swatch = (ColorFieldView)v.findViewById(R.id.colorFieldView1);
		swatch.setZOrderOnTop(true);
		//set base color and so forth
		swatch.setOnColorFieldChangeListener(new OnColorFieldChangeListener(){
			@Override
			public void onColorFieldChanged(float x, float y) {
				saturation = x;
				value = 1 - y;
				callback.OnColorChanged(Color.HSVToColor(new float[]{hue, saturation, value}));
			}	
		});
		
		hueslider = (AxisSlider)v.findViewById(R.id.axisSlider1);
		hueslider.setZOrderOnTop(true);
		hueslider.SetOnAxisSliderChangeListener(new OnAxisSliderChangeListener(){
			@Override
			public void onAxisSliderChanged(float val) {
				hue = val * 360;
				swatch.setHue(hue);
				callback.OnColorChanged(Color.HSVToColor(new float[]{hue, saturation, value}));
			}		
		});
		
		return v;
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		callback = (OnColorChangeListener)activity;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		swatch.OnResumeColorFieldView();
		hueslider.OnResumeSliderView();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		swatch.OnPauseColorFieldView();
		hueslider.OnPauseSliderView();
	}
}
