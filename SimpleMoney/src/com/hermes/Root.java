package com.hermes;

import java.io.InputStream;
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
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
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
        //Forces screen to be portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        //Gets width and height of phone, not used though
        WindowManager mWinMgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE); 
        width = mWinMgr.getDefaultDisplay().getWidth(); 
        height = mWinMgr.getDefaultDisplay().getHeight();
        
        //Creates blackground for widgets
        PorterDuffColorFilter filter = new PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP); 
        
        //Gets user data and sets top row with user avatar, name, email, and balance
        user = getUserData();
        TextView userData = (TextView) findViewById(R.id.userData);
        userData.setText(" " + user.getName() + "   (" + user.getEmail() + ")\n");
        userData.append(" Balance: " + user.getBalance());
        userData.setCompoundDrawablesWithIntrinsicBounds(getImage(user.getAvatarURL()), null, null, null);
        userData.setBackgroundResource(R.layout.box);
        
        //Applies black background to "Send Money" button
        Button pic = (Button) findViewById(R.id.sendMoney);
        pic.getBackground().setColorFilter(filter);
    	
        //Applies black background to "Transactions" button
    	pic = (Button) findViewById(R.id.pendingTransactions);
        pic.getBackground().setColorFilter(filter);
        
        //Applies black background to "Send Money" button
        pic = (Button) findViewById(R.id.quickPay);
        pic.getBackground().setColorFilter(filter);
        
        //Applies black background to "Request Money" button
    	pic = (Button) findViewById(R.id.requestMoney);
        pic.getBackground().setColorFilter(filter);
        
        //Applies black background to "Local Deals" button
    	pic = (Button) findViewById(R.id.localAds);
        pic.getBackground().setColorFilter(filter);
    }
	
	//Retrieves a photo at the specified user path
	public Drawable getImage(String url)
	{
		try
		{
			if(url.equals("/images/medium/missing.png") || url.equals("/images/small/missing.png"))
				url = "http://severe-leaf-6733.herokuapp.com" + url;
		    return Drawable.createFromStream((InputStream)new URL(url).getContent(), "src");
		}
		//If an error occurs, a notice is displayed for user and "None" photo from
		//Drawable folder is used instead.
	    catch (Exception e) {
	    	Log.e("Root", "Null Image Error");
	    	return getResources().getDrawable( R.drawable.none );
		}
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
			return um;
		}
		//Displays error messages and go to Home view
		catch (Exception e) {
			Log.e("Root", "Unable to retrieve User data");
			Toast.makeText(this, "Unable to Retrieve Data", Toast.LENGTH_LONG).show(); 
			Toast.makeText(this, "Please Try Again Later", Toast.LENGTH_LONG).show(); 
			Intent myIntent = new Intent(getApplicationContext(), Home.class);
    		startActivityForResult(myIntent, 0);
		}
		return null;
	}
	
	public void goToSendMoneyView(View view)
	{
		Intent myIntent = new Intent(view.getContext(), SendMoney.class);
        startActivityForResult(myIntent, 0);
	}
	
	//Goes to a QR scanner
	//Was working before but I'm not sure what I changed to bresk it
	public void goToQuickPayView(View view)
	{
		Toast.makeText(this, "QuickPay is under maintenance", Toast.LENGTH_LONG).show();
		/*Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, 0);*/
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
    	{
			URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users/sign_out");
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
    		Toast.makeText(this, "Unable to Logout", Toast.LENGTH_LONG).show();  
    		Toast.makeText(this, "Please Try Again Later", Toast.LENGTH_LONG).show();  
    		Log.e("Root", "Unable to logout");
    	}
	}
	
	//Handles users interaction with QR reader
	//Haven't tested yet
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		    	  String contents = intent.getStringExtra("SCAN_RESULT");
		    	  String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
			         
		          //Handle successful scan
		    	  //Not test
		          Toast.makeText(this, contents, Toast.LENGTH_LONG).show();
		          try
		     	  {
		     		 URI uri = new URI("http://severe-leaf-6733.herokuapp.com/transactions");
		     		 HttpClient client = Global.client;
		     		 HttpPost post = new HttpPost(uri);
		     		
		     		 JSONObject json = new JSONObject();
		     		 JSONObject m = new JSONObject();
		     		 //Complicated string parser that gets email from QR contents
		     		 String email = contents.substring(contents.indexOf('\"')+1, contents.indexOf('\"', contents.indexOf('\"')+1));
		     		 m.put("recipient_email", email);
		     		 m.put("complete", false);
		     		 json.put("transaction", m);
		     		
		     		 StringEntity se = new StringEntity(json.toString());
		     		 post.setEntity(se);
		     		 post.setHeader("Accept", "application/json");
		     		 post.setHeader("Content-type", "application/json");
		     		 BasicResponseHandler responseHandler = new BasicResponseHandler();
		     		 HttpResponse responseString = client.execute(post, Global.localContext);
			     		
			         if(responseString.getStatusLine().getStatusCode() == 200)
			         {
			     		 Toast.makeText(this, "Scan Complete to:\n" + email, Toast.LENGTH_LONG).show();
			         } else {
			             Toast.makeText(this, "Invalid QR", Toast.LENGTH_LONG).show();               
			         }
			     }
			     catch (Exception e){
			    	 Toast.makeText(this, "QR not Recognized", Toast.LENGTH_LONG).show(); 
			     	 Log.e("Root", "QR not Recognized");
			     }
			}
		    //Is unwantingly invoked when user pressed back button on phone to go to root menu
		    else if (resultCode == RESULT_CANCELED) {
			     // Handle cancel
			     Toast.makeText(this, "Scan Fail", Toast.LENGTH_LONG).show();
			}
		}
	}
}
