package com.jooketechnologies.jooke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jooketechnologies.adapter.PlayListArtistAdapter;
import com.jooketechnologies.adapter.PlayListArtistAdapter.ArtistItem;
import com.jooketechnologies.adapter.PlayListArtistAdapter.Row;
import com.jooketechnologies.adapter.PlayListArtistAdapter.Section;
import com.jooketechnologies.database.ArtistComparator;
import com.jooketechnologies.music.Artist;
import com.jooketechnologies.music.Song;
import com.jooketechnologies.style.FontHelper;

public class CreatePlaylistActivity extends ListActivity implements
		OnItemClickListener {
	Context mContext;
	public static final int SONG_SELECT = 2;
    private GestureDetector mGestureDetector;
	private List<Object[]> alphabet = new ArrayList<Object[]>();
	private HashMap<String, Integer> sections = new HashMap<String, Integer>();
	private int sideIndexHeight;
	private static float sideIndexX;
	private static float sideIndexY;
	private int indexListSize;
	private int current_num_songs = 0;
	private int selected_artist;
	public TextView artist;
	private ArrayList<Artist> artistList = new ArrayList<Artist>();
	private ArrayList<Song> selectedSongs = new ArrayList<Song>();
	List<Row> rows;
	PlayListArtistAdapter adapter;

	public void getSongs() {
		Log.e("get songs","get songs");
		String lastArtistName = null;
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

		String[] projection = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.ALBUM_ID };
		String sortOrder = MediaStore.Audio.Media.ARTIST + " ASC";
		Cursor cursor = managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				selection, null, sortOrder);
		
		Log.e("cursor size",cursor.getCount()+"");
		while (cursor.moveToNext()) {
			String artistStr = cursor.getString(1);
			String albumStr = cursor.getString(3);
			String titleStr = cursor.getString(2);
			String durationStr = cursor.getString(6);
			long album_id = cursor.getLong(7);
			if (lastArtistName == null || !lastArtistName.equals(artistStr)) {
				lastArtistName = artistStr;

				if (Utils.getAlbumBitmap(mContext, album_id) == null) {
					Bitmap default_cover = BitmapFactory.decodeResource(
							getResources(),
							R.drawable.default_cover);
					Artist artist = new Artist(artistStr, default_cover);
					Song song = new Song(albumStr, artistStr, titleStr,
							durationStr,null);
					artist.insertSong(song);
					artistList.add(artist);
				} else {
					Artist artist = new Artist(artistStr, Utils.getAlbumBitmap(
							mContext, album_id));
					Song song = new Song(albumStr, artistStr, titleStr,
							durationStr,null);
					artist.insertSong(song);
					artistList.add(artist);
				}

			} else {
				Song song = new Song(albumStr, artistStr, titleStr, durationStr,null);
				artistList.get(artistList.size() - 1).insertSong(song);
			}
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.menu_done:
			Log.e("menu","done");
			for(Song song:selectedSongs){
				Log.e("song",song.title);
			}
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.create_playlist_menu, menu);
		return true;
	}
	
	// always update the selected artist list when necessary.
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SONG_SELECT) {
			// Make sure the request was successful
			if (resultCode == Activity.RESULT_OK) {

				selected_artist = data.getIntExtra("position", -1);
				Bundle bundleObject = data.getExtras();

				ArrayList<Song> selected_songList = (ArrayList<Song>) bundleObject
						.getSerializable("selected_songs");
				
				
				ArrayList<Integer> selectedArtists = new ArrayList<Integer>();
				selectedArtists = Utils.getStoredArtistIndex(this);
				Log.e("selectedArtists before store",selectedArtists.size()+"");
				if (!selectedArtists.contains(selected_artist)) {
					// Normally you need to add an artist here. 
					// But since we allow back and forth checking. 
					// We allow a user unselect a singer as well. 
					// In that case, we need to remove the artist from the list. 
					
					if(selected_songList==null){
						selectedArtists.remove(Integer.valueOf(selected_artist));
						Log.e("remove singer!","remove singer!");
					}
					else{
						selectedArtists.add(Integer.valueOf(selected_artist));
						Log.e("new singer!","new singer!");
					}
					
					Utils.storeSelectedArtistIndex(this,
							selectedArtists);
					Log.e("selectedArtists after store",selectedArtists.size()+"");
					Log.e("getStoredArtistIndex",Utils.getStoredArtistIndex(this)+"");
					adapter.selectedArtists = selectedArtists;
					adapter.notifyDataSetChanged();
					
				}
				
				TextView number_of_songs = (TextView)findViewById(R.id.number_of_songs);
				// Set the current number of the selected songs. 
				// if should display what ever you stored in the shared preference. 
				
				if (Utils.getStoredSongList(this) != null){
					selectedSongs = Utils.getStoredSongList(this);
					Log.e("storedSong",selectedSongs.size()+"");
				}
				if(selected_songList!=null){
					for (Song selected_song : selected_songList) {
						selectedSongs.add(selected_song);
					}
				}
				
				Set<Song> s = new LinkedHashSet<Song>(selectedSongs);
				selectedSongs.clear();
				selectedSongs.addAll(s);
				for (Song song : selectedSongs) {
					Log.e("song", song.title);
				}
				Utils.storeSongList(this, selectedSongs);
				Log.e("after store",Utils.getStoredSongList(this).size()+"");
				number_of_songs.setText(selectedSongs.size() + "");
			}
		}
	}

	// Click event
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		if (adapter.getItemViewType(position) == 0) {
			Intent selectSongIntent = new Intent();
			selectSongIntent
					.setClass(this, ArrayAdapterActivity.class);

			selectSongIntent.putExtra("current_num_songs", current_num_songs);
			Log.e("current_num_songs", current_num_songs + "");
			ArtistItem artistItem = (ArtistItem) adapter.getItem(position);
			for (Artist artist : artistList) {
				if (artistItem.artistName.equals(artist.name)) {
					Bundle artistSongListBundle = new Bundle();
					
					if(artist.getSongList()==null){
						Log.e("fuck","delos");
					}
					else{
						Log.e("fuck","me");
					}
					
					artistSongListBundle.putSerializable("songs",
							artist.getSongList());

					// Put Bundle in to Intent and call start Activity
					selectSongIntent.putExtras(artistSongListBundle);
					break;
				}
			}
			selectSongIntent.putExtra("position", position);

			startActivityForResult(selectSongIntent, SONG_SELECT);
		}

	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            return false;
        }
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		setTheme(R.style.Theme_JookeBlue);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist);
		FontHelper.applyFont(this, findViewById(R.id.activity_create_playlist_root), "fonts/gillsans_light.ttf");
		Log.e("onCreate", "onCreate");
		mGestureDetector = new GestureDetector(this, new SideIndexGestureListener());
		adapter = new PlayListArtistAdapter(this);
		rows = new ArrayList<Row>();
		mContext = this;
		getSongs();

		Collections.sort(artistList, new ArtistComparator());
		rows.clear();

		int start = 0;
		int end = 0;
		String previousLetter = null;
		Object[] tmpIndexItem = null;
		Pattern numberPattern = Pattern.compile("[0-9]");

		for (Artist artist : artistList) {

			String artistName = artist.getNameOfArtist();
			int numberOfSongs = artist.getNumOfSongs();
			Bitmap bitmap = artist.getAlbumBitmap();
			String firstLetter = artistName.substring(0, 1);

			// Group numbers together in the scroller
			if (numberPattern.matcher(firstLetter).matches()) {
				firstLetter = "#";
			}

			// If we've changed to a new letter, add the previous letter to the
			// alphabet scroller
			if (previousLetter != null && !firstLetter.equals(previousLetter)) {
				end = rows.size() - 1;
				tmpIndexItem = new Object[3];
				tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
				tmpIndexItem[1] = start;
				tmpIndexItem[2] = end;
				alphabet.add(tmpIndexItem);

				start = end + 1;
			}

			// Check if we need to add a header row
			if (!firstLetter.equals(previousLetter)) {
				rows.add(new Section(firstLetter));
				sections.put(firstLetter, start);
			}

			// Add the country to the list
			rows.add(new ArtistItem(artistName, numberOfSongs, bitmap));
			previousLetter = firstLetter;
		}

		if (previousLetter != null) {
			// Save the last letter
			tmpIndexItem = new Object[3];
			tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
			tmpIndexItem[1] = start;
			tmpIndexItem[2] = rows.size() - 1;
			alphabet.add(tmpIndexItem);
		}

		adapter.setRows(rows);
		setListAdapter(adapter);

		updateList();
		adapter.notifyDataSetChanged();
	}

	class SideIndexGestureListener extends
			GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			sideIndexX = sideIndexX - distanceX;
			sideIndexY = sideIndexY - distanceY;

			if (sideIndexX >= 0 && sideIndexY >= 0) {
				displayListItem();
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	@SuppressLint("ResourceAsColor")
	public void updateList() {
		LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
		sideIndex.removeAllViews();
		indexListSize = alphabet.size();
		if (indexListSize < 1) {
			return;
		}

		int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
		int tmpIndexListSize = indexListSize;
		while (tmpIndexListSize > indexMaxSize) {
			tmpIndexListSize = tmpIndexListSize / 2;
		}
		double delta;
		if (tmpIndexListSize > 0) {
			delta = indexListSize / tmpIndexListSize;
		} else {
			delta = 1;
		}

		TextView tmpTV;
		for (double i = 1; i <= indexListSize; i = i + delta) {
			Object[] tmpIndexItem = alphabet.get((int) i - 1);
			String tmpLetter = tmpIndexItem[0].toString();

			tmpTV = new TextView(this);
			tmpTV.setText(tmpLetter);
			tmpTV.setGravity(Gravity.CENTER);
			tmpTV.setTextSize(15);
			tmpTV.setTextColor(getResources().getColor(R.color.jooke_gray));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			tmpTV.setLayoutParams(params);
			sideIndex.addView(tmpTV);
		}
		

		sideIndexHeight = sideIndex.getHeight();

		sideIndex.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// now you know coordinates of touch
				sideIndexX = event.getX();
				sideIndexY = event.getY();

				// and can display a proper item it country list
				displayListItem();

				return false;
			}
		});
	}

	public void displayListItem() {
		LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
		sideIndexHeight = sideIndex.getHeight();
		// compute number of pixels for every side index item
		double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

		// compute the item index for given event position belongs to
		int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

		// get the item (we can do it since we know item index)
		if (itemPosition < alphabet.size()) {
			Object[] indexItem = alphabet.get(itemPosition);
			int subitemPosition = sections.get(indexItem[0]);

			// ListView listView = (ListView) findViewById(android.R.id.list);
			getListView().setSelection(subitemPosition);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
