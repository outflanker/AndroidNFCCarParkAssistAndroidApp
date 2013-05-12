package com.example.amber.unregister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;

import com.example.amber.R;
import com.example.amber.view.ViewReservedActivity;

public class UnregisteredActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i=getIntent();
		final String slot=i.getStringExtra("SLOTID");
		new UnregisterSlotTask().execute(slot);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.unregistered, menu);
		return true;
	}
	private class UnregisterSlotTask extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute() 
		{
		      super.onPreExecute();
		      TextView textView = new TextView(UnregisteredActivity.this);
			  textView.setTextSize(40);
			  textView.setText("Loading..");
		      
		}
		@Override
		protected String doInBackground(String... arg0)
		{
			String res="Error";
			String jsonSend="{\"SLOTID\":\""+arg0[0]+"\"}";
			try 
			{
				URL url=new URL("http://192.168.1.2:8888/parking/FinalYearProject/public/Unregister");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("PUT");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
		        connection.setAllowUserInteraction(false);
		        
		        connection.setRequestProperty("Content-Type", "application/json");
	            connection.setRequestProperty("Accept", "application/json");
	            
	            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
	            osw.write(jsonSend);
	            osw.flush();
	            osw.close();
	            
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
			setContentView(R.layout.activity_unregistered);
			TextView tv=(TextView)findViewById(R.id.textView1);
			tv.setText(result);
	    }
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if(keyCode== KeyEvent.KEYCODE_BACK)
            {
            	Intent intent = new Intent(UnregisteredActivity.this, ViewReservedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
