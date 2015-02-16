package com.boltcave.artefactory;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.widget.AbsListView.*;
import javax.security.auth.*;

import com.boltcave.artefactory.R;

public class SpriteActivity extends Activity
{		
	BitmapView bmap;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        bmap = new BitmapView(this);
		bmap.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setContentView(bmap);
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bmap.onResumeBitmapView();
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		bmap.onPauseBitmapView();
	}
	
}
