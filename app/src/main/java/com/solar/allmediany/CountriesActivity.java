package com.solar.allmediany;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CountriesActivity extends Activity {

	ArrayList<HashMap<String, String>> tempdata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> logindata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> citieslist=new ArrayList<HashMap<String, String>>();
	
	HashMap<String, String> currentmap  =	new HashMap<String, String>();
	HashMap<String, String> map2		=	new HashMap<String, String>();
	String countryid, countryname;
	public ListView listView;
	public ImageView topLeftBtn;
	
	ProgressDialog progressDialog;
	ProgressBar progressBar;
	
	public  static final String DataTagArr 			= "CountryList";
	public static final String CHECK_MSG 			= "StatusMsg";		
	public static final String COUNTRY_ID 			= "CountryID";
	public static final String COUNTRY 				= "CountryName";
	
	public JSONObject jo;
	TextView titlename;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countries_activity);
		listView=(ListView)findViewById(R.id.mylist);

		titlename = (TextView)findViewById(R.id.titlename);
		Typeface topeHeaderfont = Typeface.createFromAsset(getAssets(), "georgia.ttf");
		titlename.setTypeface(topeHeaderfont);

		topLeftBtn = (ImageView)findViewById(R.id.topLeftBtn);
		topLeftBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		new LoadCitiesService().execute();
	}	
	
	private class LoadCitiesService extends AsyncTask<Void, Void, Void> {  
		 
		 protected void onPreExecute() {
			 tempdata.clear();
	         logindata.clear();	         
	         progressBar = (ProgressBar) findViewById(R.id.progressBar1);
			 progressBar.setVisibility(View.VISIBLE);
	     }  
	     @Override  
	     protected Void doInBackground(Void... args) { 
	    	 
	    	 BufferedReader inStream = null;
	    	 
	       		try {
	       			
	       			HttpClient httpClient = new DefaultHttpClient();    
	       			String url = "http://allmediany.com/service/allmediany-service.svc/getCountries/format=json/";
	       			Log.d("URL","CountriesActivity URL: "+url);
	       			
	       			HttpGet httpRequest = new HttpGet(url);
	       			HttpResponse response = httpClient.execute(httpRequest);	       			
	       			String result = EntityUtils.toString(response.getEntity());
	       			JSONObject json=new JSONObject(result); 
	       			JSONArray ja = json.getJSONArray("DataTable"); 	
	       				       			
	       			int n = ja.length();
	       			
	       			for (int i = 0; i < n; i++) {	  	       				
	       				
	       				JSONObject c 	= ja.getJSONObject(i);    		
	       	    		jo 				= c.getJSONObject(DataTagArr); 				
	       				
	       				HashMap<String, String> map = new HashMap<String, String>();       				       				
	       				
	       				if(!jo.has("StatusMsg"))
	       				{		       			  					
		       				map.put(COUNTRY_ID, jo.getString("CountryID"));	       				
		       				map.put(COUNTRY, 	jo.getString("CountryName"));     				
	       				}
	       				else
	       				{	       					
	       					map.put(CHECK_MSG, 	jo.getString("StatusMsg"));
	       				}
	      		     	
	       				tempdata.add(map);   				
	       				logindata = tempdata;
	       				
	       				Log.d("ReplyData","logindata:"+logindata);
	       				
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
	        	   
	        	   progressBar.setVisibility(View.GONE);
	        	   
	        	   if(logindata.size()>=1){	 	        		   
	        		   
	        		   if(!jo.has("StatusMsg"))
	        		   {        			   	       				
		        		     	          		 
		        		    citieslist.addAll(logindata);		        				    
		        		   
		        			//ListAdapter adapter1 = new SimpleAdapter(CountriesActivity.this, citieslist, android.R.layout.simple_list_item_single_choice, new String[] {COUNTRY},	new int[] { android.R.id.text1});


						   ListAdapter adapter1 = new SimpleAdapter(CountriesActivity.this, citieslist, R.layout.listtextview, new String[] {COUNTRY}, new int[] {(R.id.textViewTest)});

		        			listView.setAdapter(adapter1);
		        			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		        			listView.setOnItemClickListener(new OnItemClickListener()  {

				        		@Override
				        		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				        			@SuppressWarnings("unchecked")
				        			HashMap<String, String> o = (HashMap<String, String>)listView.getItemAtPosition(position);
				        			countryname	= o.get("CountryName");					        		
				        			countryid	= o.get("CountryID");		        			
				        			Log.d("URL","countryname:"+countryname);
				        			Log.d("URL","countryid:"+countryid);
			        				Intent i=new Intent();
			        				i.putExtra("CountryId", countryid);
			        				i.putExtra("CountryName", 	countryname);
			        				setResult(1, i);
			        				finish();
				        		}
		        			});		        	 
					   
	       				}
	        		   	else
	       				{
	        		   		String msg = logindata.get(0).get(CHECK_MSG); 
	        		   		
	       					AlertDialog alertDialog = new AlertDialog.Builder(CountriesActivity.this).create();
							alertDialog.setTitle("Authentication Error");
							alertDialog.setMessage(msg);
							alertDialog.setIcon(R.drawable.delete);					
							alertDialog.setButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {								
										}
									});
							alertDialog.show();      
	       				}
	          		 
	        	   }else{
	        		   	AlertDialog alertDialog = new AlertDialog.Builder(CountriesActivity.this).create();
						alertDialog.setTitle("Authentication Error");
						alertDialog.setMessage("There is an error, Please try again.");
						alertDialog.setIcon(R.drawable.delete);					
						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {								
									}
								});
						alertDialog.show();      
	        	   } 
	        	  
	           }           
	
	}

}