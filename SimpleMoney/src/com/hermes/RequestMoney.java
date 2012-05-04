package com.hermes;

import java.net.URI;

import org.apache.http.HttpResponse;
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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//Similar to SendMoney.java
public class RequestMoney extends Activity{
	
	private int width, height, user_id;	
	private EditText em;
	private User user;
	
	public void onCreate(Bundle savedInstanceState) {
		user_id = Global.user_id;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_money);
        //Forces screen to be portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        user = getUserData();
        
        //Gets width and height of phone
        WindowManager mWinMgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE); 
        width = mWinMgr.getDefaultDisplay().getWidth(); 
        height = mWinMgr.getDefaultDisplay().getHeight();
        
        //Sets height and width of amount field
        EditText am = (EditText) findViewById(R.id.amount);
        am.setWidth((int)(width/1.1));
    	am.setHeight((int)(height/50));
    	
    	//Sets height and width of email field
    	em = (EditText) findViewById(R.id.email);
    	em.setWidth((int)(width/1.3));
    	em.setHeight((int)(height/50));
    	
    	//Sets height and width of description field
    	//Doesn't seem to set height right as it's supposed to be bigger
    	EditText des = (EditText) findViewById(R.id.description);
        des.setWidth((int)(width/1.1));
    	des.setHeight((int)(height/25));
    	
    	//Changes the "Request Money" button to have a black background color
    	Button button = (Button) findViewById(R.id.ok);
        button.setWidth((int)(width/1.1));
        Drawable d = button.getBackground(); 
        PorterDuffColorFilter filter = new PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
        
        //Changes the "Search" button to have a black background color
        ImageButton search = (ImageButton) findViewById(R.id.search);
        d = search.getBackground(); 
        filter = new PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
    }
	
	//Goes into get contacts mode when search button is pressed
	public void getContacts(View view)
	{
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 
		startActivityForResult(intent, 1); 
	}
	
	//Handles contact selection mode
	//Straight from Internet
	//Only grabs first email of contact
	public void onActivityResult(int reqCode, int resultCode, Intent data) { 
	    super.onActivityResult(reqCode, resultCode, data); 
	 
	    try { 
	        if (resultCode == Activity.RESULT_OK) { 
	            Uri contactData = data.getData(); 
	            Cursor cur = managedQuery(contactData, null, null, null, null); 
	            ContentResolver contect_resolver = getContentResolver(); 
	            String emailAddress = "";
	            
	            //Gets contact data
	            if (cur.moveToFirst()) {   
		            String id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));             
	                Cursor emails = contect_resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
	                		ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null); 
	                
	                //Gets first email
	                if(emails.moveToFirst())
	                {
	                	emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	                }
	                emails = null;
	            }
	            //Sets email to field
                em.setText(emailAddress); 
 
                //"Closes" all cursors to avoid memory leaks
                emailAddress = null;
	            contect_resolver = null; 
	            cur = null; 
	        } 
	    } catch (IllegalArgumentException e) { 
	        e.printStackTrace(); 
	        Log.e("IllegalArgumentException :: ", e.toString()); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	        Log.e("Error :: ", e.toString()); 
	    } 
	} 
	
	public void goToRootView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), Root.class);
	    startActivity(myIntent);
	}
	
	//Sets all field to have blank texts
	public void clearFields(View view)
	{
		EditText am = (EditText) findViewById(R.id.amount);
        am.setText("");
    	
    	EditText em = (EditText) findViewById(R.id.email);
    	em.setText("");
    	
    	EditText des = (EditText) findViewById(R.id.description);
    	des.setText("");
	}
	
	//Gets user data from server returns a user object to caller
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
			TextView userData = (TextView) findViewById(R.id.title);
	        userData.setText("Balance: " + um.getBalance());
			return um;
		}
		//Displays error messages and go to Root view
		catch (Exception e) {
			Toast.makeText(this, "Unable to retrieve data", Toast.LENGTH_LONG).show();
    		Toast.makeText(this, "Please Try Again Later", Toast.LENGTH_LONG).show();  
    		Intent myIntent = new Intent(getApplicationContext(), Root.class);
    		startActivityForResult(myIntent, 0);
			Log.e("Request Money", "Unable to retrieve User data");
		}
		return null;
	}
	
	public void requestMoney(View view)
	{
		//Extracts user fields from widgets
		EditText amo = (EditText) findViewById(R.id.amount);
    	EditText ema = (EditText) findViewById(R.id.email);
    	EditText des = (EditText) findViewById(R.id.description);
    	String amount = amo.getText().toString();
    	//Checks for "$" in amount and omits it
    	if(amount.contains("$"))
    		amount.replace("$", "");
    	String email = ema.getText().toString();
    	String description = des.getText().toString();
    	
	    String[] tokens = email.split("@");	//Splits email address into two tokens at the "@"
	    boolean okay = true;
	    if(email.equals(""))
	    {
    		Toast.makeText(this, "E-mail Required", Toast.LENGTH_SHORT).show();
    		okay = false;
    	}
	    else
	    {
	    	//Checks if email is valid
	    	//Not perfect as it only checks if a "." is in the second token
	    	//and if the two sides of the "@" symbol exist
	    	if(!(tokens.length == 2 && 
	    		    (tokens[0] != null &&(tokens[0].trim().length() > 0)) && 
	    		    (tokens[1] != null && (tokens[1].trim().length() > 0)) && 
	    		    tokens[1].contains(".")))
	    	{
	    		Toast.makeText(this, "Invalid E-mail", Toast.LENGTH_SHORT).show();
	    		okay = false;
	    	}
	    }
    	if(amount.equals(""))
    	{
    		Toast.makeText(this, "Amount Required", Toast.LENGTH_SHORT).show();
    		okay = false;
    	}
    	else
    	{
    		//Only one decimal in string
    		if(amount.indexOf('.') == amount.lastIndexOf('.'))
    		{
    			Toast.makeText(this, "Invalid Amount", Toast.LENGTH_SHORT).show();   
				okay = false;
    		}
    		else
    		{
    			//Checks if amount only contains numbers or a decimal
	    		for(int i = 0; i < amount.length(); i++)
	    		{
	    			int character = amount.charAt(i);
	    			if(!(character >= 48 && character <= 57 || character == 46))
	    			{
	    				Toast.makeText(this, "Invalid Amount", Toast.LENGTH_SHORT).show();   
	    				okay = false;
	    			}
	    		}
    		}
    	}
    	if(okay)
    	{
	    	try
	    	{
	    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/invoices");
	    		DefaultHttpClient client = Global.client;
	    		HttpPost post = new HttpPost(uri);
	    		
	    		JSONObject json = new JSONObject();
	    		JSONObject m = new JSONObject();
	    		m.put("sender_email", email);
	    		m.put("recipient_email", user.getEmail());
	    		m.put("description", description);
	    		m.put("complete", false);
	    		Double d = new Double(amount);
	    		if(!d.isNaN())
	    			m.put("amount", "" + d.doubleValue());
	    		//m.put("amount", amount);
	    		json.put("transaction", m);
	    		
	    		StringEntity se = new StringEntity(json.toString());
	    		post.setEntity(se);
	    		post.setHeader("Accept", "application/json");
	    		post.setHeader("Content-type", "application/json");
	    		//BasicResponseHandler responseHandler = new BasicResponseHandler();
	    		//client.execute(post, responseHandler, Global.localContext);
	    		HttpResponse response = client.execute(post, Global.localContext);
	    		
	    		if(response.getStatusLine().getStatusCode() == 201)
	    		{
	    			Toast.makeText(this, "Invoice Approved", Toast.LENGTH_SHORT).show();
	            } else {
	            	Log.e("Request Money Response Code", "" + response.getStatusLine().getStatusCode());
	            	Toast.makeText(this, "Request Denied", Toast.LENGTH_SHORT).show();               
	            }
	    	}
	    	catch (Exception e){
	    		Toast.makeText(this, "Request Money Failed", Toast.LENGTH_LONG).show();
	    		Toast.makeText(this, "Please Try Again Later", Toast.LENGTH_LONG).show();  
	    		Log.e("Request Money", "Can't request error");
	    	}
    	}
	}
}
