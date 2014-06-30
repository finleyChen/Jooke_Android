package com.jooketechnologies.event;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.music.Song;
import com.jooketechnologies.user.User;

public class Event {
	
	// Event settings.
	public String eventName;
	public String hostId;
	public String hostName;
	public String eventId;
	public String hostProfileImgUrl;
	public String eventMode;
	public String allowAddSongs;

	// User list and song list.
	public static ArrayList<User> mUserList;
	public static ArrayList<Song> mSongList;
	
	
	// HOST
	// If you are the host, you will init this list by add youself to be the
	// first user.
	// Only the host can call this function since they create this user list.
	// The host needs to convert to a User to provide the parameter.

	
	public Event(String eventName, String hostId, String hostName,
			String hostProfileImgUrl, String eventId, String eventMode,
			String allowAddSongs) {
		this.eventName = eventName;
		this.hostId = hostId;
		this.hostName = hostName;
		this.hostProfileImgUrl = hostProfileImgUrl;
		this.eventId = eventId;
		this.eventMode = eventMode;
		this.allowAddSongs = allowAddSongs;
	}

	public void initUserList(User myself) {

		mUserList = new ArrayList<User>();
		mUserList.add(myself);
	}
	public void initUserList(){
		mUserList = new ArrayList<User>();
	}
	public void initSongList(ArrayList<Song> songList){
		mSongList = new ArrayList<Song>();
		mSongList.addAll(songList);
	}


	// PUBLIC
	private static void getUserListFromJsonArray(JSONArray jsonArray){
		mUserList = new ArrayList<User>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject userJsonObject;
			try {
				
			    userJsonObject = jsonArray.getJSONObject(i);
				String userIp = userJsonObject.getString(Constants.KEY_USER_IP);
				String userId = userJsonObject.getString(Constants.KEY_USER_ID);
				String fullname = userJsonObject.getString(Constants.KEY_FULL_NAME);
				String userProfileImgUrl =  userJsonObject.getString(Constants.KEY_PROFILE_IMG);
				String facebookUrl = userJsonObject.getString(Constants.KEY_FACEBOOK_LINK);
				String twitterUrl = userJsonObject.getString(Constants.KEY_TWITTER_LINK);
				String instagramUrl = userJsonObject.getString(Constants.KEY_INSTAGRAM_LINK);
				User newUser = new User(userIp, userId,fullname,userProfileImgUrl,facebookUrl, twitterUrl, instagramUrl);
				mUserList.add(newUser);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			
			}
		
	}
	private static void getSongListFromJsonArray(JSONArray jsonArray){
		mSongList = new ArrayList<Song>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject songJsonObject;
			try {
				
				songJsonObject = jsonArray.getJSONObject(i);
				
				
				String album = songJsonObject.getString(Constants.KEY_SONG_ALBUM);
				String artist = songJsonObject.getString(Constants.KEY_SONG_ARTIST);
				String title = songJsonObject.getString(Constants.KEY_SONG_TITLE);
				String duration =  songJsonObject.getString(Constants.KEY_SONG_DURATION);
				String md5 = songJsonObject.getString(Constants.KEY_SONG_MD5);
				Song newSong = new Song(album, artist,title,duration,null);
				mSongList.add(newSong);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			
			}
	}
	public void initFromJson(JSONObject jsonObject) {

		try {
			JSONObject eventObject = new JSONObject(jsonObject.getJSONObject(
					"event").toString());
			eventName = eventObject.getString(Constants.KEY_EVENT_NAME);
			hostId = eventObject.getString(Constants.KEY_HOST_ID);
			hostName = eventObject.getString(Constants.KEY_HOST_NAME);
			hostProfileImgUrl = eventObject
					.getString(Constants.KEY_HOST_PROFILE_IMG);
			eventId = eventObject.getString(Constants.KEY_EVENT_ID);
			eventMode = eventObject.getString(Constants.KEY_EVENT_MODE);
			allowAddSongs = eventObject.getString(Constants.KEY_ALLOW_ADDSONGS);

			try {
				JSONArray userJsonArray = new JSONArray(
						eventObject.getString("users"));
				JSONArray songsJsonArray = new JSONArray(
						eventObject.getString("songs"));
				getUserListFromJsonArray(userJsonArray);
				getSongListFromJsonArray(songsJsonArray);
			} catch (org.json.JSONException e) {
				
				e.printStackTrace();
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}

	}
}