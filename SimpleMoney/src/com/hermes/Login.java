package com.hermes;

import java.net.URI;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity{
	private int width, height;	
	public User user;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
       
        //Gets height and width of android device
        WindowManager mWinMgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE); 
        width = mWinMgr.getDefaultDisplay().getWidth(); 
        height = mWinMgr.getDefaultDisplay().getHeight();
    	
        //Sets height and width of email field
    	EditText un = (EditText) findViewById(R.id.username);
        un.setWidth((int)(width/1.1));
    	un.setHeight((int)(height/50));
    	
    	//Sets height and width of password field
    	EditText pw = (EditText) findViewById(R.id.password);
    	pw.setWidth((int)(width/1.1));
    	pw.setHeight((int)(height/50));
    	
    	//Changes the "Sign In" button to have a black background color
    	Button button1 = (Button) findViewById(R.id.ok);
    	Drawable d = button1.getBackground(); 
    	PorterDuffColorFilter filter = new PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
        
        //Changes the "Cancel" button to have a black background color
        Button button2 = (Button) findViewById(R.id.back);
    	d = button2.getBackground(); 
        filter = new PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
    }
    
    public void goToRootView(View view)
    {
    	//Extracts user fields from widgets
    	EditText un = (EditText) findViewById(R.id.username);
    	EditText pw = (EditText) findViewById(R.id.password);
    	String username = un.getText().toString();
    	String password = pw.getText().toString();
    	
    	String[] tokens = username.split("@");	//Splits email address into two tokens at the "@"
    	boolean okay = true;
    	if(username.equals(""))
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
    	if(password.equals(""))
    	{
    		Toast.makeText(this, "Password Required", Toast.LENGTH_SHORT).show(); 
    		okay = false;
    	}
    	if(okay)
    	{
	    	try
	    	{
	    		//Creates server communication objects
	    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users/sign_in");
	    		DefaultHttpClient client = new DefaultHttpClient();
	    		HttpPost post = new HttpPost(uri);
	    		
	    		//Creates JSON object and sets parameters
	    		JSONObject json = new JSONObject();
	    		JSONObject m = new JSONObject();
	    		m.put("email", username);
	    		m.put("password", password);
	    		json.put("user", m);
	    		
	    		//Sets properties of "POST" object
	    		StringEntity se = new StringEntity(json.toString());
	    		post.setEntity(se);
	    		post.setHeader("Accept", "application/json");
	    		post.setHeader("Content-type", "application/json");
	    		BasicResponseHandler responseHandler = new BasicResponseHandler();
	    		
	    		//Executes "POST" and saves response in string
	    		String responseString = client.execute(post, responseHandler);
	    		
	    		//Creates GSON to read server response
	    		GsonBuilder g = new GsonBuilder();
	    		g.setDateFormat("E MMM d HH:mm:ss Z y");
	    		Gson gson = g.create();
	    		User um = gson.fromJson(responseString, User.class);
	    		
	    		if(um.getID() != 0)
	    		{
	    			//Sets a cookie 
	    			CookieStore cookieStore = client.getCookieStore();
	    			HttpContext localContext = new BasicHttpContext(); 
	    			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	    			
	    			//Sets some global parameters
	    			Global.localContext = localContext;
	    			Global.client = client;
	    			Global.user_id = um.getID();
	    			
	            	Intent myIntent = new Intent(getApplicationContext(), Root.class);
	                startActivity(myIntent);
	            } else {
	            	Toast.makeText(this, "Failed to Login", Toast.LENGTH_LONG).show();  
	            	Toast.makeText(this, "Please Try Again Later", Toast.LENGTH_LONG).show(); 
	            }
	    	}
	    	//Prints error message if anything goes wrong
	    	catch (Exception e){
	    		Toast.makeText(this, "Unable to Login", Toast.LENGTH_LONG).show();  
	    		Toast.makeText(this, "Please Try Again Later", Toast.LENGTH_LONG).show();  
	    		Log.e("Login", e.getMessage());
	    	}
    	}
    }
    
    public void goToHomeView(View view)
    {
    	Intent myIntent = new Intent(getApplicationContext(), Home.class);
        startActivity(myIntent);
    }
}