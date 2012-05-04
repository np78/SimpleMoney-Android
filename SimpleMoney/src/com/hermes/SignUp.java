package com.hermes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends Activity{
	
	private int width, height;	
	public User user;
	
	//YOU CAN EDIT THIS TO WHATEVER YOU WANT
    private static final int SELECT_PICTURE = 1;
    
    //Used for retrieving an image from phone camera or gallery
	protected ImageView _image;
	protected String selectImagePath;
	protected boolean _taken;
	private String selectedImagePath;
    private String filemanagerstring;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        //Forces screen to be portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       
        //Gets width and height of phone
        WindowManager mWinMgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE); 
        width = mWinMgr.getDefaultDisplay().getWidth(); 
        height = mWinMgr.getDefaultDisplay().getHeight();
    	
        //Sets height and width of name field
        EditText name = (EditText) findViewById(R.id.name);
    	name.setWidth((int)(width/1.1));
    	name.setHeight((int)(height/50));
    	
    	//Sets height and width of email field
    	EditText email = (EditText) findViewById(R.id.email);
    	email.setWidth((int)(width/1.1));
    	email.setHeight((int)(height/50));
    	
    	//Sets height and width of password field
    	EditText pw = (EditText) findViewById(R.id.password);
    	pw.setWidth((int)(width/1.1));
    	pw.setHeight((int)(height/50));
    	
    	//Sets height and width of "Take a Photo" button and sets its background color to light blue
    	TextView text1 = (TextView) findViewById(R.id.getPhoto);
    	text1.setWidth((int)(width/1.7));
    	Drawable d = text1.getBackground(); 
        PorterDuffColorFilter filter = new PorterDuffColorFilter(Color.parseColor("#3333FF"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
    	
        //Sets height and width of "Choose From Library" button and sets its background color to light blue
    	TextView text2 = (TextView) findViewById(R.id.getLibrary);
    	text2.setWidth((int)(width/1.7));
    	d = text2.getBackground(); 
        filter = new PorterDuffColorFilter(Color.parseColor("#3333FF"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
    	
        //Sets height and width of photo icon and gets the default missing photo from server
        ImageView photo = (ImageView) findViewById(R.id.photo);
    	photo.getLayoutParams().height = (int)(height/5.7);
    	photo.getLayoutParams().width = (int)(height/5.7);
    	photo.setImageDrawable(getImage("/images/medium/missing.png"));
    	
    	//Changes the "Sign Up" button to have a black background color
    	Button button1 = (Button) findViewById(R.id.ok);
    	d = button1.getBackground(); 
        filter = new PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
        
        //Changes the "Cancel" button to have a black background color
        Button button2 = (Button) findViewById(R.id.back);
    	d = button2.getBackground(); 
        filter = new PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
        
    	_image = photo;
    	//Path doesn't seem to do anything
        selectImagePath = Environment.getExternalStorageDirectory() + "/res/drawable/avatar.jpg";
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
	    	Log.e("Sign Up", "Null Image Error");
	    	Toast.makeText(this, "Internet may be down", Toast.LENGTH_LONG).show();
	    	return getResources().getDrawable( R.drawable.none ); 
		}
	}
    
    public void goToHomeView(View view)
    {
    	Intent myIntent = new Intent(getApplicationContext(), Home.class);
        startActivity(myIntent);
    }
    
    //Code is straight from Internet
    //Switches to built-in photo capture mode
    //Not fully debugged as saving your photo by pressing the okay button doesn't do anything
    //The cancel button also doesn't work after you take a photo and view your temporary image
    public void getPhotoFromPhone(View view)
    {
    	Toast.makeText(this, "Camera is under maintenance", Toast.LENGTH_LONG).show();
    	/*Log.i("MakeMachine", "startCameraActivity()" );
    	File file = new File( selectImagePath );
    	Uri outputFileUri = Uri.fromFile( file );
    	
    	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
    	intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
    	
    	startActivityForResult( intent, 0 );*/
    }
    
    
    //Code is straight from Internet
    //Switches to built-in gallery selection mode
    //Not fully debugged as a memory leak issue crashes the app
    //if the user continually uses this feature to select photos
    public void getPhotoFromLibrary(View view)
    {
    	Toast.makeText(this, "Gallery is under maintenance", Toast.LENGTH_LONG).show();
    	/*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);*/
    }
    
    //Checks sign up parameters
    public void signUp(View view)
    {
    	//Extracts user fields from widgets
    	EditText email = (EditText) findViewById(R.id.email);
    	EditText pw = (EditText) findViewById(R.id.password);
    	EditText nameField = (EditText) findViewById(R.id.name);
    	String username = email.getText().toString();
    	String password = pw.getText().toString();
    	String name = nameField.getText().toString();
    	
    	String[] tokens = username.split("@");	//Splits email address into two tokens at the "@"
    	boolean okay = true;
    	if(name.equals(""))
    	{
    		Toast.makeText(this, "Name Required", Toast.LENGTH_SHORT).show(); 
    		okay = false;
    	}
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
    	else
    	{
    		//Checks password length
	    	if(password.length() < 6)
	    	{
	    		Toast.makeText(this, "Password is too short", Toast.LENGTH_LONG).show(); 
	    		okay = false;
	    	}
    	}
    	if(okay)
    	{
	    	try
	    	{
	    		//Creates server communication objects
	    		URI uri = new URI("http://severe-leaf-6733.herokuapp.com/users");
	    		DefaultHttpClient client = new DefaultHttpClient();
	    		HttpPost post = new HttpPost(uri);
	    		
	    		JSONObject json = new JSONObject();
	    		JSONObject m = new JSONObject();
	    		
	    		//Tries to load image from gallery path and sets avatar image in bytes to json object
	    		//Doesn't work as nothing happens but the file is read and exists and bytes are what I'm supposed to put
	    		File f = null;
	    		if(selectedImagePath != null && !selectedImagePath.equals(""))
	    			f = new File(selectedImagePath);
	    		if(f.exists())
	    		{
	    			byte[] bytes = new byte[(int) f.length()];
	    			FileInputStream stream = new FileInputStream(f);
	    			stream.read(bytes);
	    			m.put("avatar", bytes);
	    		}
	    		m.put("name", name);
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
	            	Toast.makeText(this, "Login Incorrect", Toast.LENGTH_LONG).show();
	            }
	    	}
	    	//Prints error message if anything goes wrong
	    	catch (Exception e){
	    		Toast.makeText(this, "Unable to Sign Up", Toast.LENGTH_LONG).show();  
	    		Toast.makeText(this, "Please Try Again Later", Toast.LENGTH_LONG).show();  
	    		Log.e("Sign Up", e.getMessage());
	    	}
    	}
    }
    
    //Used when camera or gallery activities are running
    //Straight from Internet
    //Doesn't seem to respond to user clicking the okay button in the camera mode
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.i( "Result Code", "" + resultCode );
        switch( resultCode )
    	{
    		case 0:
    			Log.i( "MakeMachine", "User cancelled" );
    			break;
    			
    		case -1:
    			if(requestCode == SELECT_PICTURE)
    			{
    				Uri selectedImageUri = data.getData();

                    //OI FILE Manager
                    filemanagerstring = selectedImageUri.getPath();

                    //MEDIA GALLERY
                    selectedImagePath = getPath(selectedImageUri);

                    //DEBUG PURPOSE - you can delete this if you want
                    if(selectedImagePath!=null)
                        System.out.println(selectedImagePath);
                    else System.out.println("selectedImagePath is null");
                    if(filemanagerstring!=null)
                        System.out.println(filemanagerstring);
                    else System.out.println("filemanagerstring is null");

                    //NOW WE HAVE OUR WANTED STRING
                    if(selectedImagePath!=null)
                    {
                    	_image.setImageBitmap(null);
                    	_image.setImageDrawable(Drawable.createFromPath(selectedImagePath));
                        System.out.println("selectedImagePath is the right one for you!");
                    }
                    else
                        System.out.println("filemanagerstring is the right one for you!");
    			}
    			else
    				onPhotoTaken();
    			break;
    	}
    }

    //Straight from Internet
    //Used by gallery selection to get file path of photo
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
    
    //Sets the image icon on screen to photo selected
    //Straight from Internet
    protected void onPhotoTaken()
    {
    	Log.i( "MakeMachine", "onPhotoTaken" );
    	
    	_taken = true;
    	
    	BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
    	
    	Bitmap bitmap = BitmapFactory.decodeFile( selectImagePath, options );
    	
    	_image.setImageBitmap(bitmap);
    }
    
    //Straight from Internet
    //Not sure what it does
    protected void onRestoreInstanceState( Bundle savedInstanceState){
    	Log.i( "MakeMachine", "onRestoreInstanceState()");
    	if( savedInstanceState.getBoolean( SignUp.PHOTO_TAKEN ) ) {
    		onPhotoTaken();
    	}
    }
    
    //Straight from Internet
    //Not sure what it does
    protected void onSaveInstanceState( Bundle outState ) {
    	outState.putBoolean( SignUp.PHOTO_TAKEN, _taken );
    }
}
