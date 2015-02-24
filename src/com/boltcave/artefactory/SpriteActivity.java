package com.boltcave.artefactory;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.RadioGroup.*;
import android.content.*;
import android.graphics.*;
import android.widget.AbsListView.*;
import javax.security.auth.*;

import com.boltcave.artefactory.R;

public class SpriteActivity extends Activity
{		
	BitmapView bmap;
	RadioGroup toolpalette;
	BitmapUndoStack undostack;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
		//undostack = new BitmapUndoStack(this, savedInstanceState);
      
		//bmap = new BitmapView(this);
		//bmap.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//this.setContentView(bmap);
        setContentView(R.layout.main);
        
        bmap = (BitmapView)findViewById(R.id.bitmapView1);
        toolpalette = (RadioGroup)findViewById(R.id.toolPalette);
        toolpalette.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup palette, int btnId) {
				// TODO Auto-generated method stub
				switch(btnId)
				{
					case R.id.btnDrawPixel:
						bmap.setTool(new PixelTool(bmap));
						break;
					case R.id.btnPanZoom:
						bmap.setTool(new PanZoomTool(bmap));
						break;
				}
			}	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.actionbar_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent paletteIntent = new Intent(this, ColorPalette.class);
		startActivity(paletteIntent);
		return true;
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

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		//outState = undostack.saveInstanceState(outState);
	}
	
}
