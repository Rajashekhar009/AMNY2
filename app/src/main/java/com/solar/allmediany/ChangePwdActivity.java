package com.solar.allmediany;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangePwdActivity extends HeaderActivity {
	
	ArrayList<HashMap<String, String>> tempdata = new ArrayList<HashMap<String,String>>();
	ArrayList<HashMap<String, String>> pwdData  = new ArrayList<HashMap<String,String>>();
	EditText eTOldPwd, eTNewPwd, eTCnfmPwd;
	String strOldPwd="", strNewPwd="", strCnfmPwd="";
	Button btnChngPwd;
	public String userId;	
	public static String CHECK_MSG	=	"StatusMsg", CHECK_STS	=	"Status";
	ImageView topLeftBtn;	
	TextView titlename, appVersionCode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_pwd_activity);	

		titlename = (TextView)findViewById(R.id.titlename);		
		titlename.setTypeface(font);
		
		userId = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.CUSTID,"");
	     
		topLeftBtn = (ImageView)findViewById(R.id.topLeftBtn);
		eTOldPwd = (EditText)findViewById(R.id.eTOldPwd);
		eTNewPwd = (EditText)findViewById(R.id.eTNewPwd);
		eTCnfmPwd = (EditText)findViewById(R.id.eTCnfmPwd);
		btnChngPwd = (Button)findViewById(R.id.btnChngPwd);

		eTOldPwd.setTypeface(font);
		eTNewPwd.setTypeface(font);
		eTCnfmPwd.setTypeface(font);
		btnChngPwd.setTypeface(font);

		appVersionCode = (TextView)findViewById(R.id.appVersionCode);
		appVersionCode.setTypeface(font);
		
		topLeftBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
			Intent i = new Intent(getApplicationContext(),MyAccountActivity.class);
			startActivity(i);
			}		
		});		
		
		btnChngPwd.setOnClickListener(new OnClickListener() {			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				
				strOldPwd 	= eTOldPwd.getText().toString();
				strNewPwd 	= eTNewPwd.getText().toString();
				strCnfmPwd 	= eTCnfmPwd.getText().toString();				
				
				if(null==strOldPwd || strOldPwd.length()==0 || strOldPwd.equalsIgnoreCase(" ")){
					eTOldPwd.setError("Please enter old password");
				}
				else if(null==strNewPwd || strNewPwd.length()==0 || strNewPwd.equalsIgnoreCase(" ")){
					eTNewPwd.setError("Please enter new password");
				}
				else if(null==strCnfmPwd || strCnfmPwd.length()==0 || strCnfmPwd.equalsIgnoreCase(" ")){
					eTCnfmPwd.setError("Please enter confirm password");
				}
				else if(!strNewPwd.equals(strCnfmPwd))
				{
					eTCnfmPwd.setError("Your passwords are not matched");
				}				
				else{							
					strOldPwd	= strOldPwd.replaceAll(" ","%20");
					strNewPwd	= strNewPwd.replaceAll(" ","%20");
					strCnfmPwd	= strCnfmPwd.replaceAll(" ","%20");						
					
					ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
					
					if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {			
						new funChangePwd().execute();
					}
					else
					{							
						ad.setMessage("Internet is not available in your Device, Please check your Internet connectivity and try again");
						ad.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									Intent i=new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);	        		
					        		startActivity(i);					        		
								}
							});       
						ad.show();
					}
											
				}				
			}		
		});
		
	}
	private class funChangePwd extends AsyncTask<Void, Void, Void>
	{
		protected void onPreExecute()
		{
			tempdata.clear();
	        pwdData.clear();
			pd.setMessage("We are processing, Please wait...");
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			try
			{
				HttpClient httpClient = new DefaultHttpClient();
				//String url  = "http://allmediany.com/service/allmediany-service.svc/getUserChangePwd/format=json/UserID=6365/OldPwd=22448293/NewPwd=22448293/";
				String url  = "http://allmediany.com/service/allmediany-service.svc/getUserChangePwd/format=json/UserID="+userId+"/OldPwd="+strOldPwd+"/NewPwd="+strNewPwd+"/";
				Log.d("URL", "ChangePwdActivity: "+url);
				HttpGet httpRequest = new HttpGet(url);
				HttpResponse response	=	httpClient.execute(httpRequest);
				String result = EntityUtils.toString(response.getEntity());
				
				Log.d("RESULT","ChangePwdActivity "+result);
				
				JSONObject jobj = new JSONObject(result);
				JSONArray  jarr = jobj.getJSONArray("DataTable");
				
				int n = jarr.length();
				
				for(int i=0; i<n; i++)
				{       	    		
					JSONObject jobj1 = jarr.getJSONObject(i);	
					JSONObject jobj2 = jobj1.getJSONObject("UserData");
					
					HashMap<String, String> map = new HashMap<String, String>();					
									
					map.put(CHECK_STS, 	jobj2.getString("Status"));
					map.put(CHECK_MSG, 	jobj2.getString("StatusMsg"));
					
					tempdata.add(map);
					pwdData = tempdata;				
					
				}				
				
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)			
			{
				e.printStackTrace();
			}
			return null;
		}
		
		@SuppressWarnings("deprecation")
		protected void onPostExecute(Void result)
		{
			pd.dismiss();
			
			if(pwdData.size()>=1){				
			
			String msg = pwdData.get(0).get(CHECK_MSG);
			final String status = pwdData.get(0).get(CHECK_STS);

			ad.setMessage(msg);
			ad.setButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if(status.equalsIgnoreCase("1"))
							{
								eTOldPwd.setText("");
								eTNewPwd.setText("");
								eTCnfmPwd.setText("");							
								strOldPwd	= "";
								strNewPwd	= "";
								strCnfmPwd	= "";
								finish();
								//Intent i = new Intent(getApplicationContext(), MyAccountActivity.class);
								//startActivity(i);
							}
						}
					});
				ad.show();
			}
			else{
				ad.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+getResources().getString(R.string.autherr)+"</font>"));
				ad.setMessage("There is an Error, Please try again once..");
				ad.setButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
				ad.show();
			}
		}
		
	}
}