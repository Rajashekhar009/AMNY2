package com.solar.allmediany;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.solar.allmediany.CommonUtilities.stipHtml;
import static com.solar.allmediany.HeaderActivity.font;
import static com.solar.allmediany.HeaderActivity.imgLoader;
import static com.solar.allmediany.R.id.titlename;

public class CategoryList extends HeaderActivity {

	ProgressBar progressBar;
	public static final String KEY_ID = "ID";
	public static final String KEY_TITLE = "Title";
	public static final String KEY_DESC = "Description";
	public static final String KEY_IMAGE = "Image";
	public static final String KEY_DATE = "PostedDate";
	public static final String KEY_VIDEOURL = "VideoUrl";
	public JSONObject jo;
	ArrayList<HashMap<String, String>> tempdata = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> storydata = new ArrayList<HashMap<String, String>>();
	TextView id, title, date;
	String titlename="", cattype = "", idvalue = "", URL = "";
	ListView lv;
	CatListAdapter adapter;
	ImageView topLeftBtn, refresh;
	RelativeLayout loadview, listlayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categorylist);

		lv = (ListView) findViewById(R.id.list);

		TextView title1 = (TextView) findViewById(R.id.titlename);
		title1.setTypeface(font);

		refresh = (ImageView) findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new MyTask().execute();
			}
		});

		topLeftBtn = (ImageView) findViewById(R.id.topLeftBtn);
		topLeftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Intent i = getIntent();
		idvalue = i.getStringExtra("id");
		titlename = i.getStringExtra("ttl");
		URL = i.getStringExtra("url");
		cattype = i.getStringExtra("cattype");

		title1.setText(titlename);

		//URL = "http://allmediany.com/service/allmediany-service.svc/getNewsService/format=json/NewsCatID="+idvalue+"/page=0/";
		Log.d("URL", URL);
		new MyTask().execute();
	}

	private class MyTask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			progressBar = (ProgressBar) findViewById(R.id.progressBar1);
			loadview = (RelativeLayout) findViewById(R.id.layoutloading);
			listlayout = (RelativeLayout) findViewById(R.id.listlayout);
			loadview.setVisibility(View.VISIBLE);
			listlayout.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... args) {
			try {
				int n = 0;
				HttpClient httpClient = new DefaultHttpClient();
				Log.d("URL", URL);
				HttpGet httpRequest = new HttpGet(URL);
				HttpResponse response = httpClient.execute(httpRequest);
				String result = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(result);
				JSONArray ja = json.getJSONArray("DataTable");
				n = ja.length();

				for (int i = 0; i < n; i++) {

					JSONObject c = ja.getJSONObject(i);
					jo = c.getJSONObject("List");

					String vidUrlCntnt = "FALSE";

					HashMap<String, String> map = new HashMap<String, String>();

					map.put(KEY_ID, jo.getString(KEY_ID));
					map.put(KEY_TITLE, CommonUtilities.stipHtml(jo.getString(KEY_TITLE)));

					String imgUrl = "";
					if (!jo.getString(KEY_IMAGE).equals("") && jo.getString(KEY_IMAGE) != null) {
						imgUrl = jo.getString(KEY_IMAGE).replaceAll(" ", "%20");
					}

					map.put(KEY_IMAGE, imgUrl);
					map.put(KEY_DESC, CommonUtilities.stipHtml(jo.getString(KEY_DESC)));
					map.put(KEY_DATE, jo.getString(KEY_DATE));
					if (jo.getString(KEY_VIDEOURL).length() >= 10) {
						vidUrlCntnt = "TRUE";
					}
					map.put(KEY_VIDEOURL, vidUrlCntnt);
					tempdata.add(map);
					storydata = tempdata;
					Log.d("AMNYTRACE", "storydata:" + storydata);
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

			adapter = new CatListAdapter(getApplicationContext(), storydata);
			lv.setAdapter(adapter);
			lv.setTextFilterEnabled(true);

		}
	}


	public class CatListAdapter extends BaseAdapter {

		public ArrayList<HashMap<String, String>> data;
		public LayoutInflater inflater = null;

		public CatListAdapter(Context context, ArrayList<HashMap<String, String>> mylist) {
			this.data = mylist;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.latestnews, null);

			LinearLayout mainlayout = (LinearLayout) vi.findViewById(R.id.mainlayout);

			TextView title = (TextView) vi.findViewById(R.id.title);
			TextView desc = (TextView) vi.findViewById(R.id.desc);
			ImageView image = (ImageView) vi.findViewById(R.id.imageview1);

			RelativeLayout rlAd = (RelativeLayout) vi.findViewById(R.id.rlAd);
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

			title.setTypeface(font);
			desc.setTypeface(font);

			HashMap<String, String> news = new HashMap<String, String>();
			news = data.get(position);

			title.setText(news.get(stipHtml(CategoryList.KEY_TITLE)));
			desc.setText(news.get(stipHtml(CategoryList.KEY_DESC)));
			String imageurl = news.get(CategoryList.KEY_IMAGE).replaceAll(" ", "%20");
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
			HashMap<String, String> newsData = new HashMap<String, String>();
			newsData = storydata.get(position);

			String date1=newsData.get(KEY_DATE);
			String date2=date1.replaceAll("T"," " );
			String date3=date2.replaceAll("-07:00","" );

			Intent in = new Intent(getApplicationContext(), Details.class);
			in.putExtra("From", "Local");
			in.putExtra("catType", titlename);
			in.putExtra("name", newsData.get(stipHtml(KEY_TITLE)));
			in.putExtra("description", newsData.get(stipHtml(KEY_DESC)));
			in.putExtra("date", date3);
			in.putExtra("imageurl", newsData.get(KEY_IMAGE));
			in.putExtra("videoStatus", newsData.get(KEY_VIDEOURL));
			in.putExtra("vidID", newsData.get(KEY_ID));

			if(cattype.equalsIgnoreCase("NEWS")){
				in.putExtra("videoCat", "2");
			}
			else{
				in.putExtra("videoCat", "1");
			}
			startActivity(in);
		}
	}
}