package com.solar.allmediany;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.solar.allmediany.HeaderActivity.imgLoader;

public class Sectionsadapter extends BaseAdapter {
	 	
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	//public ImageLoader imageLoader;
	Typeface font,font1;
	    
	public Sectionsadapter(Context context,ArrayList<HashMap<String, String>> mylist) {	
		this.data=mylist;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader();
        font = Typeface.createFromAsset(context.getAssets(), "ufonts.com_arial-black-2.ttf");  
        font1 = Typeface.createFromAsset(context.getAssets(), "georgia.ttf");  
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
	        title.setTypeface(font1);
	        TextView catid = (TextView)vi.findViewById(R.id.catid); 
	        TextView catname = (TextView)vi.findViewById(R.id.catname);
	        ImageView image=(ImageView)vi.findViewById(R.id.imageview1);
	 
	        HashMap<String, String> sections = new HashMap<String, String>();
	        sections = data.get(position);	 
	     
	        String newsname=sections.get(Sections.CAT_NAME);
	        title.setText(newsname);
	        catid.setText(sections.get(Sections.CAT_ID));
	        catname.setText(sections.get(Sections.CAT_TYPE));	        
	        
	        String url=sections.get(Sections.CAT_LOGO);
	        if(url.isEmpty())
	        {	        
	        }
	        else
	        {
	        	String imageurl = url.replaceAll(" ", "%20");	        
		     	//imageLoader.download(imageurl,image);
				imgLoader.DisplayImage(imageurl, image);
	        }      
	 
			return vi;	  
	}

}