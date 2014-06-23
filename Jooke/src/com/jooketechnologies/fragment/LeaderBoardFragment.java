
package com.jooketechnologies.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jooketechnologies.adapter.LeaderBoardAdapter;
import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.R;
import com.jooketechnologies.jooke.Utils;
import com.jooketechnologies.style.BaseSwipeListViewListener;
import com.jooketechnologies.style.SwipeListView;

public class LeaderBoardFragment extends ListFragment {

	ArrayList<HashMap<String, String>> songsList;
	SwipeListView swipelistview;
	LeaderBoardAdapter adapter;
	Context mContext;
	private TextView mLogo;
	
	public void populateList() {
		songsList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();

		// adding each child node to HashMap key => value
		map1.put(Constants.KEY_LEADERBOARD_TITLE,
				"The Celebrated Summer");
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
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();

		populateList();
		adapter = new LeaderBoardAdapter(getActivity(), songsList);
		setListAdapter(adapter);

		final View rootView = inflater.inflate(R.layout.fragment_leaderboard,
				container, false);
		mLogo = (TextView) rootView.findViewById(R.id.logo);
		
		
		Typeface roboto=Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Thin.ttf");
		mLogo.setTypeface(roboto);

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
        swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT); // there are five swiping modes
        swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions 
        //swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        swipelistview.setOffsetLeft(Utils.convertDpToPixel(mContext,220f)); // left side offset
        swipelistview.setOffsetRight(Utils.convertDpToPixel(mContext,0f)); // right side offset
        swipelistview.setAnimationTime(200); // Animation time
        swipelistview.setSwipeOpenOnLongPress(false); // enable or disable SwipeOpenOnLongPress
		return rootView;
	}
	
	
}