package com.example.amber.view;

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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.amber.MainActivity;
import com.example.amber.R;

public class ViewReservedActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		String user=getUserID();
		
		new SlotFetchTask().execute("http://192.168.1.2:8888/parking/FinalYearProject/public/Park?id=0&&userid="+user);
		
	}

	private String getUserID() 
	{
		AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
    	Account req=manager.getAccountsByType("com.google")[0];
    	return req.name;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_reserved, menu);
		return true;
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if(keyCode== KeyEvent.KEYCODE_BACK)
            {
            	Intent intent = new Intent(ViewReservedActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
	private class SlotFetchTask extends AsyncTask<String, String, String>
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
			setContentView(R.layout.activity_view_reserved);
			ListView lvSlotsReserved=(ListView)findViewById(R.id.listView1);
			final ArrayList<String> list = new ArrayList<String>();
			
			JSONObject j=null;
			try 
			{
				j=new JSONObject(result);
				if(j.getString("SLOTS").isEmpty())
				{
					list.add("You have no reservations at the moment:");
				}
				else
				{
					JSONArray jarray=j.getJSONArray("SLOTS");
					for(int i=0;i<jarray.length();i++)
					{
						JSONObject ij=jarray.getJSONObject(i);
						String slot=ij.getString("SLOTID");
						String timeIn=ij.getString("TIMEIN");
						
						  list.add(slot+":"+timeIn);
					}
				}
			} 
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		    
		    final ViewReservationsAdapter adapter = new ViewReservationsAdapter(ViewReservedActivity.this,android.R.layout.simple_list_item_1, list);
		    lvSlotsReserved.setAdapter(adapter);
		    lvSlotsReserved.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
		        {
		        	Intent intent=new Intent(ViewReservedActivity.this,SlotOperationActivity.class);
		        	intent.putExtra("SLOTID", adapter.getItem(position));
		        	startActivity(intent);
		        }
		    });
	    }
		
	}
}
