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

    private String selectedImagePath;
    //ADDED
    private String filemanagerstring;
    
	protected ImageView _image;
	protected String _path;
	protected boolean _taken;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       
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
    	
        ImageView photo = (ImageView) findViewById(R.id.photo);
    	photo.getLayoutParams().height = (int)(height/5.7);
    	photo.getLayoutParams().width = (int)(height/5.7);
    	photo.setImageDrawable(getImage("/images/medium/missing.png"));
        
    	_image = photo;
    	//Path doesn't seem to do anything
        _path = Environment.getExternalStorageDirectory() + "/res/drawable/avatar.jpg";
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
    
    public void goToHomeView(View view)
    {
    	Intent myIntent = new Intent(getApplicationContext(), Home.class);
        startActivity(myIntent);
    }
    
    public void getPhotoFromPhone(View view)
    {
    	Log.i("MakeMachine", "startCameraActivity()" );
    	File file = new File( _path );
    	Uri outputFileUri = Uri.fromFile( file );
    	
    	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
    	intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
    	
    	startActivityForResult( intent, 0 );
    }
    
    public void getPhotoFromLibrary(View view)
    {
    	Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
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
    		DefaultHttpClient client = new DefaultHttpClient();
    		HttpPost post = new HttpPost(uri);
    		
    		JSONObject json = new JSONObject();
    		JSONObject m = new JSONObject();
    		
    		File f = null;
    		if(selectedImagePath != null && !selectedImagePath.equals(""))
    			f = new File(selectedImagePath);
    		else if (_path != null && !_path.equals(""))
    			f = new File(_path);
    		if(f != null)
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
            	Toast.makeText(this, "Unable to Sign Up", Toast.LENGTH_LONG).show();               
            }
    	}
    	catch (Exception e){
    		Log.e("Sign Up Error",e.getMessage());
    	}
    }
    
  //UPDATED
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
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
                    System.out.println("selectedImagePath is the right one for you!");
                else
                    System.out.println("filemanagerstring is the right one for you!");
            }
        }
        
        switch( resultCode )
    	{
    		case 0:
    			Log.i( "MakeMachine", "User cancelled" );
    			break;
    			
    		case -1:
    			onPhotoTaken();
    			break;
    	}
    }

    //UPDATED!
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
    
    protected void onPhotoTaken()
    {
    	Log.i( "MakeMachine", "onPhotoTaken" );
    	
    	_taken = true;
    	
    	BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
    	
    	Bitmap bitmap = BitmapFactory.decodeFile( _path, options );
    	
    	_image.setImageBitmap(bitmap);
    }
    
    protected void onRestoreInstanceState( Bundle savedInstanceState){
    	Log.i( "MakeMachine", "onRestoreInstanceState()");
    	if( savedInstanceState.getBoolean( SignUp.PHOTO_TAKEN ) ) {
    		onPhotoTaken();
    	}
    }
    
    protected void onSaveInstanceState( Bundle outState ) {
    	outState.putBoolean( SignUp.PHOTO_TAKEN, _taken );
    }
}
