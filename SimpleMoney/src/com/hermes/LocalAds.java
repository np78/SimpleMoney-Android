package com.hermes;

//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*
 * <application
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name" >
        <uses-library 
        	android:name="com.google.android.maps"
        	android:required="true"/>
        	
   <com.google.android.maps.MapView
		android:id="@+id/mapView"
		android:layout_width="fill_parent"
        android:layout_height="fill_parent"
        android:layout_below="@id/back"
        android:clickable="true"
        android:enabled="true"
        android:apiKey="0CBS-8-bfi7olOGAdu_kE9OSZ3c5xQLlOIGKqPg" />
 */
public class LocalAds extends Activity{//MapActivity{
	
	private int user_id;
	
	public void onCreate(Bundle savedInstanceState) {
		user_id = Global.user_id;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_ads);
        
        //MapView mapView = (MapView) findViewById(R.id.mapView);    
        //mapView.setBuiltInZoomControls(true);
    }
	
	public void goToRootView(View view)
	{
		Intent myIntent = new Intent(getApplicationContext(), Root.class);
	    startActivity(myIntent);
	}
	
	protected boolean isRouteDisplayed()
	{
		return false;
	}
}