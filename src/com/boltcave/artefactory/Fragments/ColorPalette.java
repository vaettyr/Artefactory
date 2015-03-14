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

import java.security.*;

public class ColorPalette extends Fragment
{
	static final int COLOR_REQUEST = 1;
	OnColorSelectedListener colorChangeListener;
	private SwatchAdapter adapter;
	private GridView grid;
	
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
		grid = (GridView) v;
		adapter = new SwatchAdapter(this.getActivity());
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	int selection = parent.getItemAtPosition(position);
				if(position != parent.getCount()-1){
	        		colorChangeListener.onColorSelected(selection);
	        	} else {
	        		Intent colorIntent = new Intent(v.getContext(), ColorPickerActivity.class);
	        		colorIntent.putExtra("mode", ColorPickerActivity.APPENDMODE);
	        		colorIntent.putExtra("color", Color.WHITE);
					startActivityForResult(colorIntent, COLOR_REQUEST);
	        	}
	        }
	    });
		grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				int editcolor = adapter.getItem(position);
				Intent editIntent = new Intent(v.getContext(), ColorPickerActivity.class);
				editIntent.putExtra("mode", ColorPickerActivity.EDITMODE);
				editIntent.putExtra("color", editcolor);
				editIntent.putExtra("position",  position);
				startActivityForResult(editIntent, COLOR_REQUEST);
				return true;
			}			
		});
		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == COLOR_REQUEST){
			if(resultCode == ColorPickerActivity.RESULT_OK)
			{
				int mode = data.getIntExtra("mode", ColorPickerActivity.APPENDMODE);
				int color = data.getIntExtra("color", Color.WHITE);
				int position = data.getIntExtra("position", 0);
				switch(mode)
				{
					case ColorPickerActivity.APPENDMODE:
						adapter.addColor(color);
						break;
					case ColorPickerActivity.EDITMODE:
						adapter.setColor(color, position);
						colorChangeListener.onColorSelected(color);
						break;
				}
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null)
		{
			int[] initColors = savedInstanceState.getIntArray("colors");
			adapter.setColors(initColors);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putIntArray("colors", adapter.getColors());
	}
}
