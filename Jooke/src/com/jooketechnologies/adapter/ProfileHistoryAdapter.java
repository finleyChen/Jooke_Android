package com.jooketechnologies.adapter;


import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.JookeApplication;
import com.jooketechnologies.jooke.R;
import com.jooketechnologies.network.ImageLoader;




@SuppressLint("ResourceAsColor")
public class ProfileHistoryAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	
	public JookeApplication jookeApplication;
	public boolean isInEvent;

	public ProfileHistoryAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		jookeApplication= (JookeApplication) activity.getApplication();
		isInEvent= jookeApplication.isInEvent;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
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
		if (convertView == null)
			vi = inflater.inflate(R.layout.item_feed, null);

		TextView action = (TextView) vi.findViewById(R.id.action);
		final TextView subject1 = (TextView) vi.findViewById(R.id.subject1);
		if (isInEvent)
		{
			subject1.setTextColor(activity.getResources().getColor(R.color.jooke_orange));
		}
		else
		{
			subject1.setTextColor(activity.getResources().getColor(R.color.jooke_blue));
		}
		
		TextView subject2 = (TextView) vi.findViewById(R.id.subject2);
		subject1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.e("click",subject1.getText().toString());
			}
		});
		TextView duration = (TextView) vi
				.findViewById(R.id.feed_clock_duration); 
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); 

		HashMap<String, String> feed = new HashMap<String, String>();
		feed = data.get(position);
		action.setText(feed.get(Constants.KEY_HOME_FEED_ACTION));
		subject1.setText(feed.get(Constants.KEY_HOME_FEED_SUBJECT1));
		subject2.setText(feed.get(Constants.KEY_HOME_FEED_SUBJECT2));
		duration.setText(feed.get(Constants.KEY_HOME_PASS_TIME));

		imageLoader.DisplayImage(feed.get(Constants.KEY_HOME_SUBJECT1_PROFILE_IMG),thumb_image,true);
		return vi;
	}
}