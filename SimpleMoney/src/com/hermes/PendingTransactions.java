package com.hermes;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

//Creates tab view and set Bills.java activity as selected "window"
public class PendingTransactions extends TabActivity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_transactions);
        //Forces screen to be portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            
        TabHost tabHost = getTabHost(); 
        TabHost.TabSpec spec;
        
        //Creates bills tab
        Intent intent = new Intent().setClass(this, Bills.class);  
        spec = tabHost.newTabSpec("bills");
        spec.setIndicator("Bills");
        spec.setContent(intent);
        tabHost.addTab(spec);    
        
        //Creates invoices tab
        intent = new Intent().setClass(this, Invoices.class); 
        spec = tabHost.newTabSpec("invoices");
        spec.setIndicator("Invoices");
        spec.setContent(intent);
        tabHost.addTab(spec); 
    }
	
	public void goToRootView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), Root.class);
	    startActivity(myIntent);
	}
}