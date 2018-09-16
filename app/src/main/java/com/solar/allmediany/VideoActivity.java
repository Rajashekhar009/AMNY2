package com.solar.allmediany;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class VideoActivity extends Activity {

	WebView webView1;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_video);
		
		webView1 = (WebView) findViewById(R.id.webView1);
		
		Intent i		= getIntent();        	        
        String videoCat = i.getStringExtra("videoCat");
        String vidID	= i.getStringExtra("vidID");   
		webView1 = (WebView) findViewById(R.id.webView1);		
		webView1.getSettings().setJavaScriptEnabled(true);
		//Log.d("AMNY",""+"http://www.allmediany.com/app/views/playvideo.php?txtWidth=&txtHeight=&vidType="+videoCat+"&vidId="+vidID);
		//int vidType  = 2 ;// 1 for News, 2 for Articles	
		//webView1.loadUrl("http://www.allmediany.com/app/views/playvideo.php?txtWidth=&txtHeight=&vidType="+videoCat+"&vidId="+vidID);






		webView1.loadUrl("file:///android_asset/rxhome.html");

		webView1.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				ViewTreeObserver viewTreeObserver  = webView1.getViewTreeObserver();
				viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						int height = webView1.getMeasuredHeight();
						int width = webView1.getMeasuredWidth();
						if( height != 0 ){
							webView1.getViewTreeObserver().removeOnPreDrawListener(this);
						}
						return false;
					}
				});
			}
		});
	}
	
	@Override
    public void onBackPressed() {    	
    	 
    	 final WebView webview = (WebView)findViewById(R.id.webView1);
    	 webview.stopLoading();
    	 webview.loadData("", "text/html", "utf-8");
    	 
    	 this.finish();
    }
	
}