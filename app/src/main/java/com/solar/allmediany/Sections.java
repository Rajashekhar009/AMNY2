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

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static com.solar.allmediany.HeaderActivity.font;
import static com.solar.allmediany.HeaderActivity.imgLoader;
import static com.solar.allmediany.R.drawable.load;
import static com.solar.allmediany.Sections.CAT_ID;
import static com.solar.allmediany.Sections.CAT_LOGO;
import static com.solar.allmediany.Sections.CAT_NAME;
import static com.solar.allmediany.Sections.CAT_TYPE;

public class Sections extends HeaderActivity {
	
	ArrayList<HashMap<String, String>> servicelist = new ArrayList<HashMap<String, String>>();
	
    final HashMap<String,String> map1 = new HashMap <String, String>();
	ProgressBar progressBar;
	static final String CAT_ID   = "CatID";
	static final String CAT_NAME = "Name";
	static final String CAT_TYPE = "Type";
	static final String CAT_LOGO = "Path";
	public JSONObject jo;
	
	ArrayList<HashMap<String, String>> tempdata 	= new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> storydata	= new ArrayList<HashMap<String, String>>();

	String titlename;
	RelativeLayout loadview,listlayout;
	TextView id, title, desc, date;
	ListView lv;
	SectionsAdapterClass adapter;
	ImageView refresh;
	private AdView mAdView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sectionslistview); 
        TextView title1=(TextView)findViewById(R.id.titlename);
        title1.setTypeface(font);

		lv = (ListView)findViewById(R.id.list);

		//MobileAds.initialize(this, "ca-app-pub-1245050593606452~8420268127");
		MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_home_footer));
		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
                
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
            	 loadview=(RelativeLayout)findViewById(R.id.layoutloading);
            	 listlayout=(RelativeLayout)findViewById(R.id.listlayout);
            	 loadview.setVisibility(View.VISIBLE);
            	 listlayout.setVisibility(View.GONE);
            	 progressBar.setVisibility(View.VISIBLE);
              }  
              @Override  
              protected Void doInBackground(Void... args) {
            	  try {
                    	String URL = "http://allmediany.com/service/allmediany-service.svc/getAllCategories/format=json/";
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
        	       			
							map1.put(CAT_ID,"11");
							map1.put(CAT_NAME,"Breaking News");
							map1.put(CAT_TYPE,"BreakingNews");
							map1.put(CAT_LOGO,"http://allmediany.com/logos/breakingnews.png");

							tempdata.add(map1);

							for (int i = 0; i < n; i++) {

								JSONObject c 	= ja.getJSONObject(i);
								jo 				= c.getJSONObject("AllCategories");

								HashMap<String, String> map = new HashMap<String, String>();

								map.put(CAT_ID, jo.getString("CatID"));
								map.put(CAT_NAME, jo.getString("Name"));
								map.put(CAT_TYPE, jo.getString("Type"));
								map.put(CAT_LOGO, jo.getString("Path"));

								tempdata.add(map);
								storydata=tempdata;
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
		  	@Override
			protected void onPostExecute(Void result) {
					progressBar.setVisibility(View.GONE);
					loadview.setVisibility(View.GONE);
					listlayout.setVisibility(View.VISIBLE);
					adapter = new SectionsAdapterClass(getApplicationContext(), storydata);
					lv.setAdapter(adapter);
					lv.setTextFilterEnabled(true);
					lv.setOnItemClickListener(new OnItemClickListener() {

					@SuppressWarnings("unused")
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

						TextView idget =(TextView)view.findViewById(R.id.catid);
						String idvalue = idget.getText().toString();
						TextView path =(TextView)view.findViewById(R.id.catname);
						String pathvalue = path.getText().toString();

						TextView title =(TextView)view.findViewById(R.id.title);
						String titlename = title.getText().toString();

						ImageView img=(ImageView)view.findViewById(R.id.imageview1);

						Log.d("CatValues",": ID: "+idvalue+"pathvalue: "+pathvalue+" titlename: "+titlename);

						if(pathvalue.equals("BreakingNews")){
							Intent in = new Intent(getApplicationContext(), BreakingNewsCategory.class);
							in.putExtra("titlename", "Breaking News");
							startActivity(in);
						}
						else if(pathvalue.equals("News")){
							String serviceurl="http://allmediany.com/service/allmediany-service.svc/getNewsService/format=json/NewsCatID="+idvalue+"";
							//String serviceurl="http://allmediany.com/service/allmediany-service.svc/getNewsList/format=xml/NewsCatID="+idvalue+"";
							Intent in = new Intent(getApplicationContext(), CategoryList.class);
							in.putExtra("id", idvalue);
							in.putExtra("url", serviceurl);
							in.putExtra("ttl", titlename);
							in.putExtra("cattype", "NEWS");
							startActivity(in);
						}
						else{
							//String serviceurl="http://allmediany.com/service/allmediany-service.svc/getArticleList/format=xml/ArticleCatID="+idvalue+"";
							String serviceurl="http://allmediany.com/service/allmediany-service.svc/getArticleService/format=json/ArticleCatID="+idvalue+"";
							Log.d("articlepath",""+serviceurl);
							//Intent in = new Intent(getApplicationContext(), CatArticleCategory.class);
							Intent in = new Intent(getApplicationContext(), CategoryList.class);
							in.putExtra("id", idvalue);
							in.putExtra("url", serviceurl);
							in.putExtra("ttl", titlename);
							in.putExtra("cattype", "ARTICLES");
							startActivity(in);
						}
					}
				   });
		   	}
		}
}

class SectionsAdapterClass extends BaseAdapter {

	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	//public ImageLoader imageLoader;

	public SectionsAdapterClass(Context context, ArrayList<HashMap<String, String>> mylist) {
		this.data=mylist;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//imageLoader=new ImageLoader();
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
			vi = inflater.inflate(R.layout.sections, null);

		TextView title = (TextView)vi.findViewById(R.id.title);
		title.setTypeface(font);
		TextView catid = (TextView)vi.findViewById(R.id.catid);
		TextView catname = (TextView)vi.findViewById(R.id.catname);
		ImageView image=(ImageView)vi.findViewById(R.id.imageview1);

		HashMap<String, String> sections = new HashMap<String, String>();
		sections = data.get(position);

		title.setText(sections.get(CAT_NAME));
		catid.setText(sections.get(CAT_ID));
		catname.setText(sections.get(CAT_TYPE));
		String url=sections.get(CAT_LOGO);
		if(!url.isEmpty()){
			String imageurl = url.replaceAll(" ", "%20");
			//imageLoader.download(imageurl,image);
            imgLoader.DisplayImage(imageurl, image);
		}

		return vi;
	}

}