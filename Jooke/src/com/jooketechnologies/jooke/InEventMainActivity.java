package com.jooketechnologies.jooke;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jooketechnologies.adapter.LeaderBoardAdapter;
import com.jooketechnologies.network.ServerService;
import com.jooketechnologies.network.ServerUtilities;
import com.jooketechnologies.style.BaseSwipeListViewListener;
import com.jooketechnologies.style.SwipeListView;

public class InEventMainActivity extends ListActivity{

	ArrayList<HashMap<String, String>> songsList;
	SwipeListView swipelistview;
	LeaderBoardAdapter adapter;
	public TextView mLogo;
	public Context mContext;
	public ImageView clickMore;
	public String hostId;
	public String hostIp;
	public String eventId;
	public boolean allowVoting;
	public boolean allowAddSongs;
	public String eventName;
	public JookeApplication jookeApplication;
	class getHostIpTask extends AsyncTask<Void, Void, String> {

		protected String doInBackground(Void... none) {
			try {
				
				return ServerUtilities.getHostIp(hostId, eventId);
			} catch (Exception e) {
				Log.e("exception",e.toString());
				return null;
			}
		}
		protected void onPostExecute(String result){
			if(result!=null){
				//update the view here.
				//call populate list here. 
				hostIp = result;
				jookeApplication.startConnection(hostIp,mContext);
			}
			else{
				Log.e("failed to acquire host ip", "failed to acquire host ip");
			}
		}

	};


	class leaveEventTask extends AsyncTask<Void, Void, String> {

		protected String doInBackground(Void... none) {
			try {
				String host_id = SharedPreferenceUtils.getHostId(mContext);
				String event_id = SharedPreferenceUtils.getEventId(mContext);
				return ServerUtilities.leaveEvent(host_id, event_id);

			} catch (Exception e) {
				return null;
			}
		}

		protected void onPostExecute(String success) {
			Log.e("onPost","onPost");
			if (success.equals("true")) {
				Toast.makeText(mContext,
						"You have successfully leave the event!",
						Toast.LENGTH_LONG).show();
				SharedPreferenceUtils.clearEventStatus(mContext);
				finish();

			}
		}

	};

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		jookeApplication = (JookeApplication)getApplication();
		hostId=getIntent().getStringExtra(Constants.KEY_HOST_ID);
		// as a user who is already logged in. 
		// you need to get info from the SharedPreference. 
		if(hostId == null){
			hostId = SharedPreferenceUtils.getHostId(mContext);
			eventId = SharedPreferenceUtils.getEventId(mContext);
			allowVoting = SharedPreferenceUtils.getAllowVoting(mContext);
			allowAddSongs = SharedPreferenceUtils.getAddSongSetting(mContext);
			eventName = SharedPreferenceUtils.getEventName(mContext).toUpperCase();;
		}
		else{
			eventId=getIntent().getStringExtra(Constants.KEY_EVENT_ID);
			allowVoting=getIntent().getBooleanExtra(Constants.KEY_EVENT_MODE, false);
			allowAddSongs=getIntent().getBooleanExtra(Constants.KEY_ALLOW_ADDSONGS, false);
			eventName = getIntent().getStringExtra(Constants.KEY_EVENT_NAME).toUpperCase();
		}
		
		//If you are host
		if(SharedPreferenceUtils.getEventRole(mContext)){
			hostIp=Utils.getIPAddress(true);
			Intent startHostServerIntent = new Intent(mContext,ServerService.class );
			startService(startHostServerIntent);
			
		}
		//If you are public
		else{
			new getHostIpTask().execute();
		}
		
		
		setContentView(R.layout.fragment_leaderboard);
		clickMore = (ImageView) findViewById(R.id.click_more);
		clickMore.setOnClickListener((new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(mContext, v);
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {

						switch (item.getItemId()) {
						case R.id.view_people:
							Log.e("view people", "view people");
							Intent peopleListIntent = new Intent(mContext,PeopleListActivity.class);
							startActivity(peopleListIntent);
							return true;
						case R.id.playlist:
							Log.e("playlist", "playlist");

							return true;
						case R.id.add_songs:
							Log.e("add songs", "add songs");
							return true;
						case R.id.exit:
							// new leaveEventTask
							new leaveEventTask().execute();
							Log.e("exit", "exit");
							return true;
						}
						return false;
					
					}
				});

				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.in_event_drop_down, popup.getMenu());
				popup.show();
			}
		}));
		// Getting adapter by passing xml data ArrayList
		populateList();
		adapter = new LeaderBoardAdapter(this, songsList);
		setListAdapter(adapter);

		mLogo = (TextView) findViewById(R.id.logo);

		Typeface roboto = Typeface.createFromAsset(this.getAssets(),
				"fonts/gillsans_light.ttf");
		mLogo.setTypeface(roboto);
		mLogo.setText(eventName);
		swipelistview = (SwipeListView) findViewById(android.R.id.list);
		swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onOpened(int position, boolean toRight) {
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
			}

			@Override
			public void onListChanged() {
			}

			@Override
			public void onMove(int position, float x) {
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				Log.d("swipe", String.format("onStartOpen %d - action %d",
						position, action));
			}

			@Override
			public void onStartClose(int position, boolean right) {
				Log.d("swipe", String.format("onStartClose %d", position));
			}

			@Override
			public void onClickFrontView(int position) {
				Log.d("swipe", String.format("onClickFrontView %d", position));

				// swipelistview.openAnimate(position); //when you touch front
				// view it will open

			}

			@Override
			public void onClickBackView(int position) {
				Log.d("swipe", String.format("onClickBackView %d", position));

				swipelistview.closeAnimate(position);// when you touch back view
														// it will close
			}

			@Override
			public void onDismiss(int[] reverseSortedPositions) {

			}

		});

		swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT); // there are
																	// five
																	// swiping
																	// modes
		swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); // there
																				// are
																				// four
																				// swipe
																				// actions
		// swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
		swipelistview.setOffsetLeft(Utils.convertDpToPixel(mContext, 220f)); // left
																				// side
																				// offset
		swipelistview.setOffsetRight(Utils.convertDpToPixel(mContext, 0f)); // right
																			// side
																			// offset
		swipelistview.setAnimationTime(200); // Animation time
		swipelistview.setSwipeOpenOnLongPress(false); // enable or disable
														// SwipeOpenOnLongPress

	}

	public void populateList() {
		songsList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();

		// adding each child node to HashMap key => value
		map1.put(Constants.KEY_LEADERBOARD_TITLE, "The Celebrated Summer");
		map1.put(Constants.KEY_LEADERBOARD_ARTIST, "Memory Map");
		map1.put(Constants.KEY_LEADERBOARD_TITLE, "5:30");
		map1.put(Constants.KEY_LEADERBOARD_ALBUM_IMG, "memory_map");

		// adding HashList to ArrayList
		songsList.add(map1);
		HashMap<String, String> map2 = new HashMap<String, String>();
		// adding each child node to HashMap key => value
		map2.put(Constants.KEY_LEADERBOARD_TITLE, "In/Out");
		map2.put(Constants.KEY_LEADERBOARD_ARTIST, "Dan Croll");
		map2.put(Constants.KEY_LEADERBOARD_TITLE, "4:01");
		map2.put(Constants.KEY_LEADERBOARD_ALBUM_IMG, "rihanna");
		songsList.add(map2);
	}

}
