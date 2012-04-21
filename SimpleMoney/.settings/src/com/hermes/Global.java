package com.hermes;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Global {
	public static DefaultHttpClient client;
	public static HttpContext localContext;
	public static int user_id;
	//:avatar, :name, :email, :password, :password_confirmation, :remember_me, :currency, :address, :latitude, :longitude, :is_merchant, :description
}
