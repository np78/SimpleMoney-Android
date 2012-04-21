package com.hermes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class QuickPay extends Activity{
	
	private int user_id;
	
	public void onCreate(Bundle savedInstanceState) {
		user_id = Global.user_id;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_pay);
        //Intent intent = new Intent("com.google.zxing.client.android.SCAN"); 
        //intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); 
        //startActivityForResult(intent, 0); 
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
	            // Handle successful scan 
	        } else if (resultCode == RESULT_CANCELED) { 
	            // Handle cancel 
	        } 
	    } 
	} 
}
