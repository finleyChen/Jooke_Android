package com.jooketechnologies.jooke;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jooketechnologies.network.ImageLoader;
import com.jooketechnologies.style.FontHelper;

// Extra credit: add a birthday field using DatePicker, can save and load
// You can find sample code here:
// http://developer.android.com/resources/tutorials/views/hello-datepicker.html

public class PublicProfileActivity extends Activity {

	private ImageView profileImgView;
	private TextView facebookTextView;
	private TextView twitterTextView;
	private TextView instagramTextView;
	private TextView fullNameTextView;

	private String mFacebookUrl;
	private String mInstagramUrl;
	private String mTwitterUrl;
	private String mFullName;
	private String mProfileImgUrl;
	public ImageLoader imageLoader;
	
	
	// All static variables
	static final String URL = "http://api.androidhive.info/music/music.xml";
	ArrayList<HashMap<String, String>> historyList;
	NodeList nl;
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
	
	public static Intent getOpenInstagramIntent(Context context, String userName){
		Uri uri = Uri.parse("http://instagram.com/"+userName);
		return new Intent(Intent.ACTION_VIEW, uri);
	}
	
	public static Intent getOpenFacebookIntent(Context context, String id, String userName) {

		try {
			context.getPackageManager()
					.getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("fb://profile/"+id));
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"+userName));
		}
	}

	public static Intent getOpenTwitterIntent(Context context,String userName) {
		try {
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("twitter://user?screen_name="+userName));

		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://twitter.com/#!/"+userName));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_profile);
		FontHelper.applyFont(this, findViewById(R.id.row_profile_header),
				"fonts/gillsans_light.ttf");
		Intent receivedIntent = getIntent();
		imageLoader = new ImageLoader(this.getApplicationContext());
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(
				receivedIntent.getStringExtra(Constants.KEY_PROFILE_NAME));

		
		mFacebookUrl = getIntent().getStringExtra(Constants.KEY_FACEBOOK_LINK);
		mInstagramUrl = getIntent().getStringExtra(Constants.KEY_INSTAGRAM_LINK);
		mTwitterUrl = getIntent().getStringExtra(Constants.KEY_TWITTER_LINK);
		mFullName = getIntent().getStringExtra(Constants.KEY_FULL_NAME);
		mProfileImgUrl = getIntent().getStringExtra(Constants.KEY_PROFILE_IMG);
	
		profileImgView = (ImageView) findViewById(R.id.profile_img);
		imageLoader.DisplayImage(mProfileImgUrl,profileImgView,true);

		facebookTextView = (TextView) findViewById(R.id.facebook);
		facebookTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	startActivity(getOpenFacebookIntent(mContext,mFacebookUrl, mFacebookUrl));
            }
        });
		twitterTextView = (TextView) findViewById(R.id.twitter);
		twitterTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	startActivity(getOpenTwitterIntent(mContext,mTwitterUrl));
            }
        });
		
		
		instagramTextView = (TextView) findViewById(R.id.instagram);
		instagramTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	startActivity(getOpenInstagramIntent(mContext,mInstagramUrl));
            }
        });
		fullNameTextView = (TextView) findViewById(R.id.fullname);
		fullNameTextView.setText(mFullName);

	}
}