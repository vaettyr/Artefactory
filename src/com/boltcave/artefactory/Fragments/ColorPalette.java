package com.boltcave.artefactory.Fragments;
import com.boltcave.artefactory.ColorPickerActivity;
import com.boltcave.artefactory.R;
import com.boltcave.artefactory.R.layout;
import com.boltcave.artefactory.Views.SwatchAdapter;

import android.app.*;
import android.content.Intent;
import android.graphics.Color;
import android.os.*;
import android.view.*;
import android.widget.*;

public class ColorPalette extends Fragment
{
	static final int COLOR_REQUEST = 1;
	OnColorSelectedListener colorChangeListener;
	
	public interface OnColorSelectedListener
	{
		public void onColorSelected(int color);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		colorChangeListener = (OnColorSelectedListener) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.palette, container, false);
		GridView gridview = (GridView) v;
		gridview.setAdapter(new SwatchAdapter(this.getActivity()));
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	int selection = (int)parent.getItemAtPosition(position);
	        	if(selection != -1){
	        		colorChangeListener.onColorSelected(selection);
	        	} else {
	        		Intent colorIntent = new Intent(v.getContext(), ColorPickerActivity.class);
	        		colorIntent.putExtra("mode", ColorPickerActivity.APPENDMODE);
	        		colorIntent.putExtra("color", Color.BLACK);
					startActivityForResult(colorIntent, COLOR_REQUEST);
	        	}
	        }
	    });
		return v;
	}
}
