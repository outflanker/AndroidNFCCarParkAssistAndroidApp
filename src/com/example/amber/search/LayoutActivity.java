package com.example.amber.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amber.MainActivity;
import com.example.amber.R;

public class LayoutActivity extends Activity 
{
	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout);
		Location lastloc = getLocation();   
		
		if(null!=lastloc)
		{
			tv=(TextView)findViewById(R.id.textView2);
			tv.setText("Latitude"+lastloc.getLatitude());
			tv.setVisibility(View.INVISIBLE);
			tv=(TextView)findViewById(R.id.textView3);
			tv.setText("Longitude"+lastloc.getLongitude());
			tv.setVisibility(View.INVISIBLE);
		}
		
		new LayoutFetchTask().execute("http://192.168.1.2:8888/parking/FinalYearProject/public/Layout");
	}

	private Location getLocation() 
	{
		LocationManager locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		Criteria crit=new Criteria();
		String bestProv=locationManager.getBestProvider(crit, false);
		Location lastloc=locationManager.getLastKnownLocation(bestProv);
		return lastloc;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.layout, menu);
		return true;
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if(keyCode== KeyEvent.KEYCODE_BACK)
            {
            	Intent intent = new Intent(LayoutActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
	private class LayoutFetchTask extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute() 
		{
		      super.onPreExecute();
		      
		}
		@Override
		protected String doInBackground(String... arg0)
		{
			String res="Error";
			try 
			{
				URL url=new URL(arg0[0]);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setUseCaches(false);
		        connection.setAllowUserInteraction(false);
				connection.connect();
				
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) 
				{
					BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                StringBuilder sb = new StringBuilder();
	                String line;
	                while ((line = br.readLine()) != null) {
	                    sb.append(line+"\n");
	                }
	                br.close();
	                res=sb.toString();
		        }
				else
				{
					res="Conn Error";
		        }
			} 
			catch (MalformedURLException e) 
			{
				res=e.toString();
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				res=e.toString();
				e.printStackTrace();
			}
			return res;
		}
		@Override
	    protected void onPostExecute(String result) 
		{
			ListView lvSlotsReserved=(ListView)findViewById(R.id.listView1);
			final ArrayList<String> list = new ArrayList<String>();
			
			JSONObject j=null;
			try 
			{
				j=new JSONObject(result);
				JSONArray jarray=j.getJSONArray("LAYOUTS");
				for(int i=0;i<jarray.length();i++)
				{
					JSONObject ij=jarray.getJSONObject(i);
					String slot=ij.getString("LAYOUTID");
					String timeIn=ij.getString("LAYOUTNAME");
					list.add(slot+":"+timeIn);
				}
			} 
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		    
		    final LayoutAdapter adapter = new LayoutAdapter(LayoutActivity.this,android.R.layout.simple_list_item_1, list);
		    lvSlotsReserved.setAdapter(adapter);
		    lvSlotsReserved.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
		        {
		        	Intent intent=new Intent(LayoutActivity.this,LevelActivity.class);
		        	intent.putExtra("LAYOUTID", adapter.getItem(position));
		        	startActivity(intent);
		        }
		    });
	    }
		
	}
}
