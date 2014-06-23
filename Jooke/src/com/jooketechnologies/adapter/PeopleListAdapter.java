package com.jooketechnologies.adapter;


import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.R;
import com.jooketechnologies.network.ImageLoader;
import com.jooketechnologies.style.RoundedAvatarDrawable;




@SuppressLint("ResourceAsColor")
public class PeopleListAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;


	public PeopleListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
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
			vi = inflater.inflate(R.layout.item_people, null);

		final TextView subject1 = (TextView) vi.findViewById(R.id.user);
		subject1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.e("click",subject1.getText().toString());
			}
		});
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image);
		Typeface defaul=Typeface.createFromAsset(activity.getAssets(),
                "fonts/gillsans_light.ttf");								

		HashMap<String, String> people = new HashMap<String, String>();
		people = data.get(position);
		subject1.setText(people.get(Constants.KEY_HOME_FEED_SUBJECT1));
		subject1.setTypeface(defaul);
		imageLoader.DisplayImage(people.get(Constants.KEY_HOME_SUBJECT1_PROFILE_IMG),thumb_image,true);

		return vi;
	}
}