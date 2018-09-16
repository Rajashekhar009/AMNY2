package com.solar.allmediany;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.solar.allmediany.R.id.etForgotEmail;

public class UserLoginActivity extends HeaderActivity {
	
	ArrayList<HashMap<String, String>> tempdata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> logindata = new ArrayList<HashMap<String, String>>();
	
	public JSONObject jo;	
	public static final String DataTag 				= "DataTable";	
	public static final String DataTagArr 			= "UserData";
	public static final String CHECK_MSG 			= "StatusMsg";		
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
	
	TextView titlename, id, title, desc, date, load, txtvFrPwd, txtvNewSignup;
	Button btnLogin;	
	ListView list;
	ImageView topLeftBtn;
	RelativeLayout loadview, listlayout;		
	EditText editUname, editPwd;	
	String uname, pwd, Loginresult="";		
	ProgressBar progressBar;
	ProgressDialog pd;
	
	public Context ct;
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "." + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}"
        );	
	protected static boolean YES;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity); 
        
        ct = getApplicationContext();   

		titlename = (TextView)findViewById(R.id.titlename);		
		titlename.setTypeface(font);
        
		topLeftBtn = (ImageView)findViewById(R.id.topLeftBtn);		
        btnLogin = (Button) findViewById(R.id.btnLogin);
		txtvFrPwd = (TextView) findViewById(R.id.txtvFrPwd);
		txtvNewSignup = (TextView) findViewById(R.id.txtvNewSignup);
		editUname = (EditText) findViewById(R.id.editUname);
		editPwd = (EditText) findViewById(R.id.editPwd);

		btnLogin.setTypeface(font);
		txtvFrPwd.setTypeface(font);
		txtvNewSignup.setTypeface(font);
		editUname.setTypeface(font);
		editPwd.setTypeface(font);
		
		topLeftBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i;			
				i = new Intent(ct, Main.class);			
				startActivity(i);				
			}
		});
		txtvFrPwd.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i;			
				i=new Intent(ct, ForgotPwdActivity.class);			
				startActivity(i);
			}
		});
		
		txtvNewSignup.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i;			
				i=new Intent(ct, RegistrationActivity.class);			
				startActivity(i);
			}
		});
		
		btnLogin.setOnClickListener(new OnClickListener() {			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager)getApplicationContext(). getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editUname.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

				uname = editUname.getText().toString();
				uname = uname.replaceAll(" ", "%20");				
				pwd = editPwd.getText().toString();
				pwd = pwd.replaceAll(" ", "%20");
			    
				if(null==uname || uname.length()==0 || uname.equals("")){				    	
					editUname.setError("Please enter Username");
			    }
			    else if(null==pwd || pwd.length()==0 || pwd.equals("")){
			    	editPwd.setError("Please enter your Passowrd");
			    }
			    else{
			    	
					ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
					
					if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {			
						new LoginService().execute();
					}
					else
					{		
						AlertDialog alertDialog = new AlertDialog.Builder(UserLoginActivity.this, ProgressDialog.THEME_HOLO_LIGHT).create();
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
        
    }  
    
    private class LoginService extends AsyncTask<Void, Void, Void> {  
		 
		 protected void onPreExecute() {
			 tempdata.clear();
	         logindata.clear();
	         pd = new ProgressDialog(UserLoginActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
			 pd.setCancelable(false);
			 pd.setIcon(R.drawable.allmediany);
			 pd.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+"'>"+getResources().getString(R.string.app_name)+"</font>"));
			 pd.setMessage("We are processing, Please wait...");
			 pd.show();
	     }  
	     @Override  
	     protected Void doInBackground(Void... args) {
	       		try {
	       			HttpClient httpClient = new DefaultHttpClient();
	       			String url = "http://allmediany.com/service/allmediany-service.svc/getUserDetails/format=json/UserUname="+uname+"/UserPwd="+pwd+"";
	       			Log.d("URL","LoginActivity LOGINURL: "+url);
	       			HttpGet httpRequest = new HttpGet(url);
	       			HttpResponse response = httpClient.execute(httpRequest);	       			
	       			String result = EntityUtils.toString(response.getEntity());
	       			JSONObject json=new JSONObject(result); 
	       			JSONArray ja = json.getJSONArray(DataTag);
	       			Log.d("URL",DataTag+" result: "+result);
	       			int n = ja.length();
	       			for (int i = 0; i < n; i++) {
	       				JSONObject c 	= ja.getJSONObject(i);    		
	       	    		jo 				= c.getJSONObject(DataTagArr);
	       				HashMap<String, String> map = new HashMap<String, String>();
	       				/*if (!ja.getJSONObject(i).has("StatusMsg")) {
	       				  map.put("Second Value", "N.A");
	       				} else {
	       				  map.put("Second Value", jArray.getJSONObject(j).getString("child2"));
	       				}*/	       				
	       				
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
	       					map.put(CHECK_MSG, jo.getString("StatusMsg"));
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
	        	   
	        	   if(logindata.size()==1){	 	        		   
	        		   
	        		    if(!jo.has("StatusMsg"))
	       				{
	       				
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUSTID, 		checkData(logindata.get(0).get(CUSTID)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_FNAME, 	checkData(logindata.get(0).get(CUST_FNAME)));       		   
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_LNAME, 	checkData(logindata.get(0).get(CUST_LNAME)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_UNAME, 	checkData(logindata.get(0).get(CUST_UNAME)));		        		   
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_EMAIL, 	checkData(logindata.get(0).get(CUST_EMAIL)));	
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_ADDR, 	checkData(logindata.get(0).get(CUST_ADDR)));
		        		   PreferenceConnector.writeString(ct, PreferenceConnector.CUST_PHONE, 	checkData(logindata.get(0).get(CUST_PHONE)));
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
	        		   		
	       					AlertDialog alertDialog = new AlertDialog.Builder(UserLoginActivity.this, ProgressDialog.THEME_HOLO_LIGHT).create();
							alertDialog.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+"'>Authentication Error</font>"));
							alertDialog.setMessage(msg);
							alertDialog.setIcon(R.drawable.allmediany);
							alertDialog.setButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {								
										}
									});
							alertDialog.show();      
	       				}
	          		 
	        	   }
	        	   else{
					    AlertDialog alertDialog = new AlertDialog.Builder(UserLoginActivity.this, ProgressDialog.THEME_HOLO_LIGHT).create();
					    alertDialog.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+"'>Authentication Error</font>"));
					    alertDialog.setMessage("Incorrect Email Id or Passowrd, Please try again.");
					    alertDialog.setIcon(R.drawable.allmediany);
						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {								
									}
								});
						alertDialog.show();      
	        	   } 
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