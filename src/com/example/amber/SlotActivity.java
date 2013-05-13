package com.example.amber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class SlotActivity extends Activity 
{
	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slot);
		textView=(TextView)findViewById(R.id.textView2);
		textView.setText(getUserID());
		textView.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slot, menu);
		return true;
	}
	@Override
	public void onResume() {
	    super.onResume();
	    if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) 
	    {
	    	processIntent(getIntent());
	    }
	}
	@Override
    public void onNewIntent(Intent intent)
	{
        setIntent(intent);
    }
	private String getUserID() 
	{
		AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
    	Account req=manager.getAccountsByType("com.google")[0];
    	return req.name;
	}
	void processIntent(Intent intent)
	{
        textView = (TextView) findViewById(R.id.textView1);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String payload=new String(msg.getRecords()[0].getPayload());
        payload=payload.split("en")[1];
        textView.setText(payload);
        textView.setVisibility(View.INVISIBLE);
        new RegisterSlotTask().execute(payload+":"+getUserID());
    }
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if(keyCode== KeyEvent.KEYCODE_BACK)
            {
            	Intent intent = new Intent(SlotActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
	private class RegisterSlotTask extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute() 
		{
		      super.onPreExecute();
		      TextView textView = new TextView(SlotActivity.this);
			  textView.setTextSize(40);
			  textView.setText("Loading..");
		      
		}
		@Override
		protected String doInBackground(String... arg0) 
		{
			String strSplit[]=arg0[0].split(":");
			String jsonSend="{\"SLOTID\":\""+strSplit[0]+"\",\"USER\":\""+strSplit[1]+"\"}";
			String res="Error";
			try 
			{
				URL url=new URL("http://192.168.1.2:8888/parking/FinalYearProject/public/Park");
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
			setContentView(R.layout.activity_slot);
			
			textView=(TextView)findViewById(R.id.textView2);
			textView.setVisibility(View.INVISIBLE);
			
			  textView = (TextView)findViewById(R.id.textView1);
			  textView.setText(result);
	    }
		
	}
}
