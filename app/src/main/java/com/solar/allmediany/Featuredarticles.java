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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static com.solar.allmediany.CommonUtilities.funConvertTimesFulldate;
import static com.solar.allmediany.CommonUtilities.stipHtml;

public class Featuredarticles extends HeaderActivity{
	
	public TextView tvTtl, postdate, id, title, desc, date, titlename;
	public LinearLayout thumbnail;
	//public ImageLoader imageLoader;
	public String imagedownload;
	public WebView webViewDetails;
	public RelativeLayout relLayout1, relLayout2, rlayout2, loadview, listlayout, rlayout;

	public ProgressBar progressBar;
	public static final String KEY_ID  		= "IDS";
	public static final String KEY_TITLE 	= "title";
	public static final String KEY_DESC 	= "discription";
	public static final String KEY_PATH 	= "path";
	public static final String KEY_IMAGE 	= "logo";
	public static final String KEY_DATE 	= "PostedDate";
	public static final String KEY_VIDEOURL	= "VideoUrl";
	public JSONObject jo;
	ListView list;
	ImageView image, refresh;
	private AdView mAdView;
    
    HashMap<String,String> newsmap = new HashMap <String, String>();		
	ArrayList<HashMap<String, String>> tempdata 	= new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> storydata	= new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.featuredlistview);

		//MobileAds.initialize(this, "ca-app-pub-1245050593606452~8420268127");

		MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_home_footer));
		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		webViewDetails = (WebView) findViewById(R.id.webViewDetails);
        relLayout1	   = (RelativeLayout)findViewById(R.id.relLayout1);
        relLayout2	   = (RelativeLayout)findViewById(R.id.relLayout2); 
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
   	 	rlayout = (RelativeLayout)findViewById(R.id.rlayout);                       
        rlayout2 = (RelativeLayout)findViewById(R.id.rlayout2);

        TextView title = (TextView)findViewById(R.id.titlename);

		tvTtl = (TextView)findViewById(R.id.tvTtl);

        title.setTypeface(font);
		tvTtl.setTypeface(font);
        image=(ImageView)findViewById(R.id.artimageView1);

    	thumbnail=(LinearLayout)findViewById(R.id.thumbnail);
        desc=(TextView)findViewById(R.id.artdesc);  
        desc.setTypeface(font);
	    postdate=(TextView)findViewById(R.id.postdate);
	    postdate.setTypeface(font);
        date=(TextView)findViewById(R.id.date);
        date.setTypeface(font);

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
              }  
              @Override  
              protected Void doInBackground(Void... args) {  
                      try {  
                    	                      	                     
                          String URL = "http://allmediany.com/service/allmediany-service.svc/getFeaturedArticles/format=json/";
                                              
                          int n=0;
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
      	       	    		jo 				= c.getJSONObject("FeaturedArticles");       	    		
      	       	    		
      	       	    		String vidUrlCntnt = "FALSE";
          					HashMap<String, String> map = new HashMap<String, String>();
              				map.put(KEY_TITLE, CommonUtilities.stipHtml(jo.getString("title")));
       		              	map.put("IDS", jo.getString("IDS"));  
       		              	map.put(KEY_ID, jo.getString("IDS"));                            		      
       		            	map.put(KEY_IMAGE,jo.getString("logo"));                   		         	 
       		              	map.put(KEY_PATH,jo.getString("path"));
    		            	map.put(KEY_DESC, CommonUtilities.stipHtml(jo.getString("discription")));
       		              	map.put(KEY_DATE,jo.getString("PostedDate"));
         		            if(jo.getString("VideoUrl").length() >= 10){
         		            	vidUrlCntnt = "TRUE";
         		            }
         		            map.put(KEY_VIDEOURL, vidUrlCntnt);
         		            tempdata.add(map);   				
             				storydata=tempdata;  
             				newsmap = map;
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
        @SuppressLint("SetJavaScriptEnabled")
		@Override  
        protected void onPostExecute(Void result) {

			 tvTtl.setText(newsmap.get(KEY_TITLE));
    	     desc.setText(newsmap.get(KEY_DESC));
    	     desc.setMovementMethod(new ScrollingMovementMethod());    	     
    	     String date1=newsmap.get(KEY_DATE);    	   
    	     String date2=date1.replaceAll("T"," ");
    	     String date3=date2.replaceAll("-07:00","");

			 date3 = funConvertTimesFulldate(date3, "WORD");
			 date.setText(date3+" EST");
    	     //date.setText(date3);
    	     String vidId = newsmap.get(KEY_ID);
    	     String videoCat = "";
    	     if(newsmap.get(KEY_PATH).equalsIgnoreCase("News"))  {
    	    	 videoCat = "2";
 	         }else{
    	    	 videoCat = "1";
    	     }
    	     
    	     if(newsmap.get(KEY_VIDEOURL).equalsIgnoreCase("FALSE"))
 	         {        	
 	        	relLayout1.setVisibility(View.GONE);
 	         }
 	         else
 	         {   		    		
 	        	relLayout2.setVisibility(View.GONE);
 	    		webViewDetails.getSettings().setJavaScriptEnabled(true);    		
 	    		webViewDetails.setPadding(0, 0, 0, 0);
 	    		webViewDetails.setInitialScale(getScale());
 	    		
 	    		webViewDetails.loadUrl("http://www.allmediany.com/app/views/playvideo.php?txtWidth=650&txtHeight=360&vidType="+videoCat+"&vidId="+vidId);
 	         }
    	    
    	     String imageurl=newsmap.get(KEY_IMAGE).replaceAll(" ", "%20");
			imgLoader.DisplayImage(imageurl, image);
			 
			 rlayout.setVisibility(View.VISIBLE);
        	 progressBar.setVisibility(View.INVISIBLE);
        	 rlayout2.setVisibility(View.INVISIBLE);
        	 
	   }      

    }

	@SuppressLint("UseValueOf")
	public int getScale(){
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	    int width = display.getWidth(); 
	    Double val = new Double(width)/new Double(680);
	    val = val * 100d;
	    return val.intValue();
	}
	
	@Override
    public void onBackPressed() {   	 
    	 
    	 final WebView webview = (WebView)findViewById(R.id.webViewDetails);
    	 webview.stopLoading();
    	 webview.loadData("", "text/html", "utf-8");
    	 
    	 this.finish();
    }
		
}