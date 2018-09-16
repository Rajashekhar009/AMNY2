package com.solar.allmediany;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

public class EditProfileActivity extends HeaderActivity {

	ImageView topLeftBtn;
	EditText tvFname, tvLname, tvAddr, tvZip, tvPhone;
	TextView titlename, tvDOB, tvCountry, tvState, tvCity, tvUsername, tvEmail;
	Button btnEditProf;
	public static final int DATE_DIALOG_ID = 0;
	private int mYear, mMonth, mDay;
    
    public Context ct;
	public static PreferenceConnector pref = new PreferenceConnector();
	
	ArrayList<HashMap<String, String>> tempdata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> logindata = new ArrayList<HashMap<String, String>>();
	
	public JSONObject jo;	
	public static final String DataTag 				= "DataTable";	
	public static final String DataTagArr 			= "UserData";	
	private static final String CUSTID 				= "UserID";
	private static final String CUST_FNAME 			= "UserFname";
	private static final String CUST_LNAME 			= "UserLname";
	private static final String CUST_EMAIL 			= "CustEmail";
	private static final String CUST_DOB 			= "CustDOB";	
	private static final String CUST_UNAME 			= "UserUname";
	private static final String CUST_ADDR 			= "UserAddr";	
	private static final String CUST_PHONE 			= "Phone";
	private static final String CUST_ZIP 			= "ZipCode";
	private static final String CUST_CITY 			= "UserCityName";
	private static final String CUST_CITY_ID 		= "UserCityID";
	private static final String CUST_STATE 			= "UserStateName";
	private static final String CUST_STATE_ID 		= "UserStateID";	
	private static final String CUST_STATE_SHORTNAME= "UserStateShortName";
	private static final String CUST_COUNTRY 		= "UserCountryName";	
	private static final String CUST_COUNTRY_ID 	= "UserCountryID";
		
	public static final int RESULT_OK = 1, SELECT_CITY = 4, SELECT_STATE = 5, SELECT_COUNTRY = 6;
	
	public String userId, strUsername = "", strFname = "", strLname = "", strEmail = "", strZip = "",
			strCity = "", strCityId = "", strState = "", strStateId = "", strStateShortname = "", 
			strCountry = "", strCountryId  = "", strPhone	= "", strDOB = "", strAddr = "";   
	
