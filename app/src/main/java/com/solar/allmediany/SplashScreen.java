package com.solar.allmediany;
import com.solar.allmediany.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import static com.solar.allmediany.R.id.idvalue;
import static com.solar.allmediany.R.id.titlename;

public class SplashScreen extends Activity {
		
		protected boolean _active = true;
		protected int _splashTime = 3000;
		 
		@Override
		public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		LinearLayout l1=new LinearLayout(this);
		 
		//ImageView image = (ImageView) findViewById(R.id.imageView1);
		//Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.anim);
//		image.startAnimation(hyperspaceJump);
		l1.setOrientation(android.widget.LinearLayout.VERTICAL);
		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
		@Override
		public void run() {
			try {
				int waited = 0;
				while(_active && (waited < _splashTime)) {
					sleep(100);
					if(_active) {
						waited += 100;
					}
				}
				} catch(InterruptedException e) {
					Log.e("InterruptedException", "Error: ", e);
				} finally {
					finish();
					//Intent i=new Intent(SplashScreen.this, Main.class);
					Intent i=new Intent(SplashScreen.this, VideoActivity.class);
					i.putExtra("videoCat", "videoCat");
					i.putExtra("videoCat", "vidID");
					startActivity(i);
				}	
			}
		};
		splashTread.start();
		}		 
		@Override
		public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
		_active = false;
		}
		return true;
		}
}