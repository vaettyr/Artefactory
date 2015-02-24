package com.boltcave.artefactory;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.database.*;

public class SwatchAdapter implements ListAdapter
{

	@Override
	public void registerDataSetObserver(DataSetObserver p1)
	{
		// TODO: Implement this method
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver p1)
	{
		// TODO: Implement this method
	}

	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return 0;
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return 0;
	}

	@Override
	public boolean hasStableIds()
	{
		// TODO: Implement this method
		return false;
	}

	@Override
	public View getView(int p1, View p2, ViewGroup p3)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public int getItemViewType(int p1)
	{
		// TODO: Implement this method
		return 0;
	}

	@Override
	public int getViewTypeCount()
	{
		// TODO: Implement this method
		return 0;
	}

	@Override
	public boolean isEmpty()
	{
		// TODO: Implement this method
		return false;
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		// TODO: Implement this method
		return false;
	}

	@Override
	public boolean isEnabled(int p1)
	{
		// TODO: Implement this method
		return false;
	}

    private Context mContext;

    public SwatchAdapter(Context c) {
        mContext = c;
    }

    

    // references to our images
    private Integer[] mColors = {
		Color.BLACK, Color.WHITE,
		Color.RED, Color.BLUE,
		Color.YELLOW, Color.GREEN
    };
}
