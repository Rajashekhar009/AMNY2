package com.solar.allmediany;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProfileActivity extends HeaderActivity {
	
	ImageView topLeftBtn;
	TextView titlename, tvUsername, tvEmail, tvFname, tvLname, tvDOB, tvCountry, tvState, tvCity, tvAddr, tvZip, tvPhone;
	Button btnEditProf;
	public Context ct;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile_activity);
		
		ct = getApplicationContext();

		titlename = (TextView)findViewById(R.id.titlename);		
		titlename.setTypeface(font);
		
		topLeftBtn = (ImageView)findViewById(R.id.topLeftBtn);
		btnEditProf = (Button)findViewById(R.id.btnEditProf);
		tvUsername 	= (TextView) findViewById(R.id.tvUsername);		
		tvEmail 	= (TextView) findViewById(R.id.tvEmail);
		tvFname 	= (TextView) findViewById(R.id.tvFname);
		tvLname		= (TextView) findViewById(R.id.tvLname);
		tvDOB		= (TextView) findViewById(R.id.tvDOB);
		tvCountry	= (TextView) findViewById(R.id.tvCountry);
		tvState		= (TextView) findViewById(R.id.tvState);
		tvCity		= (TextView) findViewById(R.id.tvCity);
		tvAddr		= (TextView) findViewById(R.id.tvAddr);
		tvZip		= (TextView) findViewById(R.id.tvZip);
		tvPhone		= (TextView) findViewById(R.id.tvPhone);

		tvUsername.setTypeface(font);
		tvEmail.setTypeface(font);
		tvFname.setTypeface(font);
		tvLname.setTypeface(font);
		tvDOB.setTypeface(font);
		tvCountry.setTypeface(font);
		tvState.setTypeface(font);
		tvCity.setTypeface(font);
		tvAddr.setTypeface(font);
		tvZip.setTypeface(font);
		tvPhone.setTypeface(font);
		titlename.setTypeface(font);
		btnEditProf.setTypeface(font);
		
		String strUsername	= PreferenceConnector.readString(ct, PreferenceConnector.CUST_UNAME,"");
		String strFname 	= PreferenceConnector.readString(ct, PreferenceConnector.CUST_FNAME,"");
        String strLname 	= PreferenceConnector.readString(ct, PreferenceConnector.CUST_LNAME,"");
        String strEmail 	= PreferenceConnector.readString(ct, PreferenceConnector.CUST_EMAIL,"");
        String strAddr	 	= PreferenceConnector.readString(ct, PreferenceConnector.CUST_ADDR,"");
        String strPhone 	= PreferenceConnector.readString(ct, PreferenceConnector.CUST_PHONE,"");
        String strDOB	 	= PreferenceConnector.readString(ct, PreferenceConnector.CUST_DOB,"");
        String strZip 		= PreferenceConnector.readString(ct, PreferenceConnector.CUST_ZIP,"");
        String strCity 		= PreferenceConnector.readString(ct, PreferenceConnector.CUST_CITY,"");
        String strState 	= PreferenceConnector.readString(ct, PreferenceConnector.CUST_STATE,"");
        String strCountry 	= PreferenceConnector.readString(ct, PreferenceConnector.CUST_COUNTRY,"");
		
        tvUsername.setText(strUsername);
        tvFname.setText(strFname);
        tvLname.setText(strLname);
        tvEmail.setText(strEmail);
        tvAddr.setText(strAddr);
        tvPhone.setText(strPhone);
        tvZip.setText(strZip);
        tvCity.setText(strCity);
        tvState.setText(strState);
        tvCountry.setText(strCountry);
        tvDOB.setText(strDOB);        
        
		topLeftBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(getApplicationContext(),MyAccountActivity.class);
		    	startActivity(i);	
			}		
		});	
		
		btnEditProf.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(getApplicationContext(),EditProfileActivity.class);
		    	startActivity(i);	
			}		
		});	
				
	}
}