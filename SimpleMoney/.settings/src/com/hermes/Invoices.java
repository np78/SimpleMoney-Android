package com.hermes;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Invoices extends Activity{
	
	private int user_id;
	private User user;
	private Transaction[] unpaidInvoices, paidInvoices;
	//private LinkedList<TextView> entries = new LinkedList<TextView>();
	
	public void onCreate(Bundle savedInstanceState) {
		user_id = Global.user_id;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoices);
        user = getUserData();
		updateView(null);
    }
	
	public User getUserData()
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
			return um;
		}
		catch (Exception e) {
			Log.e("Unable to retrieve User data",e.getMessage());
		}
		return null;
	}
	
	public static Drawable getImage(String url)
	{
		try
		{
			if(url.equals("/images/medium/missing.png") || url.equals("/images/small/missing.png"))
				url = "http://severe-leaf-6733.herokuapp.com" + url;
		    return Drawable.createFromStream((InputStream)new URL(url).getContent(), "src");
		}
	    catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("Get Image Error", "Null Error");
		return null;
	}
	
	public void getUnpaidInvoices()
	{
		LinearLayout list = (LinearLayout)findViewById(R.id.unpaidInvoices);
		list.removeAllViews();
		list.setPadding(0, 5, 0, 5);
		TextView title = new TextView(this);
        title.setText("Unpaid Invoices");
        title.setTextSize(20);
        list.addView(title);
        if(unpaidInvoices.length == 0)
        {
        	TextView none = new TextView(this);
            none.setText("None");
            list.addView(none);
        }
        else
        {
        	for(int i = 0; i < unpaidInvoices.length; i++)
	        {
	        	Transaction trans = unpaidInvoices[i];
	        	
	        	TextView tv = new TextView(this);
	        	tv.setId(trans.getID());
	        	try {
	    			tv.setCompoundDrawablesWithIntrinsicBounds(getImage(trans.getSender().getAvatarURLSmall()), null, null, null);
	    		} catch (Exception e) {
	    			Log.e("Unpaid Invoice Unable to retrieve photo", e.getMessage());
	    		}
	        	tv.append(" " + trans.getSender().getName() + "  (" + trans.getSenderEmail() + ")\n");
	        	String date = trans.getCreateDate();
	        	/*int hours = date.getHours();
	        	String timezone = "AM";
	        	int min = date.getMinutes();
	        	int sec = date.getSeconds();
	        	String minutes = "" + min;
	        	String seconds = "" + sec;
	        	if(hours > 12)
	        	{
	        		hours -= 12;
	        		timezone = "PM";
	        	}
	        	if(min < 10)
	        		minutes = "0" + minutes;
	        	if(sec < 10)
	        		seconds = "0" + seconds;
	    		tv.append(" " + date.getMonth() + " " + date.getDay() + ", " + date.getYear() + " " +
	    				 hours + ":" + minutes + ":" + seconds + timezone + "\n");*/
	        	tv.append(" " + trans.getCreateDate() + "\n");
	    		tv.append(" " + "Amount: " + trans.getAmount());
	    		
	    		TextView desc = new TextView(this);
	    		desc.setText(" " + trans.getDescription());
	    		
	    		Button pay = new Button(this);
	    		pay.setText("Pay");
	    		pay.setTag(i);
	    		pay.setOnClickListener(new View.OnClickListener() {             
	    			public void onClick(View v) {                 
	    				pay(v);            
	    			}         
	    		});
	        	
	        	RelativeLayout area = new RelativeLayout(this);
	        	RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams( 
	                    RelativeLayout.LayoutParams.FILL_PARENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT);
	        	rlp.setMargins(0, 5, 0, 5);
	        	area.setLayoutParams(rlp);
	        	
	        	RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams( 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT);
	        	tv.setPadding(5, 5, 5, 5);
	        	tv.setLineSpacing((float)0, (float)1.25);
	        	tv.setLayoutParams(parameters);
	        	
	        	rlp = new RelativeLayout.LayoutParams( 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT); 
	        	rlp.addRule(RelativeLayout.RIGHT_OF, tv.getId());
	        	rlp.addRule(RelativeLayout.CENTER_VERTICAL, tv.getId());
	        	pay.setLayoutParams(rlp);
	        	
	        	RelativeLayout.LayoutParams parameters2 = new RelativeLayout.LayoutParams( 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT);
	        	parameters2.addRule(RelativeLayout.BELOW, tv.getId());
	        	desc.setPadding(5, 5, 5, 5);
	        	desc.setLayoutParams(parameters2);
	    
	        	area.addView(tv);
	        	area.addView(pay);
	        	area.addView(desc);
	        	area.setBackgroundResource(R.layout.box);
	        	list.addView(area);
	        }
        }
	}
	
	public void getPaidInvoices()
	{
		LinearLayout list = (LinearLayout)findViewById(R.id.paidInvoices);
		list.removeAllViews();
		list.setPadding(0, 5, 0, 5);
		TextView title = new TextView(this);
        title.setText("Paid Invoices");
        title.setTextSize(20);
        list.addView(title);
        boolean any = false;
        for(int i = 0; i < paidInvoices.length; i++)
        {
        	if(paidInvoices[i].getComplete())
        		any = true;
        }
        if(!any)
        {
        	TextView none = new TextView(this);
            none.setText("None");
            list.addView(none);
        }
        else
        {
        	for(int i = 0; i < paidInvoices.length; i++)
	        {
	        	Transaction trans = paidInvoices[i];
	        	
	        	if(trans.getComplete())
	        	{
		        	TextView tv = new TextView(this);
		        	tv.setId(trans.getID());
		        	try {
		    			tv.setCompoundDrawablesWithIntrinsicBounds(getImage(trans.getRecipient().getAvatarURLSmall()), null, null, null);
		    		} catch (Exception e) {
		    			Log.e("Paid Invoice Unable to retrieve photo", e.getMessage());
		    		}
		        	tv.append(" " + trans.getRecipient().getName() + "  (" + trans.getRecipientEmail() + ")\n");
		        	String date = trans.getCreateDate();
		        	tv.append(" " + trans.getCreateDate() + "\n");
		    		tv.append(" " + "Amount: " + trans.getAmount());
		    		
		    		TextView desc = new TextView(this);
		    		desc.setText(" " + trans.getDescription());
		    		
		        	RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams( 
		                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
		                    RelativeLayout.LayoutParams.WRAP_CONTENT);
		        	tv.setPadding(5, 5, 5, 5);
		        	tv.setLineSpacing((float)0, (float)1.25);
		        	tv.setLayoutParams(parameters);
		        	
		        	RelativeLayout.LayoutParams parameters2 = new RelativeLayout.LayoutParams( 
		                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
		                    RelativeLayout.LayoutParams.WRAP_CONTENT);
		        	parameters2.addRule(RelativeLayout.BELOW, tv.getId());
		        	desc.setPadding(5, 5, 5, 5);
		        	desc.setLayoutParams(parameters2);
		        	
		        	RelativeLayout area = new RelativeLayout(this);
		        	RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams( 
		                    RelativeLayout.LayoutParams.FILL_PARENT, 
		                    RelativeLayout.LayoutParams.WRAP_CONTENT);
		        	rlp.setMargins(0, 5, 0, 5);
		        	area.setLayoutParams(rlp);
		    
		        	area.addView(tv);
		        	area.addView(desc);
		        	area.setBackgroundResource(R.layout.box);
		        	list.addView(area);
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
    		Log.e("Update1", responseString);
    		
    		GsonBuilder g = new GsonBuilder();
    		g.setDateFormat("E MMM d HH:mm:ss Z y");
    		Gson gson = g.create();
    		paidInvoices = gson.fromJson(responseString, Transaction[].class);
    		
    		//Get unpaid invoices
    		uri = new URI("http://severe-leaf-6733.herokuapp.com/users/" + user_id + "/unpaidInvoices");
    		get = new HttpGet(uri);
    		get.setHeader("Accept", "application/json");
    		get.setHeader("Content-type", "application/json");
    		responseHandler = new BasicResponseHandler();
    		responseString = client.execute(get, responseHandler);
    		Log.e("Update2", responseString);
    		
    		g = new GsonBuilder();
    		g.setDateFormat("E MMM d HH:mm:ss Z y");
    		gson = g.create();
    		unpaidInvoices = gson.fromJson(responseString, Transaction[].class);
    		getUnpaidInvoices();
            getPaidInvoices();
    	}
    	catch (Exception e){
    		Log.e("Unable to retrieve invoice data",e.getMessage());
    	}
	}
	
	public void pay(View view)
	{
		String index = view.getTag().toString();
		Integer i = new Integer(index);
		Transaction trans = unpaidInvoices[i.intValue()];
		try
    	{
    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/invoices/" + trans.getID());
    		HttpClient client = new DefaultHttpClient();
    		HttpPost post = new HttpPost(uri);
    		
    		JSONObject json = new JSONObject();
    		JSONObject m = new JSONObject();
    		m.put("recipient_email", trans.getSenderEmail());
    		m.put("sender_email", trans.getRecipientEmail());
    		m.put("amount", trans.getAmount());
    		m.put("complete", true);
    		json.put("transaction", m);
    		
    		StringEntity se = new StringEntity(json.toString());
    		post.setEntity(se);
    		post.setHeader("Accept", "application/json");
    		post.setHeader("Content-type", "application/json");
    		BasicResponseHandler responseHandler = new BasicResponseHandler();
    		client.execute(post, responseHandler);
    		updateView(null);
    	}
    	catch (Exception e){
    		Log.e("Bill Pay Error",e.getMessage());
    	}
	}
	
	public void goToRootView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), Root.class);
	    startActivity(myIntent);
	}
}
