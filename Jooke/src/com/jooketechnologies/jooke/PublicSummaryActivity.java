package com.jooketechnologies.jooke;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.jooketechnologies.adapter.ProfileHistoryAdapter;

// Extra credit: add a birthday field using DatePicker, can save and load
// You can find sample code here:
// http://developer.android.com/resources/tutorials/views/hello-datepicker.html

public class PublicSummaryActivity extends Activity {

	TextView leftText, rightText;
	ViewSwitcher viewSwitcher;
	Animation slide_in_left, slide_out_right;

	ArrayList<HashMap<String, String>> historyList;
	NodeList nl;
	ListView list;
	ProfileHistoryAdapter adapter;
	Context mContext;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.empty_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;

		}
		return false;
	}

	public void populateList() {
		historyList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();

		// adding each child node to HashMap key => value
		map1.put(Constants.KEY_HOME_FEED_ACTION, "is now following");
		map1.put(Constants.KEY_HOME_FEED_SUBJECT1, "JJ Ma");
		map1.put(Constants.KEY_HOME_FEED_SUBJECT2, "Larry Minaj");
		map1.put(Constants.KEY_HOME_PASS_TIME, "10s");
		map1.put(Constants.KEY_HOME_SUBJECT1_PROFILE_IMG, "jjma");

		// adding HashList to ArrayList
		historyList.add(map1);
		HashMap<String, String> map2 = new HashMap<String, String>();
		// adding each child node to HashMap key => value
		map2.put(Constants.KEY_HOME_FEED_ACTION, "attended");
		map2.put(Constants.KEY_HOME_FEED_SUBJECT1, "JJ Ma");
		map2.put(Constants.KEY_HOME_FEED_SUBJECT2, "Fuckin roll");
		map2.put(Constants.KEY_HOME_PASS_TIME, "1d");
		map2.put(Constants.KEY_HOME_SUBJECT1_PROFILE_IMG, "jjma");
		historyList.add(map2);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_summary);
		leftText = (TextView) findViewById(R.id.view_switcher_button_left);
		rightText = (TextView) findViewById(R.id.view_switcher_button_right);

		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);

		slide_in_left = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left);
		slide_out_right = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);

		viewSwitcher.setInAnimation(slide_in_left);
		viewSwitcher.setOutAnimation(slide_out_right);
		
		leftText.setOnClickListener(new OnClickListener() {
			 
		       
            public void onClick(View arg0) {
                 
                Toast.makeText(getApplicationContext(), "Showing previous view..", Toast.LENGTH_SHORT).show();
                viewSwitcher.showPrevious();
            }
        });
 
		rightText.setOnClickListener(new OnClickListener() {
 
     
            public void onClick(View arg0) {
                 
                Toast.makeText(getApplicationContext(), "Showing next view..", Toast.LENGTH_SHORT).show();
                viewSwitcher.showNext();
            }
        });

//		populateList();
//		adapter = new ProfileHistoryAdapter(this, historyList);
		Intent receivedIntent = getIntent();
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle(
				receivedIntent.getStringExtra(Constants.KEY_EVENT_NAME));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

}