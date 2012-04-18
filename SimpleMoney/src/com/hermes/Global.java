package com.hermes;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

public class Global {
	public static DefaultHttpClient client;
	public static HttpContext localContext;
	public static int user_id;
}
