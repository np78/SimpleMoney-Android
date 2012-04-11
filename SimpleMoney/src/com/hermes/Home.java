package com.hermes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Home extends Activity{

	private int width, height;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        WindowManager mWinMgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE); 
        width = mWinMgr.getDefaultDisplay().getWidth(); 
        height = mWinMgr.getDefaultDisplay().getHeight();
    	
        //LinearLayout center = (LinearLayout) findViewById(R.id.center);
    	//Button register = (Button) findViewById(R.id.register);
    	//Button signIn = (Button) findViewById(R.id.signIn);

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
