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

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.google.android.gms.ads.MobileAds;

import static android.R.attr.data;
import static com.solar.allmediany.CommonUtilities.stipHtml;
import static com.solar.allmediany.HeaderActivity.font;
import static com.solar.allmediany.HeaderActivity.imgLoader;
import static com.solar.allmediany.R.id.mainlayout;

public class MyFavoritesActivity extends HeaderActivity {

	ArrayList<HashMap<String, String>> tempdata 	= new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> storydata	= new ArrayList<HashMap<String, String>>();
	
	ProgressBar progressBar;

	static final String KEY_TITLE 		= "editorChoiceTitle";//1
	static final String KEY_ID 			= "ID";//2
	static final String KEY_IMAGE 		= "imgSrc";//3
	static final String KEY_PATH 		= "path";//4
	static final String KEY_VIDEOURL	= "VideoUrl"; //5
	static final String KEY_DATE 		= "PostedDate"; //6
	static final String KEY_DESC 		= "DataDescription"; //7
	static final String KEY_CAT 		= "CategoryName"; //7
	
	TextView id, title, desc, date;
	ListView lv;
	MyFavoriteAdapter adapter;
	ImageView refresh;
	RelativeLayout loadview, listlayout;
	public JSONObject jo;
	public String userId;
	ImageView topLeftBtn;
	private AdView mAdView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_favorites_activity);
		lv = (ListView)findViewById(R.id.list);
        TextView title1=(TextView)findViewById(R.id.titlename);
        title1.setTypeface(font);

		//MobileAds.initialize(this, "ca-app-pub-1245050593606452~8420268127");
		MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_home_footer));
		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

        userId = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.CUSTID,"");
        topLeftBtn = (ImageView)findViewById(R.id.topLeftBtn);
        topLeftBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {	
				finish();			
			}		
		});
        refresh=(ImageView)findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {
   		
	   		@Override
	   		public void onClick(View v) {
	   			new MyTask().execute(); 
	   		}
	   	});
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
                   
                	 String URL = "http://allmediany.com/service/allmediany-service.svc/getFavoritesList/format=json/UserID="+userId;	 
                	 int n=0;
              		 HttpClient httpClient = new DefaultHttpClient();       	
         			 HttpGet httpRequest = new HttpGet(URL);
         			 HttpResponse response = httpClient.execute(httpRequest);
         			 String result = EntityUtils.toString(response.getEntity());      			
         			 JSONObject json=new JSONObject(result); 
  	       			 JSONArray ja = json.getJSONArray("DataTable"); 	 	       			
         			 n = ja.length();         			
         			
         			 for (int i = 0; i < n; i++) {         				
         				
         				JSONObject c 	= ja.getJSONObject(i);    		
 	       	    		jo 				= c.getJSONObject("FavoriteList");       	    		
 	       	    		
 	       	    		String vidUrlCntnt = "FALSE";
 	       	    		
     					HashMap<String, String> map = new HashMap<String, String>();
     					
         				//map.put(KEY_EID, jo.getString("editorChoiceID"));
  		                map.put(KEY_TITLE, stipHtml(jo.getString("title")));
  		                map.put(KEY_ID, jo.getString("IDS"));           	          	                      	          		                      	        	
          	           	map.put(KEY_IMAGE,jo.getString("logo"));		                      	           	
          	           	map.put(KEY_PATH,jo.getString("path"));	       
              	  		  	         		            
       		            if(jo.getString("VideoUrl").length() >= 10){
       		            	vidUrlCntnt = "TRUE";
       		            }
       		            map.put(KEY_VIDEOURL, vidUrlCntnt);	     		            
          	           	map.put(KEY_DATE,jo.getString("PostedDate")); 
          	           	map.put(KEY_DESC,jo.getString("discription")); 
          	           	map.put(KEY_CAT,jo.getString("CategoryName")); 
          	              	
          	           	tempdata.add(map);   				
          	           	storydata=tempdata;      					        
          		        
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
		adapter = new MyFavoriteAdapter(getApplicationContext(), storydata);
		lv.setAdapter(adapter);
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

				HashMap<String, String> editorlist = new HashMap<String, String>();
				editorlist = storydata.get(position);

				String videoCat = "";
				if(editorlist.get(Editorchoice.KEY_PATH).equals("News")){
					videoCat = "2";					
				}else{
					videoCat = "1";
				}
				
				Intent in = new Intent(getApplicationContext(), Details.class);
				in.putExtra("catType", editorlist.get(Editorchoice.KEY_CAT));
			    in.putExtra("name", CommonUtilities.stipHtml(editorlist.get(Editorchoice.KEY_TITLE)));
			    in.putExtra("description", CommonUtilities.stipHtml(editorlist.get(Editorchoice.KEY_DESC)));
			    in.putExtra("date", editorlist.get(Editorchoice.KEY_DATE));
			    in.putExtra("imageurl", editorlist.get(Editorchoice.KEY_IMAGE).replaceAll(" ", "%20"));
			    in.putExtra("videoStatus", editorlist.get(Editorchoice.KEY_VIDEOURL));
			    in.putExtra("videoCat", videoCat);
			    in.putExtra("vidID", editorlist.get(Editorchoice.KEY_ID));
			    startActivity(in);				
			}
		   });
		}
    }

	public class MyFavoriteAdapter extends BaseAdapter {
	 	
		public ArrayList<HashMap<String, String>> data;
	    public LayoutInflater inflater=null;
		
	    public MyFavoriteAdapter(Context context, ArrayList<HashMap<String, String>> mylist) {
		 	this.data = mylist;
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
			vi = inflater.inflate(R.layout.editorchoice, null);
			TextView title = (TextView)vi.findViewById(R.id.title);
			title.setTypeface(font);
			ImageView image=(ImageView)vi.findViewById(R.id.imageview1);
			HashMap<String, String> editorlist = new HashMap<String, String>();
			editorlist = data.get(position);
			title.setText(editorlist.get(Editorchoice.KEY_TITLE));
			String imageurl=editorlist.get(Editorchoice.KEY_IMAGE).replaceAll(" ", "%20");
			imgLoader.DisplayImage(imageurl, image);
			return vi;
		}

	}

}