package com.boltcave.artefactory;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.RadioGroup.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.AbsListView.*;
import javax.security.auth.*;

import com.boltcave.artefactory.R;
import com.boltcave.artefactory.Fragments.ColorPalette;
import com.boltcave.artefactory.Fragments.ColorPalette.OnColorSelectedListener;
import com.boltcave.artefactory.R.id;
import com.boltcave.artefactory.R.layout;
import com.boltcave.artefactory.R.menu;
import com.boltcave.artefactory.Tools.PanZoomTool;
import com.boltcave.artefactory.Tools.PixelTool;
import com.boltcave.artefactory.Views.BitmapView;

public class SpriteActivity extends Activity
	implements ColorPalette.OnColorSelectedListener
{		
	BitmapView bmap;
	RadioGroup toolpalette;
	BitmapUndoStack undostack;
	ImageButton palbtn;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
		//undostack = new BitmapUndoStack(this, savedInstanceState);

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
    
        palbtn = (ImageButton)findViewById(R.id.imageButton1);
        palbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {		
				FragmentManager fm = getFragmentManager();
				Fragment paletteFrag = fm.findFragmentById(R.id.colorpalette);
				if(paletteFrag != null)
				{
					if(paletteFrag.isVisible())
					{
						fm.beginTransaction()
				          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
				          .hide(paletteFrag)
				          .commit();
					}
					else
					{
						fm.beginTransaction()
				          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
				          .show(paletteFrag)
				          .commit();
					}
				}
			}    	
        });
        palbtn.setOnLongClickListener(new View.OnLongClickListener() {	
			@Override
			public boolean onLongClick(View v) {				
				Intent colorIntent = new Intent(v.getContext(), ColorPickerActivity.class);
				startActivity(colorIntent);
				return true;
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
		return true;
	}
    
	@Override
	protected void onResume() {
		super.onResume();
		bmap.onResumeBitmapView();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		bmap.onPauseBitmapView();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		//outState = undostack.saveInstanceState(outState);
	}
	
	public void onColorSelected(int color)
	{
		bmap.setColor(color);
		ColorDrawable colorswatch = new ColorDrawable();
        colorswatch.setColor(color);
        palbtn.setImageDrawable(colorswatch);
        FragmentManager fm = getFragmentManager();
        Fragment paletteFrag = getFragmentManager().findFragmentById(R.id.colorpalette);
		if(paletteFrag != null)
		{
			if(paletteFrag.isVisible())
			{
				fm.beginTransaction()
		          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
		          .hide(paletteFrag)
		          .commit();
			}
		}
	}
}
