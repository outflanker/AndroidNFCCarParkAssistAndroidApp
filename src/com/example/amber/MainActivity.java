package com.example.amber;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.amber.about.AboutActivity;
import com.example.amber.search.LayoutActivity;
import com.example.amber.view.ViewReservedActivity;

public class MainActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		ListView lvMainMenu=(ListView)findViewById(R.id.listView1);
		
		String[] values = new String[] {
				"Find Locations Nearby",
				"Your parking reservations",
				"About this App!"
		};

	    final ArrayList<String> list = new ArrayList<String>();
	    for (int i = 0; i < values.length; i++) 
	    {
	      list.add(values[i]);
	    }
	    final MainMenuAdapter adapter = new MainMenuAdapter(this,android.R.layout.simple_list_item_1, list);
	    lvMainMenu.setAdapter(adapter);
	    
	    lvMainMenu.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	        {
	            navigateList(position);
	        }

			private void navigateList(int position)
			{
				Intent intent=null;
				switch(position)
	            {
		            case 0:intent=new Intent(MainActivity.this, LayoutActivity.class);
		            	   startActivity(intent);
		            	   break;
		            case 1:intent=new Intent(MainActivity.this, ViewReservedActivity.class);
	            	   	   startActivity(intent);
	            	   	   break;
		            case 2:intent=new Intent(MainActivity.this, AboutActivity.class);
		            	   startActivity(intent);
		            	   break;
		            default:break;
	            }
				
			}
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
        	finish();
        	return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
