package com.solar.allmediany;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static com.solar.allmediany.CommonUtilities.stipHtml;
import static com.solar.allmediany.HeaderActivity.font;

public class BreakingNewsCategory extends HeaderActivity {
	
	ProgressBar progressBar;
	public static String KEY_ID = "allmediany_flash_news_id";
	public static String KEY_TITLE = "allmediany_flash_news_title";
	public static String KEY_DESC = "allmediany_flash_news_desc";
	public static String KEY_IMAGE = "allmediany_flash_news_image";
	public JSONObject jo;
	ArrayList<HashMap<String, String>> tempdata 	= new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> storydata	= new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> servicelist = new ArrayList<HashMap<String, String>>();
	
	TextView id, title, desc, date, load, name;
	String idvalue, titlename;
	ListView lv;
	Breakingnewsadapter1 adapter;
	ImageView topLeftBtn, refresh;
	RelativeLayout loadview, listlayout;
	public AdView mAdView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlistview);

		//MobileAds.initialize(this, "ca-app-pub-1245050593606452~8420268127");
		MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_home_footer));
		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

        TextView title1=(TextView)findViewById(R.id.titlename);
        title1.setTypeface(font);

		refresh=(ImageView)findViewById(R.id.refresh);
		lv = (ListView)findViewById(R.id.list);

        refresh.setOnClickListener(new OnClickListener() {
	   		@Override
	   		public void onClick(View v) {
	   			new MyTask().execute(); 
	   		}
	   	});
        topLeftBtn = (ImageView)findViewById(R.id.topLeftBtn);
        topLeftBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        Intent i=getIntent();
	    titlename=i.getStringExtra("titlename");
	    name=(TextView)findViewById(R.id.titlename);
	    name.setText(titlename);
	    
	    new MyTask().execute();
	    
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
              try {  
                      HttpClient hc = new DefaultHttpClient();  
                      
                      String URL = "http://allmediany.com/service/allmediany-service.svc/getPushNotificationList/format=json/";
              		
                      Log.d("URL","breakingNewsService:"+URL);
                      
                      HttpClient httpClient = new DefaultHttpClient();       	
          			HttpGet httpRequest = new HttpGet(URL);
          			HttpResponse response = httpClient.execute(httpRequest);
          			String result = EntityUtils.toString(response.getEntity());      			
          			JSONObject json=new JSONObject(result); 
   	       			JSONArray ja = json.getJSONArray("DataTable"); 	 	       			
          			int n = ja.length();         			
          			
          			for (int i = 0; i < n; i++) {         				
          				
          				JSONObject c 	= ja.getJSONObject(i);    		
  	       	    		jo 				= c.getJSONObject("FlashNewsList");       	    		
  	       	    		  	       	    		
      					HashMap<String, String> map = new HashMap<String, String>();	  					       				
          				
          				map.put(KEY_ID, jo.getString("flashNewsId"));            				     			
          				map.put(KEY_TITLE, stipHtml(jo.getString("flashNewsTitle")));
          				map.put(KEY_IMAGE, jo.getString("flashNewsImageSrc"));    		           	
          				map.put(KEY_DESC, stipHtml(jo.getString("flashNewsDesc")));
       		            
          				tempdata.add(map);   				
          				storydata=tempdata;        				
          				Log.d("storydata",""+storydata);
          			}
             } catch (Exception e) {  
                     Log.e("", "Error loading Service", e);  
             }
			return null;  
        }  
        @Override  
        protected void onPostExecute(Void result) {
			progressBar.setVisibility(View.GONE);
			loadview.setVisibility(View.GONE);
			listlayout.setVisibility(View.VISIBLE);
			adapter=new Breakingnewsadapter1(getApplicationContext(), storydata);
			lv.setAdapter(adapter);
			lv.setTextFilterEnabled(true);
        }
	}

  }

  class Breakingnewsadapter1 extends BaseAdapter {
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	public Breakingnewsadapter1(Context context, ArrayList<HashMap<String, String>> mylist) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.breakingnews, null);

		TextView desc = (TextView)vi.findViewById(R.id.desc);
		desc.setTypeface(font);
		TextView idget = (TextView)vi.findViewById(R.id.idvalue);
		HashMap<String, String> news = new HashMap<String, String>();
		news = data.get(position);
		desc.setText(news.get(stipHtml(BreakingNewsCategory.KEY_DESC)));
		idget.setText(news.get(BreakingNewsCategory.KEY_ID));

		return vi;

	}
}