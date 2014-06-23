package com.jooketechnologies.jooke;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jooketechnologies.network.ServerUtilities;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class SignupActivity extends Activity {

	public EditText emailField;
	public EditText passwordField;
	public EditText fullname;
	public ImageView btnFbSignup;
	public ImageView btnTwSignup;
	public Context mContext;
	private SimpleFacebook mSimpleFacebook;
	
	
	static final String TWITTER_CALLBACK_URL = "twitterapp://connect_signup";
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	
	
	private static Twitter twitter;
	private static RequestToken requestToken;
	
	static String TWITTER_CONSUMER_KEY = "FeLlyrLvDyo0kWTFWeLtMPX42";
	static String TWITTER_CONSUMER_SECRET = "qIiABruCZenffdVC0O6uT9QYZf4ps3dUfYmkdFGphE6z7coq1p";
	
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	public AccessToken accessToken;
	
	
	private static SharedPreferences mSharedPreferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		mContext = this;
		emailField = (EditText) findViewById(R.id.login_email_field);
		passwordField = (EditText) findViewById(R.id.login_password_field);
		fullname = (EditText) findViewById(R.id.login_username_email_field);
		btnFbSignup = (ImageView) findViewById(R.id.signup_facebook_button);
		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
		.setAppId(Constants.FACEBOOK_APP_ID).setNamespace(Constants.APP_NAMESPACE).build();
		btnFbSignup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Fire up the facebook app, 
				//Save the login type into sharedpreference. 
				//When it goes back, fire up the loginAsyncTask.
				mSimpleFacebook.login(mOnLoginListener);
				SharedPreferenceUtils.storeLoginType(mContext, Constants.SIGNUP_TYPE_FACEBOOK);
			}
		});
		btnTwSignup = (ImageView) findViewById(R.id.signup_twitter_button);
		btnTwSignup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Fire up the twitter app, 
				//Save the login type into sharedpreference. 
				//When it goes back, fire up the loginAsyncTask.
				Log.e("onClick", "onClick");
				new signupTwitterTask().execute();
				SharedPreferenceUtils.storeLoginType(mContext, Constants.SIGNUP_TYPE_TWITTER);
			}
		});
		passwordField
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							new signupAsyncTask().execute();
							return true;
						}
						return false;
					}
				});
		
		
		
		//Get back from twitter auth. 
		if (!isTwitterLoggedInAlready()) {
			Uri uri = getIntent().getData();
			if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
				try {
					Toast.makeText(getApplicationContext(),
							"Signup successful!", Toast.LENGTH_LONG).show();
					Intent mainActivityIntent = new Intent(mContext, MainActivity.class);
					startActivity(mainActivityIntent);
					new saveAndUploadTwitterProfileTask().execute(uri);
					finish();
				} catch (Exception e) {
					// Check log for login errors
					Log.e("Twitter Login Error", "> " + e.getMessage());
				}
			}
		}
	}
	
	final OnProfileListener onProfileListener = new OnProfileListener() {

		@Override
		public void onFail(String reason) {
			
		}

		@Override
		public void onException(Throwable throwable) {
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while fetching
			// profile
			Log.e("on thinking","on thinking");
		}

		@Override
		public void onComplete(Profile profile) {
			if(profile.getPicture()==null){
				Log.e("wtf","wtf");
			}
			//Log.e("profile",profile.getPicture());
			new saveAndUploadFacebookProfileTask(null, profile.getId(), profile.getName()).execute();
		}
	};
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);

		return;

	}
	private OnLoginListener mOnLoginListener = new OnLoginListener() {

			@Override
			public void onFail(String reason) {
			
			}

			@Override
			public void onException(Throwable throwable) {
				
			}

			@Override
			public void onThinking() {
			}

			@Override
			public void onLogin() {
				Log.e("signed up","signed up");
				mSimpleFacebook.getProfile(onProfileListener);
			}

			@Override
			public void onNotAcceptingPermissions(Permission.Type type) {
			}
		};
	class signupTwitterTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... none) {
			try {
				loginToTwitter();
				return null;
			} catch (Exception e) {
				return null;
			}
		}

	}
	
	class saveAndUploadFacebookProfileTask extends AsyncTask<Void, Void, Void> {
		public String profileImgUrl;
		public String profileId;
		public String fullName;
		protected Void doInBackground(Void... none) {
			try {
				
				SharedPreferenceUtils.storeProfileImgUrl(mContext, profileImgUrl);
				SharedPreferenceUtils.storeThirdPartyId(mContext, profileId);
				SharedPreferenceUtils.storeSignupType(mContext, Constants.SIGNUP_TYPE_FACEBOOK);
				SharedPreferenceUtils.storeFullName(mContext, fullName);
				String userid = ServerUtilities.signUp(null, Constants.SIGNUP_TYPE_FACEBOOK, fullName, profileImgUrl, null, profileId);
				if(userid!=null){
					//Successful
					Toast.makeText(getApplicationContext(),
							"Signup successful!", Toast.LENGTH_LONG).show();
					Intent mainActivityIntent = new Intent(mContext, MainActivity.class);
					startActivity(mainActivityIntent);
					
					SharedPreferenceUtils.storeJookeUserId(mContext, userid);
					finish();
				}
				else{
					Log.e("something wrong","something wrong");
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}
		public saveAndUploadFacebookProfileTask(String profileImgUrl, String profileId,  String fullName){
			this.profileImgUrl = profileImgUrl;
			this.profileId = profileId;
			this.fullName = fullName;
			
		}
	}
	
	class saveAndUploadTwitterProfileTask extends AsyncTask<Uri, Void, Void> {

		protected Void doInBackground(Uri... uri) {
			try {
				String verifier = uri[0]
						.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
				accessToken = twitter.getOAuthAccessToken(requestToken,
						verifier);
				// Shared Preferences
				Editor e = mSharedPreferences.edit();

				// After getting access token, access token secret
				// store them in application preferences
				e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
				e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
				// Store login status - true
				e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
				e.commit(); // save changes
				long userID = accessToken.getUserId();
				User user = twitter.showUser(userID);
				SharedPreferenceUtils.storeProfileImgUrl(mContext, user.getProfileImageURL());
				SharedPreferenceUtils.storeThirdPartyId(mContext, String.valueOf(user.getId()));
				SharedPreferenceUtils.storeSignupType(mContext, Constants.SIGNUP_TYPE_TWITTER);
				SharedPreferenceUtils.storeFullName(mContext, user.getName());
				String userid = ServerUtilities.signUp(null, Constants.SIGNUP_TYPE_TWITTER, user.getName(), user.getProfileImageURL(), null, String.valueOf(user.getId()));
				if(userid!=null){
					//Successful
					Toast.makeText(getApplicationContext(),
							"Signup successful!", Toast.LENGTH_LONG).show();
					Intent mainActivityIntent = new Intent(mContext, MainActivity.class);
					startActivity(mainActivityIntent);
					finish();
					SharedPreferenceUtils.storeJookeUserId(mContext, userid);
				}
				else{
					Log.e("shuold not happen","should not happen");
					Toast.makeText(getApplicationContext(),
							"Your email is already registered. Please log in.", Toast.LENGTH_LONG).show();
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}
		

	}
	@Override
	protected void onResume(){
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}
	private boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}
	private void loginToTwitter() {
		if (!isTwitterLoggedInAlready()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter
						.getOAuthRequestToken(TWITTER_CALLBACK_URL);
				Log.e("requestToken", requestToken + "");
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(requestToken.getAuthenticationURL())));
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("already logged in","already logged in");
			// user already logged into twitter
			
		}
	}
	public class signupAsyncTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... none) {
			String email;
			String password;
			if(emailField.length() == 0){
				email = null;
			}
			else{
				email = emailField.getText().toString();
			}
			if(passwordField.length() ==0){
				password = null;
			}
			else{
				password = emailField.getText().toString();
			}
			String userid = ServerUtilities.signUp(email, 0, fullname.getText()
					.toString(), null, password, null);
			if(userid!=null){
				SharedPreferenceUtils.storeJookeUserId(mContext, userid);
			}
			else{
				Log.e("shuold not happen","should not happen");
			}
			return null;
		}
	}
}