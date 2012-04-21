package com.hermes;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private int width, height;	
	public User user;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
       
        WindowManager mWinMgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE); 
        width = mWinMgr.getDefaultDisplay().getWidth(); 
        height = mWinMgr.getDefaultDisplay().getHeight();
    	
    	EditText un = (EditText) findViewById(R.id.username);
        un.setWidth((int)(width/1.1));
    	un.setHeight((int)(height/50));
    	
    	EditText pw = (EditText) findViewById(R.id.password);
    	pw.setWidth((int)(width/1.1));
    	pw.setHeight((int)(height/50));
    }
    
    public void goToRootView(View view)
    {
    	EditText un = (EditText) findViewById(R.id.username);
    	EditText pw = (EditText) findViewById(R.id.password);
    	String username = un.getText().toString();
    	String password = pw.getText().toString();
    	try
    	{
    		//Working server connection
    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users/sign_in");
    		DefaultHttpClient client = new DefaultHttpClient();
    		HttpPost post = new HttpPost(uri);
    		
    		JSONObject json = new JSONObject();
    		JSONObject m = new JSONObject();
    		m.put("email", username);
    		m.put("password", password);
    		json.put("user", m);
    		
    		StringEntity se = new StringEntity(json.toString());
    		post.setEntity(se);
    		post.setHeader("Accept", "application/json");
    		post.setHeader("Content-type", "application/json");
    		BasicResponseHandler responseHandler = new BasicResponseHandler();
    		String responseString = client.execute(post, responseHandler);
    		
    		GsonBuilder g = new GsonBuilder();
    		g.setDateFormat("E MMM d HH:mm:ss Z y");
    		Gson gson = g.create();
    		User um = gson.fromJson(responseString, User.class);
    		
    		if(um.getID() != 0)
    		{
    			CookieStore cookieStore = client.getCookieStore();
    			HttpContext localContext = new BasicHttpContext(); 
    			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    			
    			Global.localContext = localContext;
    			Global.client = client;
    			Global.user_id = um.getID();
    			
            	Intent myIntent = new Intent(getApplicationContext(), Root.class);
                startActivity(myIntent);
            } else {
            	Toast.makeText(this, "Failed to Login", Toast.LENGTH_LONG).show();               
            }
    	}
    	catch (Exception e){
    		Log.e("Login Error",e.getMessage());
    	}
    }
    
    public void goToHomeView(View view)
    {
    	Intent myIntent = new Intent(getApplicationContext(), Home.class);
        startActivity(myIntent);
    }
}