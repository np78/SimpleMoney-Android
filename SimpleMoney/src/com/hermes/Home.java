package com.hermes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        //Changes the "register" button to have a black background color
        Button button1 = (Button) findViewById(R.id.register);
    	Drawable d = button1.getBackground(); 
        PorterDuffColorFilter filter = new PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
        
        //Changes the "Sign In" button to have a black background color
        Button button2 = (Button) findViewById(R.id.signIn);
    	d = button2.getBackground(); 
        filter = new PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP); 
        d.setColorFilter(filter); 
    }
	
	public void goToLoginView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), Login.class);
        startActivity(myIntent);
	}
	
	public void goToSignUpView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(myIntent);
	}
}
