package com.jooketechnologies.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jooketechnologies.jooke.R;
import com.jooketechnologies.style.RoundedAvatarDrawable;



public class PlayListArtistAdapter extends BaseAdapter {
	public int selected_position = -1;
	private Activity activity;
	public ArrayList<Integer> selectedArtists = new ArrayList<Integer>();

	public static abstract class Row {
	}
	
	

	public PlayListArtistAdapter(Activity a) {
		activity = a;
	}


	public static final class Section extends Row {
		public final String text;

		public Section(String text) {
			this.text = text;
		}
	}

	public static final class ArtistItem extends Row {
		public final String artistName;
		public final int numberOfSongs;
		public final Bitmap albumBitmap;

		public ArtistItem(String artistString, int numberOfSongsInteger,
				Bitmap bitmap) {
			artistName = artistString;
			numberOfSongs = numberOfSongsInteger;
			albumBitmap = bitmap;
		}
	}

	private List<Row> rows;

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	@Override
	public int getCount() {
		return rows.size();
	}

	@Override
	public Row getItem(int position) {
		return rows.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (getItem(position) instanceof Section) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;

		if (getItemViewType(position) == 0) { // Item
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = (LinearLayout) inflater.inflate(
						R.layout.item_playlist_artist, parent, false);
			}

			ArtistItem item = (ArtistItem) getItem(position);
			TextView artistName = (TextView) view.findViewById(R.id.artistName);
			artistName.setTextColor(activity.getResources().getColor(R.color.jooke_orange));
			artistName.setText(item.artistName);
			
			TextView numberOfSongs = (TextView) view
					.findViewById(R.id.number_songs);
			numberOfSongs.setText(item.numberOfSongs + "");
			ImageView imageView = (ImageView) view
					.findViewById(R.id.list_image);
			imageView.setImageDrawable(new RoundedAvatarDrawable(
					item.albumBitmap));
			ImageView checkBox = (ImageView) view
					.findViewById(R.id.checked_img);
			if (selectedArtists.contains(position)) {
				checkBox.setVisibility(View.VISIBLE);
			} else {
				checkBox.setVisibility(View.GONE);
			}
		} else { // Section
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = (LinearLayout) inflater.inflate(
						R.layout.header_artist_first_letter, parent, false);
			}

			Section section = (Section) getItem(position);
			TextView textView = (TextView) view.findViewById(R.id.textView1);
			textView.setText(section.text);
		}

		return view;

	}

}
