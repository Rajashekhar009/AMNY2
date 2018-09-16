package com.solar.allmediany;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HeaderActivity extends Activity {

	public static Typeface font;
	ProgressBar pb;
	ProgressDialog pd;
	public AlertDialog ad;
	public static ImageLoaderFile imgLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        font = Typeface.createFromAsset(getAssets(), "Trebuchet MS.ttf");
		imgLoader = new ImageLoaderFile(getApplicationContext());

		MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_home_footer));

		//Progress Dialogue
		pd = new ProgressDialog(HeaderActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
		pd.setCancelable(false);
		pd.setIcon(R.drawable.allmediany);
		pd.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+"'>"+getResources().getString(R.string.app_name)+"</font>"));



		ad = new AlertDialog.Builder(HeaderActivity.this, ProgressDialog.THEME_HOLO_LIGHT).create();
		ad.setIcon(R.drawable.allmediany);
		ad.setTitle(Html.fromHtml("<font color='"+getResources().getColor(R.color.tabDark)+getResources().getString(R.string.app_name)+"</font>"));
		ad.setMessage("");

    }

}