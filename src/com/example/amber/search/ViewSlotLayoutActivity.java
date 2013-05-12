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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.example.amber.R;

public class ViewSlotLayoutActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_slot_layout);
		
		Intent i=getIntent();
		String input[]=i.getStringExtra("ID").split(":");
		String layout=input[0];
		String layer=input[1];
		String size=input[2];
		new SlotLayoutFetchTask().execute("http://192.168.1.2:8888/parking/FinalYearProject/public/Slot?id=0&&layoutid="+layout+"&&layerid="+layer,size);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_slot_layout, menu);
		return true;
	}
	private class SlotLayoutFetchTask extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute() 
		{
		      super.onPreExecute();
		      setContentView(R.layout.activity_view_slot_layout);
				
				TextView textView = (TextView)findViewById(R.id.textView1);
			    textView.setText("Loading..");
				
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
			return res+"::size"+arg0[1];
		}
		@Override
	    protected void onPostExecute(String result) 
		{
			String input[]=result.split("::size");
			result=input[0];
			String size=input[1];
			setContentView(R.layout.activity_view_slot_layout);
		
		    final ArrayList<Integer> types=new ArrayList<Integer>();
		    JSONObject j=null;
			try 
			{
				j=new JSONObject(result);
				JSONArray jarray=j.getJSONArray("SLOTS");
				for(int i=0;i<jarray.length();i++)
				{
					JSONObject ij=jarray.getJSONObject(i);
					String slot=ij.getString("SLOTTYPE");
					types.add(Integer.valueOf(slot));
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			TextView tv=(TextView)findViewById(R.id.textView1);
			int col=Integer.parseInt(size);
			col=col%10;
			tv.setText(types.toString()+col);
			tv.setVisibility(View.INVISIBLE);
			GridView gridview = (GridView) findViewById(R.id.gridView1);
			gridview.setNumColumns(col);
			gridview.setAdapter(new SlotLayoutAdapter(ViewSlotLayoutActivity.this,types,col));
		}
	
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if(keyCode== KeyEvent.KEYCODE_BACK)
            {
            	Intent intent = new Intent(ViewSlotLayoutActivity.this, LayoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
