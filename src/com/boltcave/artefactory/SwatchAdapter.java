package com.boltcave.artefactory;
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
    }

    public int getCount() {
        return mColors.length;
    }

    public Object getItem(int position) {
        return mColors[position];
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

        ColorDrawable colorswatch = new ColorDrawable();
        colorswatch.setColor(mColors[position]);
        imageView.setImageDrawable(colorswatch);
        return imageView;
    }

    // references to our images
    private Integer[] mColors = {
		Color.BLACK, Color.WHITE,
		Color.RED, Color.BLUE,
		Color.YELLOW, Color.GREEN
    };
}
