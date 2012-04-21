package com.hermes;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;

public class PendingTransactions extends TabActivity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_transactions);
            
        TabHost tabHost = getTabHost(); 
        TabHost.TabSpec spec;
        
        Intent intent = new Intent().setClass(this, Bills.class);  
        spec = tabHost.newTabSpec("bills");
        spec.setIndicator("Bills");
        spec.setContent(intent);
        tabHost.addTab(spec);    
        
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