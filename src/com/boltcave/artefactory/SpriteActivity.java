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
        
        //bmap = new BitmapView(this);
		//bmap.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//this.setContentView(bmap);
        setContentView(R.layout.main);
        
        bmap = (BitmapView)findViewById(R.id.bitmapView1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.actionbar_menu, menu);
    	return super.onCreateOptionsMenu(menu);
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
