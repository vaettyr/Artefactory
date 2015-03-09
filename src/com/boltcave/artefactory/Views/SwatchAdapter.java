package com.boltcave.artefactory.Views;
import com.boltcave.artefactory.R;

import android.widget.*;
import android.content.*;
import android.view.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.database.*;

public class SwatchAdapter extends BaseAdapter {
    private Context mContext;

    public SwatchAdapter(Context c) {
        mContext = c;
		mColors = new int[0];
    }

    public int getCount() {
        return mColors.length + 1;
    }

    public Object getItem(int position) {
        if(position < mColors.length){
        	return mColors[position];
        } else {
        	return -1;
        }
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        if(position < mColors.length){
	        ColorDrawable colorswatch = new ColorDrawable();
	        colorswatch.setColor(mColors[position]);
	        imageView.setImageDrawable(colorswatch);
        } else {
        	imageView.setImageResource(R.drawable.som_gnome); 	
        }
        return imageView;
    }

    // references to our images
    private int[] mColors;
	
	public int[] getColors(){
		return mColors;
	}
	
	public void setColors(int[] colors){
		mColors = colors;
	}
	
	public void setColor(int color, int position)
	{
		mColors[position] = color;
		notifyDataSetChanged();
	}
	
	public void addColor(int color){
		int[] tcolors = new int[mColors.length + 1];
		for(int i = 0; i < mColors.length; i++){
			tcolors[i] = mColors[i];
		}
		tcolors[mColors.length] = color;
		mColors = tcolors;
		notifyDataSetChanged();
	}
}
