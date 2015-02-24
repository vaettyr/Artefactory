package com.boltcave.artefactory;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class ColorPalette extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.palette);
		GridView gridview = (GridView) findViewById(R.id.colorpalette);
		gridview.setAdapter(new SwatchAdapter(this));
	}

}
