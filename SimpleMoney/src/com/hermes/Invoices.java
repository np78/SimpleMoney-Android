package com.hermes;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
		user_id = getIntent().getExtras().getInt("User_ID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoices);
		updateView(null);
    }
	
	private Drawable grabImageFromUrl(String url) throws Exception {
		if(url.equals(""))
			url = "http://severe-leaf-6733.herokuapp.com/images/small/missing.png";
	    return Drawable.createFromStream((InputStream)new URL("http://severe-leaf-6733.herokuapp.com" + url).getContent(), "src");
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
	    			//tv.setCompoundDrawables(grabImageFromUrl(trans.getSender().getAvatarURLSmall()), null, null, null);
	    		} catch (Exception e) {}
	        	tv.append(" " + trans.getSenderEmail() + "   (" + trans.getSenderEmail() + ")\n");
	        	String date = trans.getCreateDate();
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
	    		
	        	RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams( 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT);
	        	tv.setPadding(5, 5, 5, 5);
	        	tv.setLineSpacing((float)0, (float)1.5);
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
	        	
	        	rlp = new RelativeLayout.LayoutParams( 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT); 
	        	rlp.addRule(RelativeLayout.RIGHT_OF, tv.getId());
	        	rlp.addRule(RelativeLayout.ALIGN_RIGHT, tv.getId());
	    
	        	area.addView(tv);
	        	//area.addView(pay, rlp);
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
        if(paidInvoices.length == 0)
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
	        	
	        	TextView tv = new TextView(this);
	        	tv.setId(trans.getID());
	        	try {
	    			//tv.setCompoundDrawables(grabImageFromUrl(trans.getSender().getAvatarURLSmall()), null, null, null);
	    		} catch (Exception e) {}
	        	tv.append(" " + trans.getSenderEmail() + "   (" + trans.getSenderEmail() + ")\n");
	        	String date = trans.getCreateDate();
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
	    		
	        	RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams( 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT);
	        	tv.setPadding(5, 5, 5, 5);
	        	tv.setLineSpacing((float)0, (float)1.5);
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
	        	
	        	rlp = new RelativeLayout.LayoutParams( 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT); 
	        	rlp.addRule(RelativeLayout.RIGHT_OF, tv.getId());
	        	rlp.addRule(RelativeLayout.ALIGN_RIGHT, tv.getId());
	    
	        	area.addView(tv);
	        	//area.addView(pay, rlp);
	        	area.addView(desc);
	        	area.setBackgroundResource(R.layout.box);
	        	list.addView(area);
	        }
        }
	}
	
	public void updateView(View view)
	{
		try
    	{
			//Get paid invoices
    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users/" + user_id + "/invoices");
    		HttpClient client = new DefaultHttpClient();
    		HttpGet get = new HttpGet(uri);
    		get.setHeader("Accept", "application/json");
    		get.setHeader("Content-type", "application/json");
    		BasicResponseHandler responseHandler = new BasicResponseHandler();
    		String responseString = client.execute(get, responseHandler);
    		
    		GsonBuilder g = new GsonBuilder();
    		g.setDateFormat("E MMM d HH:mm:ss Z y");
    		Gson gson = g.create();
    		paidInvoices = gson.fromJson(responseString, Transaction[].class);
    		
    		//Get unpaid invoices
    		uri = new URI("http://severe-leaf-6733.herokuapp.com/users/" + user_id + "/unpaidinvoices");
    		client = new DefaultHttpClient();
    		get = new HttpGet(uri);
    		get.setHeader("Accept", "application/json");
    		get.setHeader("Content-type", "application/json");
    		responseHandler = new BasicResponseHandler();
    		responseString = client.execute(get, responseHandler);
    		
    		g = new GsonBuilder();
    		g.setDateFormat("E MMM d HH:mm:ss Z y");
    		gson = g.create();
    		unpaidInvoices = gson.fromJson(responseString, Transaction[].class);
    		
    		getUnpaidInvoices();
            getPaidInvoices();
    	}
    	catch (Exception e){
    		Log.e("eX",e.getMessage());
    	}
	}
	
	public void pay(View view)
	{
		/*String ID = view.getTag().toString();
		try
    	{
    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/invoices");
    		HttpClient client = new DefaultHttpClient();
    		HttpPost post = new HttpPost(uri);
    		JSONObject json = new JSONObject();
    		json.put("id", ID);
    		
    		StringEntity se = new StringEntity("JSON: " + json.toString());
    		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
    		post.setEntity(se);
    		HttpResponse re = client.execute(post);
    		
    		if(re.getStatusLine().getStatusCode() == 200)
    		{
    			Toast.makeText(this, "Payment Complete", Toast.LENGTH_LONG).show();
    			getUnpaidInvoices();
    	        getPaidInvoices();
            	
            } else {
            	Toast.makeText(this, "Payment Fail", Toast.LENGTH_LONG).show();               
            }
    	}
    	catch (Exception e){
    		Log.e("eX",e.getMessage());
    	}*/
	}
	
	public void goToRootView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), Root.class);
		myIntent.putExtra("User_ID", user_id);
	    startActivity(myIntent);
	}
}
