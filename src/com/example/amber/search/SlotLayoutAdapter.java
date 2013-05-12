package com.example.amber.search;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.amber.R;


public class SlotLayoutAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] mThumbIds;
    private int columns;
    private Integer Icon0=R.drawable.car_0;
    private Integer Icon1=R.drawable.car_2;
    private Integer Icon2=R.drawable.car_4;
    
    public SlotLayoutAdapter(Context c,ArrayList<Integer> types,int numColumns) 
    {
        mContext = c;
        initValues(types);
        columns=numColumns;
    }

    private void initValues(ArrayList<Integer> types)
    {
    	mThumbIds=new Integer[types.size()];
		for(int i=0;i<types.size();i++)
		{
			int val=types.get(i);
			switch(val)
			{
			case 0:mThumbIds[i]=Icon0;
					break;
			case 1:mThumbIds[i]=Icon1;
					break;
			case 2:mThumbIds[i]=Icon2;
					break;
			default:break;
			}
		}
	}

	public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams((int)750/columns, 750/columns));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }
}