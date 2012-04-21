package com.hermes;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
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
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Root extends Activity{
	
	int width, height, user_id;
	private User user;
	
	public void onCreate(Bundle savedInstanceState) {
		user_id = Global.user_id;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root);
        
        WindowManager mWinMgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE); 
        width = mWinMgr.getDefaultDisplay().getWidth(); 
        height = mWinMgr.getDefaultDisplay().getHeight();
        
        width = (int)(width/7.5);
        height = (int)(height/12.5);
        PorterDuffColorFilter filter = new PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP); 
        
        user = getUserData();
        TextView userData = (TextView) findViewById(R.id.userData);
        userData.setText(" " + user.getName() + "   (" + user.getEmail() + ")\n");
        userData.append(" Balance: " + user.getBalance());
        try {
        	userData.setCompoundDrawablesWithIntrinsicBounds(getImage(user.getAvatarURL()), null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Log.e("Small", user.getAvatarURLSmall());
        Log.e("Medium", user.getAvatarURL());
        userData.setBackgroundResource(R.layout.box);
        
        Button pic = (Button) findViewById(R.id.sendMoney);
        pic.getBackground().setColorFilter(filter);
        //pic.setMaxWidth((int)(width/1.5));
    	//pic.setMaxHeight((int)(height/12.5));
    	
    	pic = (Button) findViewById(R.id.pendingTransactions);
        pic.getBackground().setColorFilter(filter);
        //pic.setMaxWidth((int)(width/1.5));
    	//pic.setMaxHeight((int)(height/12.5));
        
        pic = (Button) findViewById(R.id.quickPay);
        pic.getBackground().setColorFilter(filter);
        
    	pic = (Button) findViewById(R.id.requestMoney);
        pic.getBackground().setColorFilter(filter);
        //pic.setMaxWidth((int)(width/1.5));
    	//pic.setMaxHeight((int)(height/12.5));
        
    	pic = (Button) findViewById(R.id.localAds);
        pic.getBackground().setColorFilter(filter);
        //pic.setMaxWidth((int)(width/1.5));
    	//pic.setMaxHeight((int)(height/12.5));
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
	
	public void goToSendMoneyView(View view)
	{
		Intent myIntent = new Intent(view.getContext(), SendMoney.class);
        startActivityForResult(myIntent, 0);
	}
	
	public void goToQuickPayView(View view)
	{
		Intent myIntent = new Intent(view.getContext(), QuickPay.class);
        startActivityForResult(myIntent, 0);
	}
	
	public void goToRequestMoneyView(View view)
	{
		Intent myIntent = new Intent(view.getContext(), RequestMoney.class);
		startActivityForResult(myIntent, 0);
	}
	
	public void goToPendingTransactionsView(View view)
	{
		Intent myIntent = new Intent(view.getContext(), PendingTransactions.class);
        startActivityForResult(myIntent, 0);
	}
	
	public void goToLocalAdsView(View view)
	{
		Intent myIntent = new Intent(view.getContext(), LocalAds.class);
        startActivityForResult(myIntent, 0);
	}
	
	public void goToLoginView(View view)
	{
		//Terminate session
		try
    	{URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users/sign_out");
    		HttpClient client = new DefaultHttpClient();
    		HttpDelete delete = new HttpDelete(uri);
    		JSONObject json = new JSONObject();
    		JSONObject m = new JSONObject();
    		m.put("name", user.getName());
    		m.put("email", user.getEmail());
    		json.put("user", m);
    		
    		StringEntity se = new StringEntity(json.toString());
    		delete.setHeader("Accept", "application/json");
    		delete.setHeader("Content-type", "application/json");
    		BasicResponseHandler responseHandler = new BasicResponseHandler();
    		String responseString = client.execute(delete, responseHandler);
    		
    		if(responseString != null)
    		{
            	Intent myIntent = new Intent(view.getContext(), Home.class);
        		startActivityForResult(myIntent, 0);
            } else {
            	Toast.makeText(this, "Unable to Log Out", Toast.LENGTH_LONG).show();               
            }
    	}
    	catch (Exception e){
    		Log.e("Logout Error",e.getMessage());
    	}
	}
}
