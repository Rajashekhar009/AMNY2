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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static android.R.attr.name;
import static com.solar.allmediany.CommonUtilities.funConvertTimesFulldate;

public class Details extends HeaderActivity {

	public String userId, catType, title, datadesc, videoStatus, videoCat, strID, reqFrom, imageurl;
	public WebView webViewDetails;
	public RelativeLayout relLayout1, relLayout2;
	public ImageView imgViewVideo, topLeftBtn, imgButFav;
	public static String CHECK_MSG	= "StatusMsg";
	ArrayList<HashMap<String, String>> tempdata = new ArrayList<HashMap<String,String>>();
	ArrayList<HashMap<String, String>> pwdData  = new ArrayList<HashMap<String,String>>();
	private AdView mAdView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.details);

		//MobileAds.initialize(this, "ca-app-pub-1245050593606452~8420268127");

		MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_home_footer));
		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

        webViewDetails = (WebView) findViewById(R.id.webViewDetails);

		relLayout1	   = (RelativeLayout)findViewById(R.id.relLayout1);
        relLayout2	   = (RelativeLayout)findViewById(R.id.relLayout2);        

        topLeftBtn = (ImageView)findViewById(R.id.topLeftBtn);
        topLeftBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});	        
	              
        imgButFav = (ImageView)findViewById(R.id.imgButFav);
          
        userId = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.CUSTID,"");

        ImageView image=(ImageView)findViewById(R.id.artimageView1);
        TextView topAppTitle = (TextView)findViewById(R.id.titlename);
        TextView tvTtl=(TextView)findViewById(R.id.tvTtl);
        
        tvTtl.setTypeface(font);
        topAppTitle.setTypeface(font);
        TextView pdate=(TextView)findViewById(R.id.postdate);  
        pdate.setTypeface(font);
        
        imgViewVideo = (ImageView)findViewById(R.id.imgViewVideo);        
        TextView artdate=(TextView)findViewById(R.id.date);               
        TextView artdescription=(TextView)findViewById(R.id.artdesc);	          
        artdescription.setTypeface(font);
        artdate.setTypeface(font);
        
        Intent i = getIntent();    
        	
        catType = i.getStringExtra("catType");        
        topAppTitle.setText(catType);	        
        title=i.getStringExtra("name");
		tvTtl.setText(title);
        String date					= i.getStringExtra("date");

		date = funConvertTimesFulldate(date, "WORD");

        artdate.setText(date+" EST");
        String description			= i.getStringExtra("description");
        artdescription.setText(description);
        artdescription.setMovementMethod(new ScrollingMovementMethod());
        datadesc					= description;	     
        imageurl 					= i.getStringExtra("imageurl");        
        videoStatus 				= i.getStringExtra("videoStatus");       
        videoCat 					= i.getStringExtra("videoCat");
        strID		 				= i.getStringExtra("vidID");        
                        
        if(videoStatus.equalsIgnoreCase("FALSE")){
        	relLayout1.setVisibility(View.GONE);
        }
        else{
        	relLayout2.setVisibility(View.GONE);
    		webViewDetails.getSettings().setJavaScriptEnabled(true);    		
    		
    		//webViewDetails.getSettings().setBuiltInZoomControls(true);
    		//webViewDetails.getSettings().setAllowFileAccess(true);    		    
    		    
    		webViewDetails.setPadding(0, 0, 0, 0);
    		webViewDetails.setInitialScale(getScale()); 		
    		webViewDetails.loadUrl("http://www.allmediany.com/app/views/playvideo.php?txtWidth=650&txtHeight=360&vidType="+videoCat+"&vidId="+strID);
    		    		    		
    		/*webViewDetails.setOnTouchListener(new OnTouchListener() {    			
    			public boolean onTouch(View v, MotionEvent event) {
    				Toast.makeText(getApplicationContext(), "Clicked Webview", Toast.LENGTH_SHORT).show();
    				return false;
    			}
    		});*/
        }        
               
      	//imageLoader.download(imageurl,iv);
		imgLoader.DisplayImage(imageurl, image);
        
      	imgViewVideo.setOnClickListener(new OnClickListener() {			  
			@Override
			public void onClick(View v) {	
			 Intent in = new Intent(Details.this, VideoActivity.class);
			 in.putExtra("videoCat", videoCat);
			 in.putExtra("vidID", strID);
			 startActivity(in);
			}		
		});
 	
      	imgButFav.setOnClickListener(new OnClickListener() {		
    		@Override
    		public void onClick(View v) {
			if(userId.equalsIgnoreCase("")){
				Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
				startActivity(intent);
			}else{
				new funAddtoFav().execute();
			}
    		}
    	});
	
	}		
	
	private class funAddtoFav extends AsyncTask<Void, Void, Void>{
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
				//String url  = "http://allmediany.com/service/allmediany-service.svc/makeAddTofavorite/format=xml/UserID=12129/FavID=14475/Type=1";
								
				String url  = "http://allmediany.com/service/allmediany-service.svc/makeAddTofavorite/format=json/UserID="+userId+"/FavID="+strID+"/Type="+videoCat+"/";
				Log.d("URL", "Details: "+url);
				HttpGet httpRequest = new HttpGet(url);
				HttpResponse response	=	httpClient.execute(httpRequest);
				String result = EntityUtils.toString(response.getEntity());
				
				Log.d("RESULT","Add to favorites "+result);
				
				JSONObject jobj = new JSONObject(result);
				JSONArray  jarr = jobj.getJSONArray("DataTable");
				int n = jarr.length();
				for(int i=0; i<n; i++)
				{       	    		
					JSONObject jobj1 = jarr.getJSONObject(i);	
					JSONObject jobj2 = jobj1.getJSONObject("AmnyData");
					
					HashMap<String, String> map = new HashMap<String, String>();
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

		protected void onPostExecute(Void result)
		{
			pd.dismiss();
			
			if(pwdData.size()>=1){	
				
				//imgButFav.setBackgroundResource(R.drawable.favorite_select_icon);
				String msg = pwdData.get(0).get(CHECK_MSG);
				//ad.setIcon(R.drawable.allmediany);
				//ad.setTitle("AllMediaNY");
				ad.setMessage(msg);
				ad.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						});
					ad.show();
			}
			else{
				//ad.setIcon(R.drawable.allmediany);
				//ad.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+getResources().getString(R.string.autherr)+"</font>"));
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
	
	@SuppressLint("UseValueOf") 
	@SuppressWarnings("deprecation")
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
	
	@Override
	public void onPause() {
	    super.onPause();
	    final WebView webview = (WebView)findViewById(R.id.webViewDetails);
	    //webview.stopLoading();
	    webview.onPause();
	}
	
	@Override
	public void onResume(){
	    super.onResume();
	    final WebView webview = (WebView)findViewById(R.id.webViewDetails);
	    webview.onResume();
	}
		
}