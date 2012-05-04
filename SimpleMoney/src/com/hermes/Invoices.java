package com.hermes;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//Similar to Bills.java
public class Invoices extends Activity{
	
	private int user_id; 
	private Transaction[] unpaidInvoices, paidInvoices;
	
	public void onCreate(Bundle savedInstanceState) {
		user_id = Global.user_id;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoices);
        //Forces screen to be portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        getUserData();      
        //Creates layout of transactions
        updateView(null);
    }
	
	//Gets user data and displays balance
	//Is "refreshed" everytime an action occurs
	public void getUserData()
	{
		try
		{
			URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users/" + user_id);
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(uri);
			
			get.setHeader("Accept", "application/json");
			get.setHeader("Content-type", "application/json");
			BasicResponseHandler responseHandler = new BasicResponseHandler();
			String responseString = client.execute(get, responseHandler);
			
			GsonBuilder g = new GsonBuilder();
			g.setDateFormat("E MMM d HH:mm:ss Z y");
			Gson gson = g.create();
			User um = gson.fromJson(responseString, User.class);
			TextView userData = (TextView) findViewById(R.id.balance);
	        userData.setText("Balance: " + um.getBalance());
		}
		//Displays error messages and go to Root view
		catch (Exception e) {
			Log.e("Bills", "Unable to retrieve User data");
			Toast.makeText(this, "Unable to Retrieve Data", Toast.LENGTH_LONG).show(); 
			Toast.makeText(this, "Please Try Again Later", Toast.LENGTH_LONG).show(); 
			Intent myIntent = new Intent(getApplicationContext(), Root.class);
    		startActivityForResult(myIntent, 0);
		}
	}
	
	//Retrieves user avatar image
	public Drawable getImage(String url)
	{
		try
		{
			if(url.equals("/images/medium/missing.png") || url.equals("/images/small/missing.png"))
				url = "http://severe-leaf-6733.herokuapp.com" + url;
		    return Drawable.createFromStream((InputStream)new URL(url).getContent(), "src");
		}
	    catch (Exception e) {
	    	Log.e("Invoices", "Null Image Error");
	    	return getResources().getDrawable( R.drawable.none );
	    }
	}
	
	//Formats layout for unpaid invoices data
	public void getUnpaidInvoices()
	{
		LinearLayout list = (LinearLayout)findViewById(R.id.unpaidInvoices);
		list.removeAllViews();
		list.setPadding(0, 5, 0, 5);
		
		//Title label
		TextView title = new TextView(this);
        title.setText("Unpaid Invoices");
        title.setTextSize(20);
        list.addView(title);
        
        //If the transactions is null an error labeled
        if(unpaidInvoices == null)
        {
        	TextView tv = new TextView(this);
        	tv.setText("Error");
        	list.addView(tv);
        }
        else
        {
        	//If their are no unpaid invoices
	        if(unpaidInvoices.length == 0)
	        {
	        	TextView none = new TextView(this);
	            none.setText("None");
	            list.addView(none);
	        }
	        else
	        {
	        	//Loops through all unpaid invoices
	        	for(int i = 0; i < unpaidInvoices.length; i++)
		        {
		        	Transaction trans = unpaidInvoices[i];
		        	
		        	//If a invoices isn't complete then its included
		        	if(trans.getComplete() == false)
		        	{
			        	TextView tv = new TextView(this);
			        	tv.setTextSize(16);
			        	tv.setId(trans.getID());
			        	//Gets avatar of recipient on bill
			        	try {
			    			tv.setCompoundDrawablesWithIntrinsicBounds(getImage(trans.getSender().getAvatarURL()), null, null, null);
			    		} catch (Exception e) {
			    			Log.e("Invoices", "Null Unpaid Photo");
			    		}
			        	
			        	//Adds bill details to textview
			        	if(trans.getRecipientID() != 0)
			        		tv.append(" " + trans.getSender().getName() + "\n");
			        	else
			        		tv.append(" " + trans.getSenderEmail() + "\n");
			        	tv.append(" " + trans.getUpdateDate() + "\n");
			    		tv.append(" " + "Amount: " + trans.getAmount());
			    		
			    		//Creates description textview
			        	TextView desc = new TextView(this);
			    		desc.setTextSize(12);
			    		desc.setTypeface(desc.getTypeface(), 2);
			    		if(!trans.getDescription().equals(""))
			    			desc.setText(" " + trans.getDescription());
			    		else
			    			desc.setText(" No description.");
			    		if(trans.getRecipientID() != 0)
			    		{
			    			if(!desc.getText().equals(""))
			    				desc.append("\n");
			    			desc.append(" " + trans.getSenderEmail());
			    		}
			        	
			    		//Creates layout parameters for tv
			        	RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams( 
			                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
			                    RelativeLayout.LayoutParams.WRAP_CONTENT);
			        	tv.setPadding(5, 0, 0, 0);
			        	tv.setLineSpacing((float)0, (float)1.25);
			        	tv.setLayoutParams(parameters);
			        	
			        	//Creates layout parameters for description
			        	RelativeLayout.LayoutParams parameters2 = new RelativeLayout.LayoutParams( 
				                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
				                    RelativeLayout.LayoutParams.WRAP_CONTENT);
			        	parameters2.addRule(RelativeLayout.BELOW, tv.getId());
			        	desc.setPadding(5, 5, 5, 5);
			        	desc.setLayoutParams(parameters2);
			        	
			        	//Creates layout parameters for bill area
			        	RelativeLayout area = new RelativeLayout(this);
			        	RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams( 
			                    RelativeLayout.LayoutParams.FILL_PARENT, 
			                    RelativeLayout.LayoutParams.WRAP_CONTENT);
			        	rlp.setMargins(0, 5, 0, 5);
			        	area.setLayoutParams(rlp);
			    
			        	//Components are added to widget and transaction widget is added to layout
			        	area.addView(tv);
			        	area.addView(desc);
			        	area.setBackgroundResource(R.layout.box);
			        	list.addView(area);
			        	
			        	//Since margins doesn't seem to be set for the area widget
			        	//an empty widget is created to gap bills in list
			        	TextView temp = new TextView(this);
			        	temp.setTextSize(0);
			        	list.addView(temp);
		        	}
		        }
	        }
        }
	}
	
	//Formats layout for paid invoices data
	public void getPaidInvoices()
	{
		LinearLayout list = (LinearLayout)findViewById(R.id.paidInvoices);
		list.removeAllViews();
		list.setPadding(0, 5, 0, 5);
		
		//Title label
		TextView title = new TextView(this);
        title.setText("Paid Invoices");
        title.setTextSize(20);
        list.addView(title);
        
        //If the transactions is null an error labeled
        if(paidInvoices == null)
        {
        	TextView tv = new TextView(this);
        	tv.setText("Error");
        	list.addView(tv);
        }
        else
        {
        	//If their are no paid invoices
        	if(paidInvoices.length == 0)
	        {
	        	TextView none = new TextView(this);
	            none.setText("None");
	            list.addView(none);
	        }
	        else
	        {
	        	//Loops through all paid bills
	        	for(int i = 0; i < paidInvoices.length; i++)
		        {
		        	Transaction trans = paidInvoices[i];
		        	
		        	//If a invoices is complete then its included
		        	if(trans.getComplete() == true)
		        	{
			        	TextView tv = new TextView(this);
			        	tv.setTextSize(16);
			        	tv.setId(trans.getID());
			        	//Gets avatar of recipient on bill
			        	try {
			    			tv.setCompoundDrawablesWithIntrinsicBounds(getImage(trans.getRecipient().getAvatarURL()), null, null, null);
			    		} catch (Exception e) {
			    			Log.e("Invoices", "Null Paid Photo");
			    		}
			        	
			        	//Adds bill details to textview
			        	if(trans.getRecipientID() != 0)
			        		tv.append(" " + trans.getSender().getName() + "\n");
			        	else
			        		tv.append(" " + trans.getSenderEmail() + "\n");
			        	tv.append(" " + trans.getUpdateDate() + "\n");
			    		tv.append(" " + "Amount: " + trans.getAmount());
			    		
			    		//Creates description textview
			    		TextView desc = new TextView(this);
			    		desc.setTextSize(12);
			    		desc.setTypeface(desc.getTypeface(), 2);
			    		if(!trans.getDescription().equals(""))
			    			desc.setText(" " + trans.getDescription());
			    		else
			    			desc.setText(" No description.");
			    		if(trans.getRecipientID() != 0)
			    		{
			    			if(!desc.getText().equals(""))
			    				desc.append("\n");
			    			desc.append(" " + trans.getSenderEmail());
			    		}
			    		
			    		//Creates layout parameters for tv
			        	RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams( 
			                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
			                    RelativeLayout.LayoutParams.WRAP_CONTENT);
			        	tv.setPadding(5, 0, 0, 0);
			        	tv.setLineSpacing((float)0, (float)1.25);
			        	tv.setLayoutParams(parameters);
			        	
			        	//Creates layout parameters for description
			        	RelativeLayout.LayoutParams parameters2 = new RelativeLayout.LayoutParams( 
				                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
				                    RelativeLayout.LayoutParams.WRAP_CONTENT);
				        parameters2.addRule(RelativeLayout.BELOW, tv.getId());
				        desc.setPadding(5, 5, 5, 5);
				        desc.setLayoutParams(parameters2);
			        	
				        //Creates layout parameters for area
			        	RelativeLayout area = new RelativeLayout(this);
			        	RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams( 
			                    RelativeLayout.LayoutParams.FILL_PARENT, 
			                    RelativeLayout.LayoutParams.WRAP_CONTENT);
			        	rlp.setMargins(0, 5, 0, 5);
			        	area.setLayoutParams(rlp);
			    
			        	//Components are added to widget and transaction widget is added to layout
			        	area.addView(tv);
			        	area.addView(desc);
			        	area.setBackgroundResource(R.layout.box);
			        	list.addView(area);
			        	
			        	//Since margins doesn't seem to be set for the area widget
			        	//an empty widget is created to gap bills in list
			        	TextView temp = new TextView(this);
			        	temp.setTextSize(0);
			        	list.addView(temp);
			        }
		        }
	        }
        }
	}
	
	public void updateView(View view)
	{
		try
    	{
			//Get paid invoices
    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users/" + user_id + "/invoices");
    		DefaultHttpClient client = Global.client;
    		HttpGet get = new HttpGet(uri);
    		
    		get.setHeader("Accept", "application/json");
    		get.setHeader("Content-type", "application/json");
    		BasicResponseHandler responseHandler = new BasicResponseHandler();
    		String responseString = client.execute(get, responseHandler);
    		
    		GsonBuilder g = new GsonBuilder();
    		g.setDateFormat("E MMM d HH:mm:ss Z y");
    		Gson gson = g.create();
    		paidInvoices = gson.fromJson(responseString, Transaction[].class);
    	}
		catch (Exception e)
		{
			paidInvoices = null;
			Log.e("Invoices", "Unable to get Paid");
		}
		
    	try{
    		//Get unpaid invoices
    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users/" + user_id + "/unpaidInvoices");
    		DefaultHttpClient client = Global.client;
    		HttpGet get = new HttpGet(uri);
    		
    		get.setHeader("Accept", "application/json");
    		get.setHeader("Content-type", "application/json");
    		BasicResponseHandler responseHandler = new BasicResponseHandler();
    		String responseString = client.execute(get, responseHandler);
    		
    		GsonBuilder g = new GsonBuilder();
    		g.setDateFormat("E MMM d HH:mm:ss Z y");
    		Gson gson = g.create();
    		unpaidInvoices = gson.fromJson(responseString, Transaction[].class);
		}
		catch (Exception e)
		{
			unpaidInvoices = null;
			Log.e("Invoices", "Unable to get Unpaid");
		}
    	//Update layouts are retrieving data
    	getUserData();
		getUnpaidInvoices();
        getPaidInvoices();
	}
	
	public void pay(View view)
	{
		//Gets index from tag and pays for that bill
		String index = view.getTag().toString();
		Integer i = new Integer(index);
		Transaction trans = unpaidInvoices[i.intValue()];
		try
    	{
    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/transactions/" + trans.getID());
    		HttpClient client = new DefaultHttpClient();
    		HttpPut put = new HttpPut(uri);
    		
    		JSONObject json = new JSONObject();
    		JSONObject m = new JSONObject();
    		m.put("recipient_email", trans.getRecipientEmail());
    		m.put("sender_email", trans.getSenderEmail());
    		m.put("amount", trans.getAmount());
    		m.put("complete", true);
    		json.put("transaction", m);
    		
    		StringEntity se = new StringEntity(json.toString());
    		put.setEntity(se);
    		put.setHeader("Accept", "application/json");
    		put.setHeader("Content-type", "application/json");
    		BasicResponseHandler responseHandler = new BasicResponseHandler();
    		client.execute(put, responseHandler);
    		
    		//Updates layouts
    		getUserData();
    		updateView(null);
    	}
    	catch (Exception e){
    		Log.e("Invoices", "Pay Error");
    		Toast.makeText(this, "Unable to Pay Invoice", Toast.LENGTH_LONG).show();  
    		Toast.makeText(this, "Please Try Again Later", Toast.LENGTH_LONG).show(); 
    	}
	}
	
	public void goToRootView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), Root.class);
	    startActivity(myIntent);
	}
}
