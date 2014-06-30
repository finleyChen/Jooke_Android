
package com.jooketechnologies.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jooketechnologies.event.Event;
import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.InEventMainActivity;
import com.jooketechnologies.jooke.JookeApplication;
import com.jooketechnologies.jooke.R;
import com.jooketechnologies.jooke.SharedPreferenceUtils;
import com.jooketechnologies.jooke.Utils;
import com.jooketechnologies.network.ImageLoader;
import com.jooketechnologies.user.MySelf;

public class DiscoverAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	public JookeApplication jookeApplication;
	
	public DiscoverAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
		jookeApplication = (JookeApplication) activity.getApplication();
		jookeApplication.mMe = new MySelf(Utils.getIPAddress(true),SharedPreferenceUtils.getUserId(a),SharedPreferenceUtils.getFullName(a));
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		final NewsHolder holder;
		if (vi == null) {
			vi = inflater.inflate(R.layout.discover_list_item, null);
			Typeface defaul=Typeface.createFromAsset(activity.getAssets(),
                    "fonts/gillsans_light.ttf");

			holder = new NewsHolder();

			holder.eventname = (TextView) vi.findViewById(R.id.event); // title
			holder.eventname.setTypeface(defaul);
			holder.hostname = (TextView) vi.findViewById(R.id.host); // artist
			holder.hostname.setTypeface(defaul);
																		// name
			holder.event_info = (Button) vi.findViewById(R.id.event_info_btn);
			holder.event_info.setTypeface(defaul);
			holder.event_info.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	               //
	            }
	        });
			holder.join_event = (Button) vi.findViewById(R.id.join_event_info);
			holder.join_event.setTypeface(defaul);
			holder.join_event.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                
	                Intent inEventMainIntent = new Intent(activity,InEventMainActivity.class);
				    inEventMainIntent.putExtra(Constants.KEY_EVENT_NAME, holder.eventname.getText().toString());
				    inEventMainIntent.putExtra(Constants.KEY_EVENT_ID, holder.event_id);
				    inEventMainIntent.putExtra(Constants.KEY_EVENT_MODE, holder.event_mode);
				    inEventMainIntent.putExtra(Constants.KEY_ALLOW_ADDSONGS, holder.allow_addsongs);
				    inEventMainIntent.putExtra(Constants.KEY_HOST_ID, holder.host_id);
				    inEventMainIntent.putExtra(Constants.KEY_IS_HOST,false);
				    
				 
				    Event newEvent = new Event(holder.eventname.getText().toString(), holder.host_id, 
				    		holder.hostname.getText().toString(),holder.host_image_url , 
							holder.event_id, String.valueOf(holder.event_mode), String.valueOf(holder.allow_addsongs));
				    SharedPreferenceUtils.storeEventStatus(activity, holder.event_id,holder.host_id,holder.hostname.getText().toString(),
				    		
				    		holder.eventname.getText().toString(), false,holder.event_mode, holder.allow_addsongs);
					jookeApplication.mMe.isHost = false;
					jookeApplication.mMe.setEvent(newEvent);
				    activity.startActivity(inEventMainIntent);
					activity.finish();
	            }
	        });
		holder.hostimage = (ImageView) vi.findViewById(R.id.host_image);
			

			vi.setTag(holder);
		} else {
			holder = (NewsHolder) vi.getTag();
		}

		HashMap<String, String> event = new HashMap<String, String>();
		event = data.get(position);

		// Setting all values in listview
		holder.eventname.setText(event.get(Constants.KEY_DISCOVER_EVENT_NAME));
		holder.hostname.setText(event.get(Constants.KEY_DISCOVER_HOST_NAME));
		holder.event_id = event.get(Constants.KEY_EVENT_ID);
		holder.event_mode = Boolean.valueOf(event.get(Constants.KEY_EVENT_MODE));
		holder.allow_addsongs = Boolean.valueOf(event.get(Constants.KEY_ALLOW_ADDSONGS));
		holder.host_id = event.get(Constants.KEY_HOST_ID);
		holder.host_image_url = event.get(Constants.KEY_HOME_SUBJECT1_PROFILE_IMG);	
		imageLoader.DisplayImage(event.get(Constants.KEY_HOME_SUBJECT1_PROFILE_IMG),holder.hostimage,true);
		
		return vi;

	}

	static class NewsHolder {

		TextView eventname;
		TextView hostname;
		String hostFullname;
		ImageView hostimage;
		String host_image_url;
		Button event_info;
		Button join_event;
		String event_id;
		boolean allow_addsongs;
		boolean event_mode;
		String host_id;
	}
}