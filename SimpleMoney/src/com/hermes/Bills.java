package com.hermes;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Bills extends Activity{
	
	private int user_id;
	private User user;
	private Transaction[] unpaidBills, paidBills;
	//private LinkedList<TextView> entries = new LinkedList<TextView>();
	
	public void onCreate(Bundle savedInstanceState) {
		user_id = Global.user_id;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bills);
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
		catch (Exception e) {}
		return null;
	}
	
	private Drawable grabImageFromUrl(String url) throws Exception {
	    return Drawable.createFromStream((InputStream)new URL("http://severe-leaf-6733.herokuapp.com" + url).getContent(), "src");
	}
	
	public void getUnpaidBills()
	{
		LinearLayout list = (LinearLayout)findViewById(R.id.unpaidBills);
		list.removeAllViews();
		list.setPadding(0, 5, 0, 5);
		TextView title = new TextView(this);
        title.setText("Unpaid Bills");
        title.setTextSize(20);
        list.addView(title);
        if(unpaidBills.length == 0)
        {
        	TextView none = new TextView(this);
            none.setText("None");
            list.addView(none);
        }
        else
        {
        	for(int i = 0; i < unpaidBills.length; i++)
	        {
	        	Transaction trans = unpaidBills[i];
	        	
	        	TextView tv = new TextView(this);
	        	tv.setId(trans.getID());
	        	try {
	    			tv.setCompoundDrawables(grabImageFromUrl(trans.getSender().getAvatarURLSmall()), null, null, null);
	    		} catch (Exception e) {}
	        	//tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.qr_marker, 0, 0, 0);
	        	tv.append(" " + trans.getSenderEmail() + "   (" + trans.getSenderEmail() + ")\n");
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
	        	tv.setLineSpacing((float)0, (float)1.5);
	        	tv.setLayoutParams(parameters);
	        	
	        	rlp = new RelativeLayout.LayoutParams( 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT, 
	                    RelativeLayout.LayoutParams.WRAP_CONTENT); 
	        	rlp.addRule(RelativeLayout.RIGHT_OF, tv.getId());
	        	rlp.addRule(RelativeLayout.ALIGN_RIGHT, tv.getId());
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
	
	public void getPaidBills()
	{
		LinearLayout list = (LinearLayout)findViewById(R.id.paidBills);
		list.removeAllViews();
		list.setPadding(0, 5, 0, 5);
		TextView title = new TextView(this);
        title.setText("Paid Bills");
        title.setTextSize(20);
        list.addView(title);
        boolean any = false;
        for(int i = 0; i < paidBills.length; i++)
        {
        	if(paidBills[i].getComplete())
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
        	for(int i = 0; i < paidBills.length; i++)
	        {
	        	Transaction trans = paidBills[i];
	        	
	        	if(trans.getComplete())
	        	{
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
	}
	
	public void updateView(View view)
	{
		try
    	{
			//Get paid bills
    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users/" + user_id + "/bills");
    		DefaultHttpClient client = Global.client;
    		HttpGet get = new HttpGet(uri);
    		get.setHeader("Accept", "application/json");
    		get.setHeader("Content-type", "application/json");
    		BasicResponseHandler responseHandler = new BasicResponseHandler();
    		String responseString = client.execute(get, responseHandler);
    		
    		GsonBuilder g = new GsonBuilder();
    		g.setDateFormat("E MMM d HH:mm:ss Z y");
    		Gson gson = g.create();
    		paidBills = gson.fromJson(responseString, Transaction[].class);
    		
    		//Get unpaidBills
    		uri = new URI("http://severe-leaf-6733.herokuapp.com/users/" + user_id + "/unpaidbills");
    		get = new HttpGet(uri);
    		get.setHeader("Accept", "application/json");
    		get.setHeader("Content-type", "application/json");
    		responseHandler = new BasicResponseHandler();
    		responseString = client.execute(get, responseHandler);
    		
    		g = new GsonBuilder();
    		g.setDateFormat("E MMM d HH:mm:ss Z y");
    		gson = g.create();
    		unpaidBills = gson.fromJson(responseString, Transaction[].class);
    	}
    	catch (Exception e){
    		Log.e("eX",e.getMessage());
    	}
		getUnpaidBills();
        getPaidBills();
	}
	
	public void pay(View view)
	{
		String index = view.getTag().toString();
		Transaction trans = unpaidBills[index.charAt(0) - 48];
		try
    	{
    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/invoices");
    		HttpClient client = new DefaultHttpClient();
    		HttpPost post = new HttpPost(uri);
    		
    		JSONObject json = new JSONObject();
    		JSONObject m = new JSONObject();
    		m.put("recipient_email", trans.getSenderEmail());
    		m.put("sender_email", trans.getRecipientEmail());
    		m.put("amount", trans.getAmount());
    		json.put("user", m);
    		
    		StringEntity se = new StringEntity(json.toString());
    		post.setEntity(se);
    		post.setHeader("Accept", "application/json");
    		post.setHeader("Content-type", "application/json");
    		BasicResponseHandler responseHandler = new BasicResponseHandler();
    		String responseString = client.execute(post, responseHandler);
    		updateView(null);
    	}
    	catch (Exception e){
    		Log.e("eX",e.getMessage());
    	}
	}

	public void goToRootView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), Root.class);
	    startActivity(myIntent);
	}
}
