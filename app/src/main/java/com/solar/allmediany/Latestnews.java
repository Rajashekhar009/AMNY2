package com.solar.allmediany;

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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdSize;

public class Latestnews extends HeaderActivity {
	
	ProgressBar progressBar;	
	int progress, progressStatus 			= 0;
	public static final String KEY_ID  		= "NewsID";
	public static final String KEY_TITLE 	= "NewsTitle";
	public static final String KEY_DESC 	= "NewsArtDescription";
	public static final String KEY_IMAGE 	= "NewsArtImage";
	public static final String KEY_DATE 	= "NewsPostedDate";
	public static final String KEY_VIDEOURL	= "VideoUrl";
	public JSONObject jo;
	String regid="";
	ArrayList<HashMap<String, String>> tempdata 	= new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> storydata	= new ArrayList<HashMap<String, String>>();

	ListView lv;
	LatNewsAdptr adapter;
	RelativeLayout loadview, listlayout;
	ImageView refresh;
	TextView id, title, desc, date;
	//private AdView mAdView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.latestnewslistview);

		lv = (ListView)findViewById(R.id.list);

        TextView title1=(TextView)findViewById(R.id.titlename);
        title1.setTypeface(font);
        refresh=(ImageView)findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
			ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
				new MyTask().execute();
			}else{
				AlertDialog alertDialog = new AlertDialog.Builder(Latestnews.this).create();
				alertDialog.setTitle("Information");
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
        }); 
                
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {			
			new MyTask().execute();
		}else{							
			ad.setTitle("Information");
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
	
	private class MyTask extends AsyncTask<Void, Void, Void> {
	 	protected void onPreExecute() {
			 progressBar = (ProgressBar) findViewById(R.id.progressBar1);
			 loadview = (RelativeLayout)findViewById(R.id.layoutloading);
			 listlayout = (RelativeLayout)findViewById(R.id.listlayout);
			 loadview.setVisibility(View.VISIBLE);
			 listlayout.setVisibility(View.GONE);
			 progressBar.setVisibility(View.VISIBLE);
	 	}
	 	@Override
	 	protected Void doInBackground(Void... args) {
                 try 
                 {                      
                    //String URL = "http://www.postww.com/AllMediany/AllmedianyService.svc/GetLatestNews";
                    String URL = "http://allmediany.com/service/allmediany-service.svc/getLatestNews/format=json/";
                                        
                    int n = 0;
             		HttpClient httpClient = new DefaultHttpClient();       	
        			HttpGet httpRequest = new HttpGet(URL);
        			HttpResponse response = httpClient.execute(httpRequest);
        			String result = EntityUtils.toString(response.getEntity());      			
        			JSONObject json=new JSONObject(result); 
 	       			JSONArray ja = json.getJSONArray("DataTable"); 	 	       			
        			n = ja.length();         			
        			
        			if(n>0)
           			{
           				
           				tempdata.clear();       				
    	       			storydata.clear();
    	       			
        			for (int i = 0; i < n; i++) {         				
        				
        				JSONObject c 	= ja.getJSONObject(i);    		
	       	    		jo 				= c.getJSONObject("LatestNews");       	    		
	       	    		
	       	    		String vidUrlCntnt = "FALSE";
	       	    		
    					HashMap<String, String> map = new HashMap<String, String>();	  					       				
        				
        				map.put(KEY_ID, jo.getString("NewsID"));            				     			
        				map.put(KEY_TITLE, CommonUtilities.stipHtml(jo.getString("NewsTitle")));
        				map.put(KEY_IMAGE, jo.getString("NewsArtImage"));    		           	
        				map.put(KEY_DESC, CommonUtilities.stipHtml(jo.getString("NewsArtDescription")));
        				map.put(KEY_DATE, jo.getString("NewsPostedDate"));
     		            if(jo.getString("VideoUrl").length() >= 10){
     		            	vidUrlCntnt = "TRUE";
     		            }
     		            map.put(KEY_VIDEOURL, vidUrlCntnt);
        				tempdata.add(map);   				
        				storydata=tempdata;        				
        				Log.d("storydata",""+storydata);       				
        			}               
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
       	@Override
       	protected void onPostExecute(Void result) {
       		progressBar.setVisibility(View.GONE);
       		loadview.setVisibility(View.GONE);
       		listlayout.setVisibility(View.VISIBLE);
            adapter=new LatNewsAdptr(getApplicationContext(), storydata);
            lv.setAdapter(adapter);
	        lv.setTextFilterEnabled(true);
       	}
    }
	
	public class LatNewsAdptr extends BaseAdapter {
		 
		public ArrayList<HashMap<String, String>> data;
		public LayoutInflater inflater=null;
		  
		public LatNewsAdptr(Context context, ArrayList<HashMap<String, String>> mylist) {
			 this.data=mylist;
		     inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			 return data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
		    View vi=convertView;
			if(convertView==null)
				vi = inflater.inflate(R.layout.latestnews, null);

            LinearLayout mainlayout = (LinearLayout)vi.findViewById(R.id.mainlayout);
			TextView title = (TextView)vi.findViewById(R.id.title);
			TextView desc = (TextView)vi.findViewById(R.id.desc);

            title.setTypeface(font);
            desc.setTypeface(font);

			RelativeLayout rlAd = (RelativeLayout)vi.findViewById(R.id.rlAd);
			AdView mAdView1 = (AdView)vi.findViewById(R.id.adView1);
			AdView mAdView2 = (AdView)vi.findViewById(R.id.adView2);
			AdView mAdView3 = (AdView)vi.findViewById(R.id.adView3);
			AdView mAdView4 = (AdView)vi.findViewById(R.id.adView4);

			if(position%6==0) {
				if(position/6==0) {
					Log.d("AMNYTRACE", "1 position:"+position);
					AdRequest adRequest1 = new AdRequest.Builder().build();
					mAdView1.setVisibility(View.VISIBLE);
					mAdView2.setVisibility(View.GONE);
					mAdView3.setVisibility(View.GONE);
					mAdView4.setVisibility(View.GONE);
					mAdView1.loadAd(adRequest1);
				}
				else if(position/6==1){
					Log.d("AMNYTRACE", "2 position:"+position);
					AdRequest adRequest2 = new AdRequest.Builder().build();
					mAdView1.setVisibility(View.GONE);
					mAdView2.setVisibility(View.VISIBLE);
					mAdView3.setVisibility(View.GONE);
					mAdView4.setVisibility(View.GONE);
					mAdView2.loadAd(adRequest2);
				}
				else if(position/6==2){
					Log.d("AMNYTRACE", "3 position:"+position);
					AdRequest adRequest3 = new AdRequest.Builder().build();
					mAdView1.setVisibility(View.GONE);
					mAdView2.setVisibility(View.GONE);
					mAdView3.setVisibility(View.VISIBLE);
					mAdView4.setVisibility(View.GONE);
					mAdView3.loadAd(adRequest3);
				}
				else if(position/6==3){
					Log.d("AMNYTRACE", "4 position:"+position);
					AdRequest adRequest4 = new AdRequest.Builder().build();
					mAdView1.setVisibility(View.GONE);
					mAdView2.setVisibility(View.GONE);
					mAdView3.setVisibility(View.GONE);
					mAdView4.setVisibility(View.VISIBLE);
					mAdView4.loadAd(adRequest4);
				}
				rlAd.setVisibility(View.VISIBLE);
			}
			else{
				rlAd.setVisibility(View.GONE);
			}

			ImageView image=(ImageView)vi.findViewById(R.id.imageview1);
			HashMap<String, String> latestnews = new HashMap<String, String>();
			latestnews = data.get(position);
			title.setText(latestnews.get(KEY_TITLE));
			desc.setText(latestnews.get(KEY_DESC));
			String imageurl=latestnews.get(KEY_IMAGE).replaceAll(" ", "%20");
			imgLoader.DisplayImage(imageurl, image);

            mainlayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onListItemClick(position);
				}
			});

			return vi;
		}

		protected void onListItemClick(int position) {
            HashMap<String, String> map = new HashMap<String, String>();
			map = storydata.get(position);
			String date1=map.get(KEY_DATE);
			String date2=date1.replaceAll("T"," " );
			String date3=date2.replaceAll("-07:00","" );
			Intent in = new Intent(getApplicationContext(), Details.class);
			in.putExtra("catType","Latest News");
			in.putExtra("name", map.get(KEY_TITLE));
			in.putExtra("description", map.get(KEY_DESC));
			in.putExtra("date", date3);
			in.putExtra("imageurl", map.get(KEY_IMAGE).replaceAll(" ", "%20"));
			in.putExtra("videoStatus", map.get(KEY_VIDEOURL));
			in.putExtra("videoCat", "2");
			in.putExtra("vidID", map.get(KEY_ID));
			startActivity(in);
		}

	}
}