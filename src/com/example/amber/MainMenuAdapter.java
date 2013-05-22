package com.example.amber;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainMenuAdapter extends ArrayAdapter<String>
{
	HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public MainMenuAdapter(Context context, int textViewResourceId,List<String> objects)
    {
      super(context, textViewResourceId, objects);
      for (int i = 0; i < objects.size(); ++i)
      {
        mIdMap.put(objects.get(i), i);
      }
    }

    @Override
    public long getItemId(int position) 
    {
      String item = getItem(position);
      return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = super.getView(position, convertView, parent);
            ((TextView) v).setTextColor(Color.WHITE); 
            ((TextView) v).setTextSize(20);
        return v;
    }
} 
