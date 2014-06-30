package com.jooketechnologies.jooke;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

public class SharedPreferenceUtils {

	public static void storeWSUri(Context mContext, String wsuri) {

		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("wsuri", wsuri);
		prefsEditor.commit();
	}

	public static String getStoredWSUri(Context mContext) {

		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		return mPrefs.getString("wsuri", null);
	}

	public static void storeAddSongSettings(Context mContext,
			boolean allowAddSongs) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putBoolean("allowAddSongs", allowAddSongs);
		prefsEditor.commit();
	}

	public static void storeEventMode(Context mContext, boolean allowVoting) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putBoolean("allowVoting", allowVoting);
		prefsEditor.commit();
	}

	public static boolean getAddSongSetting(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);
		return mPrefs.getBoolean("allowAddSongs", false);
	}

	public static boolean getAllowVoting(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);
		return mPrefs.getBoolean("allowVoting", false);
	}

	public static void storeProfileImageUri(Context mContext, Uri mUri) {
		Log.e("mUri", mUri + "");
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("profileImgUrl", mUri.toString());
		prefsEditor.commit();
	}

	public static void storeLoginType(Context mContext, int signupType) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putInt("loginType", signupType);
		prefsEditor.commit();
	}

	public static void storeSignupType(Context mContext, int signupType) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putInt("signupType", signupType);
		prefsEditor.commit();
	}

	public static void storeFullName(Context mContext, String fullName) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("fullName", fullName);
		prefsEditor.commit();
	}

	public static void storeJookeUserId(Context mContext, String userid) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		Log.e("stored", userid);
		prefsEditor.putString("uId", userid);
		prefsEditor.commit();
	}
	public static void storeUserProfileImgUrl(Context mContext, String imgUrl){
		SharedPreferences mPrefs = mContext.getSharedPreferences("JookeUserInfo", Context.MODE_PRIVATE);
		
		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("profileImgUrl", imgUrl);
		prefsEditor.commit();
	}
	public static void storeHostId(Context mContext, String hostId) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("hostId", hostId);
		prefsEditor.commit();
	}
	public static void storeEventId(Context mContext, String eventId) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("eventId", eventId);
		prefsEditor.commit();
	}
	public static void storeHostFullname(Context mContext, String hostFullname) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("hostFullname", hostFullname);
		prefsEditor.commit();
	}
	
	// Store Event Name
	public static void storeEventName(Context mContext, String eventName) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("eventName", eventName);
		prefsEditor.commit();
	}
	public static String getHostId(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		return mPrefs.getString("hostId", null);
	}
	public static String getHostFullname(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		return mPrefs.getString("hostFullname", null);
	}
	
	
	public static String getHostIp(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		return mPrefs.getString("hostIp", null);
	}
	
	public static String getEventName(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		return mPrefs.getString("eventName", null);
	}
	
	public static String getUserProfileImgUrl(Context mContext){
		SharedPreferences mPrefs = mContext.getSharedPreferences("JookeUserInfo", Context.MODE_PRIVATE);
		return mPrefs.getString("profileImgUrl", null);
	}
	// Store Event Role
	public static void storeEventRole(Context mContext, boolean isHost) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putBoolean("isHost", isHost);
		prefsEditor.commit();
	}

	public static boolean getEventRole(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		return mPrefs.getBoolean("isHost", false);
	}
	public static String getEventId(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		return mPrefs.getString("eventId", null);
	}

	// //Store is In Event
	// public static void storeIsInEvent(Context mContext, boolean isInEvent){
	// SharedPreferences mPrefs = mContext.getSharedPreferences(
	// "JookeUserInfo", Context.MODE_PRIVATE);
	//
	// Editor prefsEditor = mPrefs.edit();
	// prefsEditor.putBoolean("isInEvent", isInEvent);
	// prefsEditor.commit();
	// }
	// public static boolean getIsInEvent(Context mContext){
	// SharedPreferences mPrefs = mContext.getSharedPreferences(
	// "JookeUserInfo", Context.MODE_PRIVATE);
	//
	// return mPrefs.getBoolean("isInEvent", false);
	// }

	// When you host an event, or join an event, you will call this function to
	// save
	// event-related status in batch.

	// before call this function, the event status will be checked to make sure
	// you are not in an event.
	public static void storeEventStatus(Context mContext, String eventId,
			String hostId,String hostFullname,String eventName, 
			boolean isHost, boolean event_mode,boolean allow_add) {
		storeEventId(mContext, eventId);
		storeHostId(mContext,hostId);
		storeHostFullname(mContext,hostFullname);
		storeEventName(mContext, eventName);
		storeEventRole(mContext, isHost);
		storeAddSongSettings(mContext, allow_add);
		storeEventMode(mContext, event_mode);
	}
	
	// If you leave an event, this function will be called.
	public static void clearEventStatus(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);
		Editor prefsEditor = mPrefs.edit();
		prefsEditor.remove("eventId");
		prefsEditor.remove("eventName");
		prefsEditor.remove("isHost");
		prefsEditor.remove("allowVoting");
		prefsEditor.remove("allowAddSongs");
		prefsEditor.commit();
	}

	public static String getUserId(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		return mPrefs.getString("uId", null);
	}

	public static String getFullName(Context mContext) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		return mPrefs.getString("fullName", null);
	}

	public static void storeHostIp(Context mContext, String hostIp) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);
		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("hostIp", hostIp);
		prefsEditor.commit();
	}


	
	public static void storeThirdPartyId(Context mContext, String thirdPartyId) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("thirdPartyId", thirdPartyId);
		prefsEditor.commit();
	}

	public static void storeProfileImgUrl(Context mContext, String profileUrl) {
		SharedPreferences mPrefs = mContext.getSharedPreferences(
				"JookeUserInfo", Context.MODE_PRIVATE);

		Editor prefsEditor = mPrefs.edit();
		prefsEditor.putString("profileImgUrl", profileUrl);
		prefsEditor.commit();
	}
}