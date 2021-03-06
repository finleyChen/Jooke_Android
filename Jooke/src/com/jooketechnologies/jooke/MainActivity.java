package com.jooketechnologies.jooke;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jooketechnologies.adapter.MainPageAdapter;
import com.jooketechnologies.style.Typefaces;

public class MainActivity extends CustomActivity implements LocationListener {
	MainPageAdapter mAdapter;
	ViewPager mPager;
	public Button joinBtn;
	public Button hostBtn;
	public JookeApplication jookeApplication;
	public Typeface normal;
	public Typeface bold;

	public class DetailOnPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {
		private int currentPage;

		@Override
		public void onPageSelected(int position) {
			currentPage = position;
			switch (currentPage) {
			case 0:
				// set bold
				hostBtn.setTypeface(bold);
				joinBtn.setTypeface(normal);
				break;
			case 1:
				// set bold
				joinBtn.setTypeface(bold);
				hostBtn.setTypeface(normal);
				break;
			}
		}

		public final int getCurrentPage() {
			return currentPage;
		}
	}

	public void hostOnClick(View v) {
		Log.e("host", "host");
		mPager.setCurrentItem(0);

	}

	public void joinOnClick(View v) {
		Log.e("join", "join");
		mPager.setCurrentItem(1);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outevent_main);

		jookeApplication = (JookeApplication) getApplication();
		
		normal= (Typeface) Typefaces.get(this, "fonts/Roboto-Thin.ttf");
		bold= (Typeface) Typefaces.get(this, "fonts/Roboto-Medium.ttf");

		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L,
				500.0f, this);
		Location location = locManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		jookeApplication.currentLocation = location;
		// Add alertdialog saying Jooke would like to know your current
		// location... JJ
		double latitude = 0;
		double longitude = 0;
		if(location!=null){
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			
			
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			// lat,lng, your current location
			try {
				List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
				jookeApplication.currentZipCode = addresses.get(0).getPostalCode();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
		else{
			Log.e("failed to get the current location","fail to get the current location");
		}

		mAdapter = new MainPageAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new DetailOnPageChangeListener());

		hostBtn = (Button) findViewById(R.id.btnHost);
		hostBtn.setTypeface(bold);
		joinBtn = (Button) findViewById(R.id.btnJoin);
		joinBtn.setTypeface(normal);


	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
}
