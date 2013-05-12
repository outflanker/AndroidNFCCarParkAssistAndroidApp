package com.example.amber.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.amber.R;
import com.example.amber.unregister.UnregisteredActivity;

public class SlotOperationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slot_operation);
		
		Intent i=getIntent();
		final String slot=i.getStringExtra("SLOTID").split(":")[0];
		TextView tv=(TextView)findViewById(R.id.textView1);
		tv.setText(slot);
		
		Button b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View arg0)
			{
				Intent intent=new Intent(SlotOperationActivity.this,UnregisteredActivity.class);
	        	intent.putExtra("SLOTID", slot);
	        	startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slot_operation, menu);
		return true;
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
        if(event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if(keyCode== KeyEvent.KEYCODE_BACK)
            {
            	Intent intent = new Intent(SlotOperationActivity.this, ViewReservedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
