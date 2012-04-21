package com.hermes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
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
