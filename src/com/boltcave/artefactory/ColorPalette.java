package com.boltcave.artefactory;
import android.app.*;
import android.graphics.Color;
import android.os.*;
import android.view.*;
import android.widget.*;

public class ColorPalette extends Fragment
{
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
	            colorChangeListener.onColorSelected((int)parent.getItemAtPosition(position));
	        }
	    });
		return v;
	}
}
