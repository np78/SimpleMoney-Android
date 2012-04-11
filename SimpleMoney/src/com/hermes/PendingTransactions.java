package com.hermes;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;

public class PendingTransactions extends TabActivity{

	private int user_id;
	
	public void onCreate(Bundle savedInstanceState) {
		user_id = getIntent().getExtras().getInt("User_ID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_transactions);
            
        TabHost tabHost = getTabHost(); 
        TabHost.TabSpec spec;
        
        Intent intent = new Intent().setClass(this, Bills.class);  
        intent.putExtra("User_ID", user_id);
        spec = tabHost.newTabSpec("bills");
        spec.setIndicator("Bills");
        spec.setContent(intent);
        tabHost.addTab(spec);    
        
        intent = new Intent().setClass(this, Invoices.class); 
        intent.putExtra("User_ID", user_id);
        spec = tabHost.newTabSpec("invoices");
        spec.setIndicator("Invoices");
        spec.setContent(intent);
        tabHost.addTab(spec); 
    }
	
	public void goToRootView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), Root.class);
		myIntent.putExtra("User_ID", user_id);
	    startActivity(myIntent);
	}
}