	protected static boolean YES;
	public static final String TAG = "EditProfileActivity";
	private static final String CHECK_MSG = "StatusMsg";
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile_activity);		
		
		ct = getApplicationContext();
		
		titlename = (TextView)findViewById(R.id.titlename);
		titlename.setTypeface(font);
		
		topLeftBtn  = (ImageView)findViewById(R.id.topLeftBtn);		
		btnEditProf = (Button)findViewById(R.id.btnEditProf);			
		tvUsername 	= (TextView) findViewById(R.id.tvUsername);		
		tvEmail 	= (TextView) findViewById(R.id.tvEmail);
		tvFname 	= (EditText) findViewById(R.id.tvFname);
		tvLname		= (EditText) findViewById(R.id.tvLname);
		tvDOB		= (TextView) findViewById(R.id.tvDOB);
		tvCountry	= (TextView) findViewById(R.id.tvCountry);
		tvState		= (TextView) findViewById(R.id.tvState);
		tvCity		= (TextView) findViewById(R.id.tvCity);
		tvAddr		= (EditText) findViewById(R.id.tvAddr);
		tvZip		= (EditText) findViewById(R.id.tvZip);
		tvPhone		= (EditText) findViewById(R.id.tvPhone);	
		
		userId		= pref.readString(ct, pref.CUSTID,"");
		strUsername	= pref.readString(ct, pref.CUST_UNAME,"");
		strFname 	= pref.readString(ct, pref.CUST_FNAME,"");
        strLname 	= pref.readString(ct, pref.CUST_LNAME,"");
        strEmail 	= pref.readString(ct, pref.CUST_EMAIL,"");
        strAddr	 	= pref.readString(ct, pref.CUST_ADDR,"");
        strPhone 	= pref.readString(ct, pref.CUST_PHONE,"");
        strDOB	 	= pref.readString(ct, pref.CUST_DOB,"");
        strZip 		= pref.readString(ct, pref.CUST_ZIP,"");
        strCity 	= pref.readString(ct, pref.CUST_CITY,"");
        strState 	= pref.readString(ct, pref.CUST_STATE,"");
        strCountry 	= pref.readString(ct, pref.CUST_COUNTRY,"");         
        strCityId 	= pref.readString(ct, pref.CUST_CITY_ID,"");
        strStateId 	= pref.readString(ct, pref.CUST_STATE_ID,"");
        strCountryId= pref.readString(ct, pref.CUST_COUNTRY_ID,"");    
        
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

		Log.d("AMNYTRACE", "strDOB:"+strDOB);

        tvDOB.setText(strDOB);

		tvFname.setTypeface(font);
		tvLname.setTypeface(font);
		tvAddr.setTypeface(font);
		tvZip.setTypeface(font);
		tvPhone.setTypeface(font);
		tvDOB.setTypeface(font);
		tvCountry.setTypeface(font);
		tvState.setTypeface(font);
		tvCity.setTypeface(font);
		tvUsername.setTypeface(font);
		tvEmail.setTypeface(font);
		btnEditProf.setTypeface(font);
        
        tvDOB.setOnClickListener(new OnClickListener() {			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				  showDialog(DATE_DIALOG_ID);
			}
		});
        
        final Calendar c 	= Calendar.getInstance();

		if(!strDOB.equals("")){
			String dobArr[] = strDOB.split("-");
			mYear				= parseInt(dobArr[0]);
			mMonth 				= parseInt(dobArr[1]);
			mDay 				= parseInt(dobArr[2]);

			mMonth = mMonth-1;
		}
		else{
			mYear				= c.get(Calendar.YEAR);
			mMonth 				= c.get(Calendar.MONTH);
			mDay 				= c.get(Calendar.DAY_OF_MONTH);
		}

		topLeftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(getApplicationContext(),MyAccountActivity.class);
		    	startActivity(i);	
			}		
		});	
		
		btnEditProf.setOnClickListener(new OnClickListener() {			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {			

				strFname 	= tvFname.getText().toString();
				strLname 	= tvLname.getText().toString();
				strPhone 	= tvPhone.getText().toString();				
				strZip 		= tvZip.getText().toString();
				strAddr		= tvAddr.getText().toString();
				
				if(null==strFname || strFname.length()==0 || strFname.equalsIgnoreCase("")){
					Toast.makeText(getApplicationContext(),"Please enter first name",Toast.LENGTH_LONG).show();
				}
				else if(null==strLname || strLname.length()==0 || strLname.equalsIgnoreCase("")){
					Toast.makeText(getApplicationContext(),"Please enter last name",Toast.LENGTH_LONG).show();
				}
				else if(null==strDOB || strDOB.length()==0 || strDOB.equalsIgnoreCase(" ")){
					Toast.makeText(getApplicationContext(),"Please enter date of birth",Toast.LENGTH_LONG).show();
				}
				else if(null==strCountryId || strCountryId.length()==0 || strCountryId.equalsIgnoreCase(" ")){
					Toast.makeText(getApplicationContext(),"Please Select Country",Toast.LENGTH_LONG).show();
				}
				else if(null==strStateId || strStateId.length()==0 || strStateId.equalsIgnoreCase(" ")){
					Toast.makeText(getApplicationContext(),"Please Select State",Toast.LENGTH_LONG).show();
				}
				else if(null==strCityId || strCityId.length()==0 || strCityId.equalsIgnoreCase(" ")){
					Toast.makeText(getApplicationContext(),"Please Select City",Toast.LENGTH_LONG).show();
				}
				else if(null==strAddr || strAddr.length()==0 || strAddr.equalsIgnoreCase("")){
					Toast.makeText(getApplicationContext(),"Please enter address",Toast.LENGTH_LONG).show();
				}
				else if(null==strZip || strZip.length()==0 || strZip.equalsIgnoreCase("")){
					Toast.makeText(getApplicationContext(),"Please enter zip code",Toast.LENGTH_LONG).show();
				}
				else if(null==strPhone || strPhone.length()==0 || strPhone.equalsIgnoreCase("")){
					Toast.makeText(getApplicationContext(),"Please enter phone number",Toast.LENGTH_LONG).show();
				}
				else{							
					
					ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
					
					if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {			
						
						strFname	= strFname.replaceAll(" ","%20");
						strLname	= strLname.replaceAll(" ","%20");
						strPhone	= strPhone.replaceAll(" ","%20");	
						strZip 		= strZip.replaceAll(" ","%20");
						strAddr 	= strAddr.replaceAll(" ","%20");						
					   	
						new Signup().execute();
					}
					else
					{
						AlertDialog alertDialog = new AlertDialog.Builder(EditProfileActivity.this, ProgressDialog.THEME_HOLO_LIGHT).create();
						alertDialog.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+"'>Information</font>"));
						alertDialog.setMessage("Internet is not available in your Device, Please check your Internet connectivity and try again");
						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										Intent i=new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
										startActivity(i);
									}
								});
						alertDialog.show();
					}					
				}				
			}		
		});			
		
		tvCountry.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(getApplicationContext(),CountriesActivity.class);
		    	startActivityForResult(i, SELECT_COUNTRY);	
			}		
		});
		
		tvState.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {		
				
				if(strCountryId.equalsIgnoreCase(""))
				{
					Toast.makeText(getApplicationContext(), "Please Select Your Country", Toast.LENGTH_LONG).show();
				}else
				{
					Intent i = new Intent(getApplicationContext(),StatesActivity.class);
					i.putExtra("CountryId", strCountryId);
					startActivityForResult(i, SELECT_STATE);
				}
			}		
		});
		
		tvCity.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(strStateId.equalsIgnoreCase(""))
				{
					Toast.makeText(getApplicationContext(), "Please Select Your State", Toast.LENGTH_LONG).show();
				}else
				{
					Intent i = new Intent(getApplicationContext(),CityActivity.class);
					i.putExtra("StateId", strStateId);
					startActivityForResult(i, SELECT_CITY);	
				}
			}		
		});
		
	}
	
	 protected Dialog onCreateDialog(int id) {
	        switch (id) {
	            case DATE_DIALOG_ID:
	            	Log.d("AMNYTRACE", "Create mYear:"+mYear+" mMonth:"+mMonth+" mDay:"+mDay);
	                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
	        }
	        return null;
	    }

	    @Override
	    protected void onPrepareDialog(int id, Dialog dialog) {
	        switch (id) {	         
	            case DATE_DIALOG_ID:
					Log.d("AMNYTRACE", "Prepare mYear:"+mYear+" mMonth:"+mMonth+" mDay:"+mDay);
	                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
	                break;
	        }
	    }    

		private void updateDisplay() {
			 //tvDOB.setText(new StringBuilder().append(mMonth+1).append("-").append(mDay).append("-").append(mYear).append(""));
			tvDOB.setText(new StringBuilder().append(mYear).append("").append("-").append(mMonth+1).append("-").append(mDay));
			strDOB = (tvDOB.getText().toString()).replaceAll(" ","%20");
		}
		private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDisplay();
			}
		};
	
	private class Signup extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
        	tempdata.clear();
	        logindata.clear();
			pd.setMessage("We are processing, Please wait...");
			pd.show();
        }
        @Override  
        protected Void doInBackground(Void... args) {    	
           		try {
           			HttpClient httpClient = new DefaultHttpClient();
           			//http://allmediany.com/service/allmediany-service.svc/getUserUpdateProfile/format=json/UserID=12152/UserFname=shekhar/UserLname=Anumalla/CountryID=3/StateID=46/CityID=600/UserAddr=5thavenue/ZipCode=10019/Phone=9491322773/dob=1983-04-06
           			String url = "http://allmediany.com/service/allmediany-service.svc/getUserUpdateProfile/format=json/UserID="+userId+"/UserFname="+strFname+"/UserLname="+strLname+"/CountryID="+strCountryId+"/StateID="+strStateId+"/CityID="+strCityId+"/UserAddr="+strAddr+"/ZipCode="+strZip+"/Phone="+strPhone+"/dob="+strDOB+"/";
           			Log.d("URL","EditProfileActivity: "+url);           			
           			
           			HttpGet httpRequest = new HttpGet(url);
	       			HttpResponse response = httpClient.execute(httpRequest);
	       			
	       			String result = EntityUtils.toString(response.getEntity());
	       			JSONObject json=new JSONObject(result); 
	       			JSONArray ja = json.getJSONArray(DataTag); 
	       				
	       			int n = ja.length(); 
	       			Log.d("URL"," result: "+result); 
	       				       			
	       			for (int i = 0; i < n; i++) {	  	       				
	       				
	       				JSONObject c 				= ja.getJSONObject(i);    		
	       	    		JSONObject jo 				= c.getJSONObject(DataTagArr);
	       	    		
	       				HashMap<String, String> map = new HashMap<String, String>();    				
	       				
	       				if(!jo.has("StatusMsg"))
	       				{	       					
	       					map.put(CHECK_MSG, 				"Success");
		       				map.put(CUSTID, 				jo.getString("UserID"));	       				
		       				map.put(CUST_FNAME, 			jo.getString("UserFname"));	
		       				map.put(CUST_LNAME, 			jo.getString("UserLname"));
		       				map.put(CUST_UNAME, 			jo.getString("UserUname"));
		       				map.put(CUST_EMAIL, 			jo.getString("CustEmail"));	
		       				map.put(CUST_ADDR, 				jo.getString("UserAddr"));
		       				map.put(CUST_PHONE, 			jo.getString("Phone"));	
		       				map.put(CUST_DOB, 				jo.getString("CustDOB"));
		       				map.put(CUST_ZIP, 				jo.getString("ZipCode"));	
		       				map.put(CUST_CITY, 				jo.getString("UserCityName"));
		       				map.put(CUST_CITY_ID, 			jo.getString("UserCityID"));	
		       				map.put(CUST_STATE, 			jo.getString("UserStateName"));
		       				map.put(CUST_STATE_ID, 			jo.getString("UserStateID"));
		       				map.put(CUST_STATE_SHORTNAME, 	jo.getString("UserStateShortName"));	
		       				map.put(CUST_COUNTRY, 			jo.getString("UserCountryName"));
		       				map.put(CUST_COUNTRY_ID, 		jo.getString("UserCountryID"));	      				
	       				}
	       				else
	       				{
	       					Log.d("URL","Details: "+jo.getString("StatusMsg")); 
	       					map.put(CHECK_MSG, 		jo.getString("StatusMsg"));
	       				}
	       				tempdata.add(map);   				
	       				logindata = tempdata;
	       			}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}             
				return null;
	     }
	     @SuppressWarnings("deprecation")
		 @Override
	    protected void onPostExecute(Void result) {
	        	   
	        	   pd.dismiss();
	        		   
	        		   if(logindata.get(0).get(CHECK_MSG).equalsIgnoreCase("Success"))
	       				{        			   
	       				
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_FNAME, 	checkData(logindata.get(0).get(CUST_FNAME)));       		   
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_LNAME, 	checkData(logindata.get(0).get(CUST_LNAME)));	
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_ADDR, 	checkData(logindata.get(0).get(CUST_ADDR)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_PHONE, 	checkData(logindata.get(0).get(CUST_PHONE)));		        		   
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_DOB, 	checkData(logindata.get(0).get(CUST_DOB)));		        		   
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_ZIP, 	checkData(logindata.get(0).get(CUST_ZIP)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_CITY, 	checkData(logindata.get(0).get(CUST_CITY)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_CITY_ID,checkData(logindata.get(0).get(CUST_CITY_ID)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_STATE, 	checkData(logindata.get(0).get(CUST_STATE)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_STATE_ID, 	checkData(logindata.get(0).get(CUST_STATE_ID)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_STATE_SHORTNAME, 	checkData(logindata.get(0).get(CUST_STATE_SHORTNAME)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_COUNTRY, checkData(logindata.get(0).get(CUST_COUNTRY)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_COUNTRY_ID, checkData(logindata.get(0).get(CUST_COUNTRY_ID)));
	        		  	          		 
		          		   Intent i =new Intent(ct, MyAccountActivity.class);         			 
						   startActivity(i);
						   finish();
					   
	       				}
	        		   	else
	       				{
	        		   		String msg = logindata.get(0).get(CHECK_MSG);

							ad.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+"'>Authentication Error</font>"));
							ad.setMessage(msg);
							ad.setButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
										}
									});
							ad.show();
						}
	        	   
	           }   
	
}
	

protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
      	
		super.onActivityResult(requestCode, resultCode, data); 
      	   
		if (resultCode != RESULT_OK) return;    
      	 
     	switch (requestCode) {
     	
     			case SELECT_COUNTRY:     	 		    	
     				if (resultCode == 1) {
     					strCountryId 	= data.getStringExtra("CountryId");     					
     					strCountry 		= data.getStringExtra("CountryName"); 				
     	            	tvCountry.setText(strCountry);

						strState 			= "";
						strStateId 			= "";
						strStateShortname 	= "";
						tvState.setText("");

						strCityId = "";
						strCity = "";
						tvCity.setText("");
					}
     				break;
     				
     			case SELECT_STATE:     	 		    	
     				if (resultCode == 1) {
     					strState 			= data.getStringExtra("State"); 
     					strStateId 			= data.getStringExtra("StateId");     					
     					strStateShortname 	= data.getStringExtra("StateShortName");								
     	            	tvState.setText(strState);

						strCityId = "";
						strCity = "";
						tvCity.setText("");
     				}   		    	
     				break;
     				
     			case SELECT_CITY:     	 		    	
     				if (resultCode == 1) {
     					strCityId = data.getStringExtra("CityId");      					
     					strCity = data.getStringExtra("CityName");
     	            	tvCity.setText(strCity);     	            	
     				}   		    	
     				break;     			
     	    }	   
      }	    
    
    public String checkData(String val)
	{
		if(val.equalsIgnoreCase("null")){
			return "";
		}else{
			return val;
		}			
	}
		
}