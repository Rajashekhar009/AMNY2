package com.solar.allmediany;

import static com.solar.allmediany.CommonUtilities.SENDER_ID;
//import static com.solar.allmediany.R.id.tabHost;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
//import com.google.android.gcm.GCMRegistrar;
//import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Main extends TabActivity  {

	private static final int NEW_MENU_ID1=1;
	private static final int NEW_MENU_ID2=2;
	private static final String MY_AD_UNIT_ID = "a14f0683775ab3e";
	GoogleAnalyticsTracker tracker;
	public String userId, regid="", window;
	TabHost tabHost;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		userId = PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.CUSTID,"");

		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession("UA-9695243-6", this);
		setContentView(R.layout.main);

		tabHost = getTabHost();

		/*final String regId = GCMRegistrar.getRegistrationId(Main.this);
		Log.d("REGIDVALUE", "registration id="+regId);
		PreferenceConnector.writeString(Main.this, PreferenceConnector.REGID,regId);
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);*/

		window=PreferenceConnector.readString(Main.this,PreferenceConnector.WINDOW, null);

		/*if(null==window||window.equals("1")||window.equals("")){

			if (regId.equals("")||null==regId||regId.equalsIgnoreCase(" ")) {

				PreferenceConnector.writeString(Main.this, PreferenceConnector.WINDOW,"3");
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main.this);
				alertDialog.setMessage("Do you want to receive the notification for Breaking News?");
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								RegisterwithGCM();
							}
							private void RegisterwithGCM() {
								GCMRegistrar.register(Main.this, SENDER_ID);
							}
						});
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int which) {
								dialog.cancel();
							}
						});
				alertDialog.show();
			}
			else {
				Log.d("REGIDVALUE", "Already registered");
			}
		}
		else{
			Log.d("Window","Window");
		}*/

		setTabs() ;

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				setTabColors(tabHost);
				return;
			}
		});

		tracker.trackPageView("com.solar.allmediany.Latestnews");
	}

	private void setTabs()
	{
		addTab("", R.drawable.latest_news_icon, Latestnews.class);
		addTab("", R.drawable.editor_choice_icon, Editorchoice.class);
		addTab("", R.drawable.featured_articles_icon, Featuredarticles.class);
		addTab("", R.drawable.top_stories_icon, TopStories.class);
		addTab("", R.drawable.sections_icon, Sections.class);

		if(userId.equalsIgnoreCase("")){
			addTab("", R.drawable.user_icon, UserLoginActivity.class);
		}
		else{
			addTab("", R.drawable.user_icon, MyAccountActivity.class);
		}

		tabHost.setCurrentTab(0);
		setTabColors(tabHost);
	}

	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this,c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tabindicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);

		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		regid=PreferenceConnector.readString(Main.this,PreferenceConnector.REGID, null);
		if(regid.equalsIgnoreCase("")||null==regid||regid.equalsIgnoreCase(" ")){
			regid="";
			menu.add(0, NEW_MENU_ID1, 0, "Enable Notification for Breaking News").setIcon(R.drawable.about_def);
		}else{
			menu.add(0, NEW_MENU_ID2, 0, "Disable Notification Messages").setIcon(R.drawable.about_def);
		}
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			/*case NEW_MENU_ID1:
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main.this);
				alertDialog.setMessage("Do you want to receive the notification for Breaking News?");
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								RegisterwithGCM();
							}
							private void RegisterwithGCM() {
								//	final String regId = GCMRegistrar.getRegistrationId(Latestnews.this);
								GCMRegistrar.register(Main.this, SENDER_ID);
								Intent intent = new Intent(Main.this, Main.class);
								startActivity(intent);
							}
						});
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int which) {
								dialog.cancel();
							}
						});
				alertDialog.show();
				return true;

			case NEW_MENU_ID2:
				AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(Main.this);
				alertDialog1.setMessage("Are you sure ? Do you want to disable the notification messages ?");
				alertDialog1.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								UnRegisterwithGCM();
							}

							private void UnRegisterwithGCM() {
								GCMRegistrar.unregister(Main.this);
								Intent intent = new Intent(Main.this, Main.class);
								//   onSaveInstanceState(bundle);
								startActivity(intent);
							}
						});
				alertDialog1.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int which) {
								dialog.cancel();
							}
						});
				alertDialog1.show();
				return true;*/

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void setTabColors(TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.titlegrr);
		}
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.titlegr);
	}
}