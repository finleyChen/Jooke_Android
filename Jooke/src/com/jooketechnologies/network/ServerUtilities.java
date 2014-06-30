/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jooketechnologies.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.jooketechnologies.event.Event;
import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.SharedPreferenceUtils;
import com.jooketechnologies.user.User;

/**
 * Helper class used to communicate with the demo server.
 */
public class ServerUtilities {
	static AmazonS3Client s3Client = new AmazonS3Client(
			new BasicAWSCredentials(Constants.ACCESS_KEY_ID,
					Constants.SECRET_KEY));
	public static long joinEvent(String event_name, String user_id,
			String user_ip, String join_time) {
		long event_id = 0;
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.JOIN_EVENT_ACTION_NAME;
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.KEY_EVENT_NAME, event_name);
		params.put(Constants.KEY_USER_ID, user_id);
		params.put(Constants.KEY_USER_IP, user_ip);
		params.put(Constants.KEY_JOIN_TIME, join_time);

		post(serverUrl, Constants.JOIN_EVENT_ACTION_CODE, params);
		return event_id;
	}

	public static String createEvent(String event_name, String event_mode, String allow_addsongs, String event_zip_code,String lat, String lon, String event_time, String host_id, String host_ip){
		Log.e("createEvent","createEvent"+event_name+event_mode+event_zip_code+lat+lon+event_time+host_id+host_ip);
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.CREATE_EVENT_ACTION_NAME;
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.KEY_EVENT_NAME, event_name);
		params.put(Constants.KEY_EVENT_ZIP_CODE, event_zip_code);
		params.put(Constants.KEY_EVENT_TIME, event_time);
		params.put(Constants.KEY_HOST_IP, host_ip);
		params.put(Constants.KEY_HOST_ID, host_id);
		params.put(Constants.KEY_LAT, lat);
		params.put(Constants.KEY_LON, lon);
		params.put(Constants.KEY_EVENT_MODE, event_mode);
		params.put(Constants.KEY_ALLOW_ADDSONGS, allow_addsongs);
		params.put( Constants.KEY_HOST_NAME, "a");
		params.put(Constants.KEY_HOST_PROFILE_IMG, "http://www.addictedtoibiza.com/wp-content/uploads/2012/12/example.png");
		JSONObject jsonObject = post(serverUrl, Constants.JOIN_EVENT_ACTION_CODE, params);
		String event_id=null;
		try {
			event_id = jsonObject.getString("event_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return event_id;
	}
	// get a profile from a certain user and update the profile to the user list. 
	public static User getProfile(String user_id, String user_ip){
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.GET_PROFILE_ACTION_NAME;
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.KEY_USER_ID, user_id
				);
		JSONObject jsonObject = post(serverUrl, Constants.GET_PROFILE_ACTION_CODE, params);
		try {
			String fullname = jsonObject.getString(Constants.KEY_FULL_NAME);
			String facebookLink = jsonObject.getString(Constants.KEY_FACEBOOK_LINK);
			String instagramLink = jsonObject.getString(Constants.KEY_INSTAGRAM_LINK);
			String twitterLink = jsonObject.getString(Constants.KEY_TWITTER_LINK);
			String userProfileImgUrl = jsonObject.getString(Constants.KEY_PROFILE_IMG);
			if (instagramLink.equals("null") )
			{
				instagramLink = null;
			}
			if (twitterLink.equals("null")  ){
				twitterLink = null;
			}
			
			if (userProfileImgUrl.equals("null") ){
				userProfileImgUrl = null;
			}
			if (facebookLink.equals("null") ){
				facebookLink = null;
			}
			User newUser = new User(user_ip, user_id,fullname,
					userProfileImgUrl,facebookLink, twitterLink, instagramLink);
			return newUser;
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return null;
		
	}
		  
	public static String leaveEvent(String user_id, String event_id) {
		String success=null;
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.LEAVE_ACTION_NAME;
		Map<String, String> params = new HashMap<String, String>();
		Log.e("event_id",event_id);
		Log.e("user_id",user_id);
		params.put(Constants.KEY_HOST_ID, user_id);
		params.put(Constants.KEY_EVENT_ID, event_id);
		JSONObject jsonObject = post(serverUrl, Constants.LEAVE_ACTION_CODE, params);
		try {
			success = (String)jsonObject.getString("success");
			Log.e("event_id",event_id);
			Log.e("user_id",user_id);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return success;
	}

	public static long saveProfile(String user_id, String full_name,
			String instagram_link, String facebook_link, String twitter_link,
			String profile_img) {
		long event_id = 0;
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.LEAVE_ACTION_NAME;
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.KEY_USER_ID, user_id);
		params.put(Constants.KEY_FULL_NAME, full_name);
		params.put(Constants.KEY_INSTAGRAM_LINK, instagram_link);
		params.put(Constants.KEY_FACEBOOK_LINK, facebook_link);
		params.put(Constants.KEY_TWITTER_LINK, twitter_link);
		params.put(Constants.KEY_PROFILE_IMG, profile_img);
		post(serverUrl, Constants.SAVE_PROFILE_ACTION_CODE, params);
		return event_id;
	}

	public static long kickOut(String user_id, String host_id) {
		long event_id = 0;
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.KICK_OUT_ACTION_NAME;
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.KEY_USER_ID, user_id);
		params.put(Constants.KEY_HOST_ID, host_id);

		post(serverUrl, Constants.KICK_OUT_ACTION_CODE, params);
		return event_id;
	}

	public static String signUp(String email, int signup_type, String fullname,
			String profile_img, String password, String thirdparty_id) {
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.SIGNUP_ACTION_NAME;
		Log.e(""+signup_type,thirdparty_id+","+email+","+fullname+","+password+","+profile_img);
		Map<String, String> params = new HashMap<String, String>();
		
		params.put(Constants.KEY_SIGNUP_TYPE, signup_type + "");
		if(thirdparty_id!= null)
			params.put(Constants.KEY_THIRD_ID, thirdparty_id + "");
		if(email!= null)
			params.put(Constants.KEY_EMAIL, email);
		if(fullname!= null)
			params.put(Constants.KEY_FULL_NAME, fullname);
		if(password!= null)
			params.put(Constants.KEY_PASSWORD, password);
		
			params.put(Constants.KEY_PROFILE_IMG, "http://abs.twimg.com/sticky/default_profile_images/default_profile_6_normal.png");

		JSONObject jsonObject = post(serverUrl,
				Constants.SIGNUP_ACTION_CODE, params);
		if (jsonObject == null) {
			
			// Toast here.
			return null;
		} else {
			String user_id;
			try {
				Log.e("success", jsonObject.getString("success"));
				Log.e("user_id", jsonObject.getString("userid"));
				user_id = jsonObject.getString("userid");
				return user_id;
			} catch (JSONException e) {
				
				e.printStackTrace();
				return null;
			}

		}
	}

	public static String logIn(Context context, String email, String password, int signup_type,
			String thirdparty_id) {
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.LOGIN_ACTION_NAME;
		Map<String, String> params = new HashMap<String, String>();
		if(email!=null)
			params.put("email", email);
		if(password!=null)
			params.put("password", password);
		
		params.put("login_type", signup_type + "");
		if(thirdparty_id!=null)
			params.put("thirdparty_id", thirdparty_id + "");
		JSONObject jsonObject = post(serverUrl, Constants.LOGIN_ACTION_CODE, params);
		try {
			if(jsonObject!=null){
				String success = jsonObject.getString("success");
				Log.e("login success",success);
				if(success.equals("true")){
					//TO ADD.
					String fullname = jsonObject.getString(Constants.KEY_FULL_NAME);
					String profile_img = jsonObject.getString(Constants.KEY_PROFILE_IMG);
					SharedPreferenceUtils.storeFullName(context, fullname);
					SharedPreferenceUtils.storeProfileImageUri(context,Uri.parse(profile_img));
					return jsonObject.getString("userid");
				}
			}
			else{
				
			}
			
		} catch (JSONException e) {
			
			Log.e("JSONException", "exception"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static void forgetPassword(String email) {
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.FORGET_PASSWORD_ACTION_NAME;

		Map<String, String> params = new HashMap<String, String>();
		params.put("email", email);

		post(serverUrl, Constants.FORGET_PASSWORD_ACTION_CODE, params);
	}
	public static ArrayList<Event> getEventListsFromJsonArray(JSONArray jsonArray){
		ArrayList<Event> eventList = new ArrayList<Event>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject eventJsonObject;
			try {
				eventJsonObject = jsonArray.getJSONObject(i);
				String eventName = eventJsonObject.getString(Constants.KEY_EVENT_NAME);
				String hostId = eventJsonObject.getString(Constants.KEY_HOST_ID);
				String hostProfileImgUrl = eventJsonObject.getString(Constants.KEY_HOST_PROFILE_IMG);
				String hostName =  eventJsonObject.getString(Constants.KEY_HOST_NAME);
				String eventId = eventJsonObject.getString(Constants.KEY_EVENT_ID);
				String allowAddSongs = eventJsonObject.getString(Constants.KEY_ALLOW_ADDSONGS);
				String eventMode = eventJsonObject.getString(Constants.KEY_EVENT_MODE);
				
				Event newEvent = new Event(eventName, hostId, hostName, hostProfileImgUrl, eventId, eventMode, allowAddSongs);
				eventList.add(newEvent);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			
			}
		return eventList;
		
	}
	public static String getHostIp(String host_id,String event_id){
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.GET_HOST_IP_ACTION_NAME;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("host_id", host_id);
		params.put("event_id", event_id);
		
		Log.e("event_id",event_id);
		Log.e("host_id",host_id);
		JSONObject jsonObject = post(serverUrl, Constants.GET_HOST_IP_ACTION_CODE, params);
		
		try {
			String host_ip = jsonObject.getString(Constants.KEY_HOST_IP);
			if(host_ip!=null){
				Log.e("host_ip",host_ip);
			}
			else{
				Log.e("host_ip","null");
			}
			return host_ip;
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<Event> discoverNearbyEvents(String current_loc_lat,
			String current_loc_lon, String current_zip, String user_id) {
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.DISCOVER_ACTION_NAME;
		Log.e("discoverNearbyEvents","discoverNearbyEvents");
		Map<String, String> params = new HashMap<String, String>();
		params.put("current_loc_lat", current_loc_lat);
		params.put("current_loc_lon", current_loc_lon);
		params.put("current_zip", current_zip);

		JSONObject jsonObject = post(serverUrl, Constants.DISCOVER_ACTION_CODE, params);
		Log.e("jsonObject",jsonObject.toString());
		
		
		try {
			JSONObject valueObject = new JSONObject(jsonObject.getJSONObject("value").toString());
			JSONArray jsonArray = new JSONArray(valueObject.getString("events"));
			return getEventListsFromJsonArray(jsonArray);
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return null;
	}

	public static void heartBeat(String user_id, String user_ip, String event_id) {
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.HEARTBEAT_ACTION_NAME;

		Map<String, String> params = new HashMap<String, String>();
		params.put("id", user_id);
		params.put("user_ip", user_ip);
		params.put("event_id", event_id);

		post(serverUrl, Constants.HEARTBEAT_ACTION_CODE, params);
	}

	public static void pullInfo(String host_id) {
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.PULLINFO_ACTION_NAME;

		Map<String, String> params = new HashMap<String, String>();
		params.put("host_id", host_id);

		post(serverUrl, Constants.PULLINFO_ACTION_CODE, params);
	}

	public static void updatePeople(String host_id, String event_name) {
		String serverUrl = Constants.CHAT_SERVER_URL
				+ Constants.UPDATE_PEOPLE_ACTION_NAME;
		Map<String, String> params = new HashMap<String, String>();
		params.put("host_id", host_id);
		params.put("event_name", event_name);

		post(serverUrl, Constants.UPDATE_PEOPLE_ACTION_CODE, params);
	}

	public static JSONObject streamToString(InputStream is) throws IOException {
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(
				is, "UTF-8"));
		StringBuilder responseStrBuilder = new StringBuilder();

		String inputStr;
		while ((inputStr = streamReader.readLine()) != null)
			responseStrBuilder.append(inputStr);
		JSONObject jsonObject;
		try {
			Log.e("responseStrBuilder.toString()",responseStrBuilder.toString());
			if(responseStrBuilder.toString()==null){
				Log.e("not return value","not return value");
				return null;
			}
			else{
				jsonObject = new JSONObject(responseStrBuilder.toString());
				return jsonObject;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	static JSONObject post(String endpoint, int action,
			Map<String, String> params) {
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response

			int status = conn.getResponseCode();
			InputStream is = conn.getInputStream();
			if (status != 200) {
				if (conn != null) {
					conn.disconnect();
				}
			} else {
				JSONObject jsonObject = streamToString(is);
				return jsonObject;
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;

	}

	private class S3TaskResult {
		String errorMessage = null;
		Uri uri = null;

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public Uri getUri() {
			return uri;
		}

		public void setUri(Uri uri) {
			this.uri = uri;
		}
	}

	public class S3PutObjectTask extends AsyncTask<Uri, Void, S3TaskResult> {
		Context mContext;

		public S3PutObjectTask(Context mContext) {
			this.mContext = mContext;
		}

		protected S3TaskResult doInBackground(Uri... uris) {

			if (uris == null || uris.length != 1) {
				Log.e("return null", "return null");
				return null;
			}

			// The file location of the image selected.
			Uri selectedImage = uris[0];

			ContentResolver resolver = mContext.getContentResolver();
			String fileSizeColumn[] = { OpenableColumns.SIZE };

			Cursor cursor = resolver.query(selectedImage, fileSizeColumn, null,
					null, null);

			cursor.moveToFirst();

			int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

			String size = null;
			if (!cursor.isNull(sizeIndex)) {
				// Technically the column stores an int, but cursor.getString
				// will do the
				// conversion automatically.
				size = cursor.getString(sizeIndex);
			}

			cursor.close();

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(resolver.getType(selectedImage));
			if (size != null) {
				metadata.setContentLength(Long.parseLong(size));
			}

			S3TaskResult result = new S3TaskResult();

			// Put the image data into S3.
			try {
				// s3Client.createBucket(Constants.getPictureBucket());

				PutObjectRequest por = new PutObjectRequest(
						Constants.PICTURE_BUCKET, "1.png",
						resolver.openInputStream(selectedImage), metadata)
						.withCannedAcl(CannedAccessControlList.PublicRead);
				Log.e("before exception", "before exception");
				s3Client.putObject(por);
			} catch (Exception exception) {
				Log.e("exception", exception.getMessage());
				result.setErrorMessage(exception.getMessage());
			}

			return result;
		}

		protected void onPostExecute(S3TaskResult result) {

			if (result.getErrorMessage() == null) {

				new S3GeneratePresignedUrlTask(mContext).execute();
			}
		}

	}

	public class S3GeneratePresignedUrlTask extends
			AsyncTask<Void, Void, S3TaskResult> {
		Context mContext;

		public S3GeneratePresignedUrlTask(Context mContext) {
			this.mContext = mContext;
		}

		protected S3TaskResult doInBackground(Void... voids) {

			S3TaskResult result = new S3TaskResult();

			try {
				// Ensure that the image will be treated as such.
				ResponseHeaderOverrides override = new ResponseHeaderOverrides();
				override.setContentType("image/jpeg");

				// Generate the presigned URL.

				// Added an hour's worth of milliseconds to the current time.
				Date expirationDate = new Date(
						System.currentTimeMillis() + 3600000);
				GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
						Constants.getPictureBucket(), "1.png");
				urlRequest.setExpiration(expirationDate);
				urlRequest.setResponseHeaders(override);

				URL url = s3Client.generatePresignedUrl(urlRequest);

				result.setUri(Uri.parse(url.toURI().toString()));

			} catch (Exception exception) {

				result.setErrorMessage(exception.getMessage());
			}

			return result;
		}

		protected void onPostExecute(S3TaskResult result) {

			if (result.getErrorMessage() != null) {

				// displayErrorAlert(
				// S3UploaderActivity.this
				// .getString(R.string.browser_failure_title),
				// result.getErrorMessage());

				// return null;
			} else if (result.getUri() != null) {
				SharedPreferenceUtils.storeProfileImageUri(mContext,
						result.getUri());
				// // Display in Browser.
				// startActivity(new Intent(Intent.ACTION_VIEW,
				// result.getUri()));
			}
		}
	}

	public void generateProfileImageUrl(Uri uri, Context context) {
		s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
		new S3PutObjectTask(context).execute(uri);

	}
}
