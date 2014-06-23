package com.jooketechnologies.jooke;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jooketechnologies.adapter.MyArrayAdapter;
import com.jooketechnologies.music.Song;

public class ArrayAdapterActivity extends ActionBarActivity implements
		OnItemClickListener {
	private MyArrayAdapter adapter;
	private int clicked_position;
	private ArrayList<Song> songList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_songs);
		Intent receivedIntent = getIntent();
		getActionBar().setTitle(receivedIntent.getStringExtra("EventName"));
		clicked_position = receivedIntent.getIntExtra("position", 0);
		Bundle bundleObject = receivedIntent.getExtras();
		songList = (ArrayList<Song>) bundleObject.getSerializable("songs");
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		rebuildList(savedInstanceState);
	}

	private ListView getListView() {
		return (ListView) findViewById(android.R.id.list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();

		if (adapter.getCheckedItemCount() != 0) {
			returnIntent.putExtra("position", clicked_position);
			
			Bundle artistSongListBundle = new Bundle();
			ArrayList<Song> tempSongList = new ArrayList<Song>();
			for (Long position : adapter.getCheckedItems()) {
				tempSongList.add(songList.get(position.intValue()));
			}
			artistSongListBundle
					.putSerializable("selected_songs", tempSongList);
			returnIntent.putExtras(artistSongListBundle);
		} 

		setResult(Activity.RESULT_OK, returnIntent);
		finish();
		super.onBackPressed(); // optional depending on your needs
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent returnIntent = new Intent();
			
			returnIntent.putExtra("position", clicked_position);
			if (adapter.getCheckedItemCount() != 0) {

				Bundle artistSongListBundle = new Bundle();

				ArrayList<Song> tempSongList = new ArrayList<Song>();
				for (Long position : adapter.getCheckedItems()) {
					tempSongList.add(songList.get(position.intValue()));
				}
				artistSongListBundle.putSerializable("selected_songs",
						tempSongList);
				returnIntent.putExtras(artistSongListBundle);
			} 

			setResult(Activity.RESULT_OK, returnIntent);
			finish();
			return true;
		case R.id.menu_select_all:
			selectAll();
			return true;
		case R.id.menu_deselect_all:
			deselectAll();
			return true;
		}
		return false;
	}

	private String[] getMusic() {
		final Cursor mCursor = managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.DISPLAY_NAME }, null,
				null, "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

		int count = mCursor.getCount();

		String[] songs = new String[count];
		int i = 0;
		if (mCursor.moveToFirst()) {
			do {
				songs[i] = mCursor.getString(0);
				Log.e("aa", songs[i]);
				i++;
			} while (mCursor.moveToNext());
		}

		mCursor.close();

		return songs;
	}

	private void selectAll() {
		for (int i = 0; i < adapter.getCount(); ++i) {
			adapter.setItemChecked(i, true);

			getMusic();
		}
	}

	public void deselectAll() {
		for (int i = 0; i < adapter.getCount(); ++i) {
			adapter.setItemChecked(i, false);
		}
	}

	private void rebuildList(Bundle savedInstanceState) {

		ArrayList<String> songTitles = new ArrayList<String>();
		for (Song song : songList) {
			songTitles.add(song.title);
		}
		adapter = new MyArrayAdapter(savedInstanceState, this, songTitles);
		if (adapter == null) {
			Log.e("null", "null");
		} else {
			Log.e("not null", "not null");
		}
		adapter.setOnItemClickListener(this);
		adapter.setAdapterView(getListView());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		adapter.save(outState);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
