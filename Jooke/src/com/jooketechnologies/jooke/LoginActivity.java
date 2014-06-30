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

import com.jooketechnologies.network.AlertDialogManager;
import com.jooketechnologies.network.ConnectionDetector;
import com.jooketechnologies.network.ServerUtilities;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class LoginActivity extends Activity {
	private static final String TAG = "MainActivity";

	public String userid;
	public EditText emailField;
	public EditText passwordField;
	private SimpleFacebook mSimpleFacebook;
	private static Twitter twitter;
	private static RequestToken requestToken;
	private ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	private static SharedPreferences mSharedPreferences;
	private Context mContext;

	static String TWITTER_CONSUMER_KEY = "FeLlyrLvDyo0kWTFWeLtMPX42";
	static String TWITTER_CONSUMER_SECRET = "qIiABruCZenffdVC0O6uT9QYZf4ps3dUfYmkdFGphE6z7coq1p";

	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	static final String TWITTER_CALLBACK_URL = "twitterapp://connect_login";

	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	// Buttons
	ImageView btnFbLogin;
	ImageView btntwLogin;

	public AccessToken accessToken;

	@Override
	protected void onResume() {
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		btnFbLogin = (ImageView) findViewById(R.id.login_facebook_button);
		btntwLogin = (ImageView) findViewById(R.id.login_twitter_button);

		emailField = (EditText) findViewById(R.id.login_email_field);
		passwordField = (EditText) findViewById(R.id.login_password_field);
		passwordField
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							Log.e("password", "password");
							String email=null;
							if(emailField.length()!=0){
								email = emailField.getText().toString();
							}
							String password=null;
							if(passwordField.length()!=0){
								password = passwordField.getText().toString();
							}
							
							
							new loginAsyncTask(email, password,Constants.SIGNUP_TYPE_NORMAL,null,mContext).execute();
							return true;
						}
						return false;
					}
				});


		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
				.setAppId(Constants.FACEBOOK_APP_ID).setNamespace(Constants.APP_NAMESPACE).build();

		SimpleFacebook.setConfiguration(configuration);

		cd = new ConnectionDetector(getApplicationContext());
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(LoginActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// Check if twitter keys are set
		if (TWITTER_CONSUMER_KEY.trim().length() == 0
				|| TWITTER_CONSUMER_SECRET.trim().length() == 0) {
			// Internet Connection is not present
			alert.showAlertDialog(LoginActivity.this, "Twitter oAuth tokens",
					"Please set your twitter oauth tokens first!", false);
			// stop executing code by return
			return;
		}
		btntwLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("Twitter Button", "button Clicked");
				new LoginTwitterTask().execute();
			}
		});
		if (!isTwitterLoggedInAlready()) {
			Uri uri = getIntent().getData();
			if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
				try {
					new returnTwitterProfileTask().execute(uri);
					
					//twitter login
					
					
				} catch (Exception e) {
					// Check log for login errors
					Log.e("Twitter Login Error", "> " + e.getMessage());
				}
			}
		}
		btnFbLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("Image Button", "facebook Clicked");
				mSimpleFacebook.login(mOnLoginListener);
			}
		});

	}

	final OnProfileListener onProfileListener = new OnProfileListener() {

		@Override
		public void onFail(String reason) {
			// insure that you are logged in before getting the profile
			Log.w(TAG, reason);
		}

		@Override
		public void onException(Throwable throwable) {
			Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while fetching
			// profile
			Log.i(TAG, "Thinking...");
		}

		@Override
		public void onComplete(Profile profile) {
			Log.e(TAG,
					"My profile id = " + profile.getId() + ","
							+ profile.getName() + "," + profile.getEmail() + "");
	
			new loginAsyncTask(null, null, Constants.SIGNUP_TYPE_FACEBOOK,profile.getId(),mContext).execute();
		}
	};
	private OnLoginListener mOnLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {

			Log.e(TAG, "Failed to login");
		}

		@Override
		public void onException(Throwable throwable) {

			Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while login is
			// happening
			Log.e(TAG, "Thinking");
		}

		@Override
		public void onLogin() {
			// change the state of the button or do whatever you want
			Log.e(TAG, "Logged in");
			
			mSimpleFacebook.getProfile(onProfileListener);

		}

		@Override
		public void onNotAcceptingPermissions(Permission.Type type) {
			Log.e(TAG, "you didn't accept permissions");
		}
	};

	class returnTwitterProfileTask extends AsyncTask<Uri, Void, Void> {

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
				String username = user.getName();
				new loginAsyncTask(null, null, Constants.SIGNUP_TYPE_TWITTER,String.valueOf(userID),mContext).execute();
				Log.e("twitter",
						username + "," + user.getId() + ","
								+ user.getProfileImageURL());
				return null;
			} catch (Exception e) {
				return null;
			}
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);

		return;

	}

	class LoginTwitterTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... none) {
			try {
				loginToTwitter();
				return null;
			} catch (Exception e) {
				return null;
			}
		}

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
			// user already logged into twitter
			Toast.makeText(getApplicationContext(),
					"Already Logged into twitter", Toast.LENGTH_LONG).show();
		}
	}

	public class loginAsyncTask extends AsyncTask<Void, Void, String> {
		public String email;
		public String password;
		public int signin_type;
		public String thirdparty_id;
		public Context context;
		
		public loginAsyncTask(String email, String password, int signin_type, String thirdparty_id, Context context){
			this.email = email;
			this.password = password;
			this.signin_type = signin_type;
			this.thirdparty_id = thirdparty_id;
			this.context = context;
		}
		protected String doInBackground(Void... none) {
			
			return ServerUtilities.logIn(context,email,
					password, signin_type, thirdparty_id);
		}
		protected void onPostExecute(String uid){
			if(uid!=null){
				SharedPreferenceUtils.storeJookeUserId(mContext, uid);
				finish();
				Toast.makeText(getApplicationContext(),
						"Log in successful!", Toast.LENGTH_LONG).show();
				Intent mainActivityIntent = new Intent(mContext, MainActivity.class);
				startActivity(mainActivityIntent);
				
			}
			else{
				Toast.makeText(getApplicationContext(),
						"Log in failed.", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}

	private boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}
}