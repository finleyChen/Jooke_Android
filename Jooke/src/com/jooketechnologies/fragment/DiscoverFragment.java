
package com.jooketechnologies.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jooketechnologies.adapter.DiscoverAdapter;
import com.jooketechnologies.event.Event;
import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.JookeApplication;
import com.jooketechnologies.jooke.R;
import com.jooketechnologies.jooke.SharedPreferenceUtils;
import com.jooketechnologies.network.ServerUtilities;
import com.jooketechnologies.style.BaseSwipeListViewListener;
import com.jooketechnologies.style.SwipeListView;

public class DiscoverFragment extends ListFragment {

	ArrayList<HashMap<String, String>> feedsList;
	ListView list;
	DiscoverAdapter adapter;
	SwipeListView swipelistview;
	Context mContext;
	Context mContextThemeWrapper;
	JookeApplication jookeApplication;
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_search:
			// search action
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}	

	public void populateList(ArrayList<Event> eventList) {
		feedsList = new ArrayList<HashMap<String, String>>();
		
		for (Event event:eventList){
			HashMap<String, String> map = new HashMap<String, String>();

			// adding each child node to HashMap key => value
			map.put(Constants.KEY_DISCOVER_EVENT_NAME, event.eventName);
			map.put(Constants.KEY_DISCOVER_HOST_NAME, event.hostName);
			map.put(Constants.KEY_HOME_SUBJECT1_PROFILE_IMG, event.hostProfileImgUrl);
			map.put(Constants.KEY_ALLOW_ADDSONGS, event.allowAddSongs);
			map.put(Constants.KEY_EVENT_MODE, event.eventMode);
			map.put(Constants.KEY_HOST_ID, event.hostId);
			map.put(Constants.KEY_EVENT_ID, event.eventId);
			
			// adding HashList to ArrayList
			feedsList.add(map);
		}

		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e("onCreate","onCreate");
		
		jookeApplication=(JookeApplication)getActivity().getApplication();
	    
		mContext = getActivity();
		super.onCreate(savedInstanceState);
		// Getting adapter by passing xml data ArrayList
		//populateList();
		new discoverNearbyEventTask().execute();
		
		setHasOptionsMenu(true);
		setMenuVisibility(true); 
		
	}
	class discoverNearbyEventTask extends AsyncTask<Void, Void, ArrayList<Event>> {

		protected ArrayList<Event> doInBackground(Void... none) {
			try {
				//CHECK
				
				String userId = SharedPreferenceUtils.getStoredJookeUserId(mContext);
				Log.e("userId",userId);
				if(jookeApplication.currentLocation==null){
					Log.e("null","location is null");
				}
				return ServerUtilities.discoverNearbyEvents(String.valueOf(jookeApplication.currentLocation.getLatitude()),
						String.valueOf(jookeApplication.currentLocation.getLongitude()), jookeApplication.currentZipCode, userId);

			} catch (Exception e) {
				return null;
			}
		}
		protected void onPostExecute(ArrayList<Event> eventList){
			if(eventList!=null){
				//update the view here.
				//call populate list here. 
				populateList(eventList);
				adapter = new DiscoverAdapter(getActivity(), feedsList);
				setListAdapter(adapter);
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View rootView = inflater.inflate(R.layout.fragment_discover,
				container, false);
		
		swipelistview = (SwipeListView) rootView.findViewById(android.R.id.list);
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
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));
                
             
                //swipelistview.openAnimate(position); //when you touch front view it will open
              
             
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
                
                swipelistview.closeAnimate(position);//when you touch back view it will close
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
            	
            }

        });
        
        //These are the swipe listview settings. you can change these
        //setting as your requirement 
        swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH); // there are five swiping modes
        swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions 
        swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
       
        swipelistview.setAnimationTime(200); // Animation time
        swipelistview.setSwipeOpenOnLongPress(true); // enable or disable SwipeOpenOnLongPress
		return rootView;
	}

}