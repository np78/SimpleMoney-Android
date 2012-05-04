//Not used


/*package com.hermes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class QuickPay extends Activity{
	
	private int user_id;
	
	public void onCreate(Bundle savedInstanceState) {
		user_id = Global.user_id;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_pay);
        Intent intent = new Intent("com.google.zxing.client.android.SCAN"); 
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); 
        startActivityForResult(intent, 0); 
    }
	
	public void goToRootView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), Root.class);
	    startActivity(myIntent);
	} 
	 
	public void onActivityResult(int requestCode, int resultCode, Intent intent) { 
	    if (requestCode == 0) { 
	        if (resultCode == RESULT_OK) { 
	            String contents = intent.getStringExtra("SCAN_RESULT"); 
	            String format = intent.getStringExtra("SCAN_RESULT_FORMAT"); 
	            
	            Log.v("Contents", contents);
	            Log.v("Format", format);
	            // Handle successful scan 
	            
	            GsonBuilder g = new GsonBuilder();
	    		Gson gson = g.create();
	    		QR qr = gson.fromJson(contents, QR.class);
	    		
	    		Toast.makeText(this, qr.getName(), Toast.LENGTH_LONG).show();
	    		Toast.makeText(this, qr.getEmail(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) { 
	            // Handle cancel 
	        } 
	    } 
	} 
}*/
