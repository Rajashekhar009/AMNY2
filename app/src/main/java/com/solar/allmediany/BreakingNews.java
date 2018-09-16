package com.solar.allmediany;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class BreakingNews extends Activity {
	TextView desc, headtitle;
	Typeface font1,font;
	Button close;	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popupwindowbusiness);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	    font1 = Typeface.createFromAsset(getAssets(), "georgia.ttf");
	   	  
	    headtitle=(TextView)findViewById(R.id.headtitle);
	    headtitle.setTypeface(font1);

	    desc=(TextView)findViewById(R.id.artdesc);
      	desc.setTypeface(font1);          	
      	
      	close=(Button)findViewById(R.id.close);
		close.setTypeface(font);
		close.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				finish();	
			}
			});
		
		if (CommonUtilities.notificationReceived) {
			onNotification();
		}
	}
	private void onNotification() {
		CommonUtilities.notificationReceived = false;
		//PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		//WakeLock  wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
		//wl.acquire();
		//String imagepath=CommonUtilities.Imagepath;
		//String url = "http://www.allmediany.com/bignails/"+imagepath;
		//String imagedownload = url.replaceAll(" ", "%20");
		desc.setText(stipHtml(CommonUtilities.Description));
		//title.setText(stipHtml(CommonUtilities.Title));
		//imageLoader.download(imagedownload,image);	
	}
	
	private String stipHtml(String description) {
		 return Html.fromHtml(description).toString();
	}

}