package com.solar.allmediany;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAccountActivity extends HeaderActivity {
	
	ImageView topLeftBtn, topRightBtn;
	TextView titlename, appVersionCode;
	Button imgMyProf, imgMyFav, imgChngPwd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		setContentView(R.layout.my_account_activity);	

		titlename = (TextView)findViewById(R.id.titlename);		
		titlename.setTypeface(font);
		appVersionCode = (TextView)findViewById(R.id.appVersionCode);
		appVersionCode.setTypeface(font);
		topLeftBtn = (ImageView)findViewById(R.id.topLeftBtn);
		topRightBtn = (ImageView)findViewById(R.id.topRightBtn);
		imgMyProf = (Button)findViewById(R.id.imgMyProf);
		imgMyFav = (Button)findViewById(R.id.imgMyFav);
		imgChngPwd = (Button)findViewById(R.id.imgChngPwd);
        imgMyProf.setTypeface(font);
		imgMyFav.setTypeface(font);
		imgChngPwd.setTypeface(font);
		topLeftBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {	
			Intent i = new Intent(getApplicationContext(),Main.class);
			startActivity(i);
			}		
		});
		topRightBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder ad = new AlertDialog.Builder(MyAccountActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
				ad.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+"'>Confirm Logout</font>"));
				ad.setIcon(R.drawable.allmediany);
				ad.setMessage("Are you sure you want to Logout?");
				ad.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							   Context ct = getApplicationContext();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUSTID).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_FNAME).commit();				
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_LNAME).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_EMAIL).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_ADDR).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_PHONE).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_UNAME).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_ZIP).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_CITY).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_CITY_ID).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_STATE).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_STATE_ID).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_STATE_SHORTNAME).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_COUNTRY).commit();
								PreferenceConnector.getEditor(ct).remove(PreferenceConnector.CUST_COUNTRY_ID).commit();
																
								Intent i=new Intent(getApplicationContext(),Main.class);
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);										
							}
						});
				ad.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int which) {										
								dialog.cancel();
							}
						});
				ad.show();
			}		
		});
	}
	
	public void funOnclick(View v)
	{
		Intent i;
		if(v.getId()==R.id.imgMyProf){
			i = new Intent(getApplicationContext(),MyProfileActivity.class);
		}
		else if(v.getId()==R.id.imgMyFav){
			i = new Intent(getApplicationContext(),MyFavoritesActivity.class);
		}
		else{
			i = new Intent(getApplicationContext(),ChangePwdActivity.class);
		}
		startActivity(i);
	}
	
}