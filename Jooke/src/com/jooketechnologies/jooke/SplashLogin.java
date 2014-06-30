package com.jooketechnologies.jooke;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;


public class SplashLogin extends FragmentActivity {
	ViewPager mPager;

	public class DetailOnPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {
		private int currentPage;

		@Override
		public void onPageSelected(int position) {
			currentPage = position;
			
		}

		public final int getCurrentPage() {
			return currentPage;
		}
	}
	 public void loginOnClick(View v) {
			Log.e("login", "login");
			Intent loginIntent = new Intent(this, LoginActivity.class);
			loginIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(loginIntent);
			finish();
		}

		public void signupOnClick(View v) {
			Log.e("signup", "signup");
			
			Intent signupIntent = new Intent(this, SignupActivity.class);
			signupIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(signupIntent);
		}
		public class getIpTask extends AsyncTask<Void, Void, Void> {

			protected Void doInBackground(Void... none) {
				Log.e("kao","kao");
				Utils.getIP();
				return null;
				
			}
		};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String eventId = SharedPreferenceUtils.getEventId(this);
		String eventName = SharedPreferenceUtils.getEventName(this);
		boolean isHost = SharedPreferenceUtils.getEventRole(this);
		boolean allowAddSongs = SharedPreferenceUtils.getAddSongSetting(this);
		boolean allowVoting = SharedPreferenceUtils.getAllowVoting(this);
		Log.e("before execute","before execur");
		new getIpTask().execute();
		// if the user enters the activity with a ongoing event. 
		// redirect to the in event main activity. 
		// and make sure the in event main activity has the right configuration. 
		if(eventId!=null){
			Log.e("eventID",eventId);
			Intent inEventMainIntent = new Intent(this,InEventMainActivity.class);
			inEventMainIntent.putExtra(Constants.KEY_EVENT_NAME, eventName);
			inEventMainIntent.putExtra(Constants.KEY_ALLOW_ADDSONGS, allowAddSongs);
			inEventMainIntent.putExtra(Constants.KEY_EVENT_MODE, allowVoting);
			inEventMainIntent.putExtra(Constants.KEY_IS_HOST, isHost);
			inEventMainIntent.putExtra(Constants.KEY_EVENT_ID, eventId);
		   
		    
		    
		    
			startActivity(inEventMainIntent);
			finish();
			return;
		}
		String userId = SharedPreferenceUtils.getUserId(this);
		if(userId!=null){
			Log.e("userId",userId);
			Intent mainIntent = new Intent(this, MainActivity.class);
			startActivity(mainIntent);
			finish();
			return;
		}
		setContentView(R.layout.activity_splash);


		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setOnPageChangeListener(new DetailOnPageChangeListener());
		
		final Button login = (Button) findViewById(R.id.btnLogin);

		final Button signup = (Button) findViewById(R.id.btnSignup);

		new CountDownTimer(1000, 1000) {

			public void onTick(long millisUntilFinished) {

			}

			public void onFinish() {
				Log.e("onFinish", "onFinish");

				Animation loginAnimation = new TranslateAnimation(0, 0,
						login.getHeight(), 0);
				loginAnimation.setDuration(1000);
				loginAnimation.setFillAfter(true);
				loginAnimation.setFillEnabled(true);
				login.startAnimation(loginAnimation);
				loginAnimation
						.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(Animation arg0) {
								Log.e("animation start",
										"signup animation start");
								login.setVisibility(View.VISIBLE);
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
							}

							@Override
							public void onAnimationEnd(Animation arg0) {
								Log.e("animation start", "signup animation end");
								login.setVisibility(View.VISIBLE);
							}
						});

				Animation signupAnimation = new TranslateAnimation(0, 0,
						signup.getHeight(), 0);
				signupAnimation.setDuration(1000);
				signupAnimation.setFillAfter(true);
				signupAnimation.setFillEnabled(true);
				signup.startAnimation(signupAnimation);
				signupAnimation
						.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(Animation arg0) {
								Log.e("animation start",
										"signup animation start");
								signup.setVisibility(View.VISIBLE);
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {
							}

							@Override
							public void onAnimationEnd(Animation arg0) {
								Log.e("animation start", "signup animation end");
								signup.setVisibility(View.VISIBLE);
							}
						});
			}
		}.start();
		login.setVisibility(View.INVISIBLE);
		signup.setVisibility(View.INVISIBLE);

	}
}
