package com.boltcave.artefactory.Fragments;

import com.boltcave.artefactory.R;
import com.boltcave.artefactory.Views.ColorFieldView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HSVPicker extends Fragment
{
	private ColorFieldView swatch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.hsvpicker, container, false);
		swatch = (ColorFieldView)v.findViewById(R.id.colorFieldView1);
		swatch.setZOrderOnTop(true);
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		swatch.OnResumeColorFieldView();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		swatch.OnPauseColorFieldView();
	}
}
