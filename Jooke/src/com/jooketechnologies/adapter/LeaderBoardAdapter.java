
package com.jooketechnologies.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.R;
import com.jooketechnologies.network.ImageLoader;
import com.jooketechnologies.style.RoundedAvatarDrawable;

public class LeaderBoardAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public LeaderBoardAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
		NewsHolder holder = null;
		holder = null;
		if (vi == null) {
			vi = inflater.inflate(R.layout.leaderboard_list_item, null);

			holder = new NewsHolder();

			holder.title = (TextView) vi.findViewById(R.id.title); // title
			holder.artist = (TextView) vi.findViewById(R.id.artist); // artist
			holder.rank = (TextView) vi.findViewById(R.id.rank);															// name
			holder.button1 = (Button) vi.findViewById(R.id.swipe_button1);
			holder.button2 = (Button) vi.findViewById(R.id.swipe_button2);
			holder.thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb
																				// image

			vi.setTag(holder);
		} else {
			holder = (NewsHolder) vi.getTag();
		}

		HashMap<String, String> song = new HashMap<String, String>();
		song = data.get(position);

		// Setting all values in listview
		holder.title.setText(song.get(Constants.KEY_LEADERBOARD_TITLE));
		holder.artist.setText(song.get(Constants.KEY_LEADERBOARD_ARTIST));
		holder.rank.setText(position+1+"");
		String uri = "drawable/"
				+ song.get(Constants.KEY_LEADERBOARD_ALBUM_IMG);

		int imageResource = activity.getResources().getIdentifier(uri, null,
				activity.getPackageName());
		Log.e("",uri);

		BitmapDrawable bitmapDrawable = (BitmapDrawable)activity.getResources().getDrawable(imageResource);
		holder.thumb_image.setImageDrawable(new RoundedAvatarDrawable(bitmapDrawable.getBitmap()));
		

		return vi;

	}

	static class NewsHolder {

		TextView title;
		TextView artist;
		TextView rank;
		ImageView thumb_image;
		Button button1;
		Button button2;
	}
}