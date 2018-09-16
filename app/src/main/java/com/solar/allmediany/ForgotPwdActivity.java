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
import android.widget.TextView;

import static com.solar.allmediany.R.id.tvEmail;

public class ForgotPwdActivity extends Activity {

	public static String DataTagArr = "OrdForgotPassword";
	EditText etForgotEmail;
	public String StrForgotEmail ="";
	protected static boolean YES;
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}@[a-zA-Z0-9][a-zA-Z0-9-]{0,64}.[a-zA-Z0-9][a-zA-Z0-9-]{0,25}");
	ArrayList<HashMap<String, String>> tempdata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> logindata = new ArrayList<HashMap<String, String>>();
	private static final String CHECK_MSG = "CheckMsg";	
	ProgressDialog pd;
	ImageView topLeftBtn;
	TextView titlename;	
	Button btnFrgtDone;
	public Context ct;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password_activity);
		
		ct = getApplicationContext();  
		
		Typeface topeHeaderfont = Typeface.createFromAsset(getAssets(), "georgia.ttf");
		
		titlename = (TextView)findViewById(R.id.titlename);		
		titlename.setTypeface(topeHeaderfont);
		
		etForgotEmail = (EditText) findViewById(R.id.etForgotEmail);
		
		topLeftBtn  = (ImageView)findViewById(R.id.topLeftBtn);
		btnFrgtDone		= (Button)findViewById(R.id.btnFrgtDone);
		
		topLeftBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
			Intent i = new Intent(getApplicationContext(),UserLoginActivity.class);
			startActivity(i);
			}		
		});
		
		btnFrgtDone.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager)getApplicationContext(). getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etForgotEmail.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

			StrForgotEmail = etForgotEmail.getText().toString();
			if(null==StrForgotEmail || StrForgotEmail.length()==0 || StrForgotEmail.equalsIgnoreCase(" ")){
				etForgotEmail.setError("Please enter Email Id");
			}
			else if(checkEmail(StrForgotEmail)==YES){
				etForgotEmail.setError("Please enter a valid Email Id");
			}
			else{
				ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

				if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
					new ForgotPwdService().execute();
				}
				else
				{
					AlertDialog alertDialog = new AlertDialog.Builder(ForgotPwdActivity.this, ProgressDialog.THEME_HOLO_LIGHT).create();
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
	private boolean checkEmail(String emailid) {
		boolean YES = EMAIL_ADDRESS_PATTERN.matcher(emailid).matches();
		return YES;
	}
	private class ForgotPwdService extends AsyncTask<Void, Void, Void> {
		String msg = "";
		 protected void onPreExecute() {
			 tempdata.clear();
	         logindata.clear();
	         pd = new ProgressDialog(ForgotPwdActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
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
	       			String url = "http://allmediany.com/service/allmediany-service.svc/getUserForgotPwd/format=json/UserEmail="+StrForgotEmail+"";
	       			Log.d("URL","ForgotPwdActivity: "+url);
	       			
	       			HttpGet httpRequest = new HttpGet(url);
	       			HttpResponse response = httpClient.execute(httpRequest);	       			
	       			String result = EntityUtils.toString(response.getEntity());
	       			JSONObject json=new JSONObject(result); 
	       			JSONArray ja = json.getJSONArray("DataTable"); 
	
	       			int n = ja.length();  			          			
	       			
	       			for (int i = 0; i < n; i++) {	  				
	       				JSONObject c 				= ja.getJSONObject(i);    		
	       	    		JSONObject jo 				= c.getJSONObject("UserData");       				
	       				HashMap<String, String> map = new HashMap<String, String>();	       				
	       				map.put(CHECK_MSG, 				jo.getString("StatusMsg"));		       					       				
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

			   AlertDialog alertDialog = new AlertDialog.Builder(ForgotPwdActivity.this, ProgressDialog.THEME_HOLO_LIGHT).create();
			   alertDialog.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+"'>Information</font>"));
			   //alertDialog.setMessage("Incorrect Email Id or Passowrd, Please try again.");
			   alertDialog.setIcon(R.drawable.allmediany);

				   msg = "";
			   if(logindata.size()==1){
				   msg = logindata.get(0).get(CHECK_MSG);
				   alertDialog.setMessage(msg);
			   }
			   else{
				   alertDialog.setMessage("Some error has been occured, Please try again...");
			   }

			   alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(msg.equalsIgnoreCase("Password has been sent to your Email!!")){
							Intent i = new Intent(getApplicationContext(),UserLoginActivity.class);
							startActivity(i);
						}
					}
			   });

        	   alertDialog.show();    
	     }           
	}

}