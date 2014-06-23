package com.jooketechnologies.jooke;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.jooketechnologies.adapter.PeopleListAdapter;
import com.jooketechnologies.user.User;

public class PeopleListActivity extends ListActivity  {
	
	
	public PeopleListAdapter peopleadapter;
	public JookeApplication jookeApplication;
	ArrayList<HashMap<String, String>> peopleList;
//	UserList userList; 
	ListView list;
	
	// here we extract the list from the JookeApplication. 
	public void populateList() {
//		userList = jookeApplication.getUserList();
		peopleList = new ArrayList<HashMap<String, String>>();
		
//		for(User user:userList.getUserList()){
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put(Constants.KEY_FULL_NAME, user.userName);
//			map.put(Constants.KEY_PROFILE_IMG, user.userProfileImgUrl);
//			peopleList.add(map);
//		}
				
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.empty_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; goto parent activity.
	            this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent publicProfileIntent = new Intent(this, PublicProfileActivity.class);
//        publicProfileIntent.putExtra(Constants.KEY_FACEBOOK_LINK, userList.getUserList().get(position).facebookUrl);
//        publicProfileIntent.putExtra(Constants.KEY_INSTAGRAM_LINK, userList.getUserList().get(position).instagramUrl);
//        publicProfileIntent.putExtra(Constants.KEY_TWITTER_LINK, userList.getUserList().get(position).twitterUrl);
//        publicProfileIntent.putExtra(Constants.KEY_FULL_NAME, userList.getUserList().get(position).userName);
//        
        startActivity(publicProfileIntent);
        
        
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jookeApplication = (JookeApplication)getApplication();
		setContentView(R.layout.activity_people_list);
		
		// Getting adapter by passing xml data ArrayList
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		populateList();
		peopleadapter = new PeopleListAdapter(this, peopleList);
		setListAdapter(peopleadapter);
		
	}

	


}