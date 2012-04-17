package com.hermes;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

public class Image implements Runnable{
	
	private Activity context;
	private String url;
	
	public Image(Activity context, String url)
	{
		this.context = context;
		this.url = url;
	}

	public void run() {
		try
		{
			TextView userData = (TextView) context.findViewById(R.id.userData);
			
			HttpURLConnection connection = (HttpURLConnection) new URL("http://severe-leaf-6733.herokuapp.com" + url).openConnection(); 
		    connection.connect(); 
		    InputStream input = connection.getInputStream(); 
		 
		    Bitmap x = BitmapFactory.decodeStream(input); 
		    userData.setCompoundDrawables(new BitmapDrawable(x), null, null, null);
		}
		catch (Exception e) 
		{ 
			e.printStackTrace(); 
		}
	}

}
