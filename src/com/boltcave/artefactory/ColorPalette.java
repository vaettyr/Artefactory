package com.boltcave.artefactory;
import android.app.*;
import android.os.*;
import android.view.*;

public class ColorPalette extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.palette, container, false);
		return v;
	}

}
