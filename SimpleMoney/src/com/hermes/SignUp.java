package com.hermes;

import java.net.URI;

import org.apache.http.client.HttpClient;
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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends Activity{
	
	private int width, height;	
	public UserModel user;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
       
        WindowManager mWinMgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE); 
        width = mWinMgr.getDefaultDisplay().getWidth(); 
        height = mWinMgr.getDefaultDisplay().getHeight();
    	
        EditText name = (EditText) findViewById(R.id.name);
    	name.setWidth((int)(width/1.1));
    	name.setHeight((int)(height/50));
    	
    	EditText email = (EditText) findViewById(R.id.email);
    	email.setWidth((int)(width/1.1));
    	email.setHeight((int)(height/50));
    	
    	EditText pw = (EditText) findViewById(R.id.password);
    	pw.setWidth((int)(width/1.1));
    	pw.setHeight((int)(height/50));
    	
    	TextView text1 = (TextView) findViewById(R.id.getPhoto);
    	text1.setWidth((int)(width/1.7));
    	Drawable d = text1.getBackground(); 
        PorterDuffColorFilter filter = new PorterDuffColorFilter(Color.parseColor("#3333FF"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
    	
    	TextView text2 = (TextView) findViewById(R.id.getLibrary);
    	text2.setWidth((int)(width/1.7));
    	d = text2.getBackground(); 
        filter = new PorterDuffColorFilter(Color.parseColor("#3333FF"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
    	
    	scalePhoto();
        //Toast.makeText(this, "" + name.getHeight(), Toast.LENGTH_LONG).show();
    }
    
    public void scalePhoto()
    {
    	ImageView photo = (ImageView) findViewById(R.id.photo);
    	photo.getLayoutParams().height = (int)(height/5.7);
    	photo.getLayoutParams().width = (int)(height/5.7);
    }
    
    public void goToHomeView(View view)
    {
    	Intent myIntent = new Intent(getApplicationContext(), Home.class);
        startActivity(myIntent);
    }
    
    public void signUp(View view)
    {
    	EditText email = (EditText) findViewById(R.id.email);
    	EditText pw = (EditText) findViewById(R.id.password);
    	EditText nameField = (EditText) findViewById(R.id.name);
    	String username = email.getText().toString();
    	String password = pw.getText().toString();
    	String name = nameField.getText().toString();
    	try
    	{
    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users");
    		HttpClient client = new DefaultHttpClient();
    		HttpPost post = new HttpPost(uri);
    		
    		JSONObject json = new JSONObject();
    		JSONObject m = new JSONObject();
    		m.put("name", name);
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
    		UserModel um = gson.fromJson(responseString, UserModel.class);
    		
    		if(um.getID() != 0)
    		{
            	Intent myIntent = new Intent(getApplicationContext(), Root.class);
            	myIntent.putExtra("User_ID", um.getID());
                startActivity(myIntent);
            } else {
            	Toast.makeText(this, "Unable to Sign Up", Toast.LENGTH_LONG).show();               
            }
    	}
    	catch (Exception e){
    		Log.e("eX",e.getMessage());
    	}
    }
}
