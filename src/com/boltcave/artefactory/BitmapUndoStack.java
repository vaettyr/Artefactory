package com.boltcave.artefactory;
import android.graphics.*;
import java.util.*;
import android.content.*;
import android.os.*;

public class BitmapUndoStack
{
	private Bitmap temp;
	private ArrayList<Bitmap> stack;
	private Context context;
	private int step;
	
	public BitmapUndoStack(Context ctx, Bundle savedstate)
	{
		this.context = ctx;
		this.temp = savedstate.getParcelable("temp");
		this.stack = savedstate.getParcelableArrayList("stack");
		if(this.stack == null)
		{
			this.stack = new ArrayList<Bitmap>(10);
		}
		this.step = savedstate.getInt("step");
	}
	
	public BitmapUndoStack(Context ctx)
	{
		this.context = ctx;
		this.stack = new ArrayList<Bitmap>(10);
	}
	
	public Bundle saveInstanceState(Bundle bundle)
	{
		bundle.putParcelable("temp", this.temp);
		bundle.putParcelableArrayList("stack", this.stack);
		bundle.putInt("step", this.step);
		return bundle;
		
	}
	
	public void hold(Bitmap b)
	{
		this.temp = b;
	}
	
	public void commit()
	{
		if(this.temp != null)
		{
			this.stack.subList(0, this.step).clear();
			this.stack.add(0, this.temp);
			this.stack.trimToSize();
		}
	}
	
	public Bitmap undo()
	{
		this.step++;
		return this.stack.get(this.step);
	}
	
	public Bitmap redo()
	{
		this.step--;
		return this.stack.get(this.step);
	}
	
	public boolean canUndo()
	{
		if(this.step < this.stack.size()-1)
			return true;
		return false;
	}
	
	public boolean canRedo()
	{
		if(this.step > 1)
			return true;
		return false;
	}
}
