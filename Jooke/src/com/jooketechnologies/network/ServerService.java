package com.jooketechnologies.network;

import org.java_websocket.drafts.Draft_17;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.JookeApplication;

public class ServerService extends Service {
	static JookeWebSocketServer wsServer;
	Notification notification;
	HttpServer server;
	TelephonyManager tm;
	JookeApplication jookeApplication;
	public Context mContext;
	private BroadcastReceiver updatesReceiver2Broadcast;
	
	public IBinder onBind(Intent paramIntent) {
		return null;
	}

	public void onCreate() {
		jookeApplication= (JookeApplication)getApplication();
		mContext = this;
		super.onCreate();
	}

	public void onDestroy() {
		super.onDestroy();
	}

	// NOT TESTED. 
	public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
		Log.e("onStartCommand","onStartCommand");
		
		JookeWebSocketServer s = new JookeWebSocketServer( 8069, new Draft_17(), this, jookeApplication);
		s.start();
		updatesReceiver2Broadcast = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.e("onReceive","onReceive");
				String updateAction = intent.getAction();
				
				JSONObject updateJsonObject = new JSONObject();
				if(updateAction.equals(Constants.KEY_ADD_PEOPLE)){
					
					JSONObject addPeopleJsonObject = new JSONObject();
					try {
						addPeopleJsonObject.put(Constants.KEY_USER_ID, intent.getStringExtra(Constants.KEY_USER_ID));
						addPeopleJsonObject.put(Constants.KEY_FULL_NAME, intent.getStringExtra(Constants.KEY_FULL_NAME));
						addPeopleJsonObject.put(Constants.KEY_PROFILE_IMG, intent.getStringExtra(Constants.KEY_PROFILE_IMG));
						addPeopleJsonObject.put(Constants.KEY_USER_ID, intent.getStringExtra(Constants.KEY_USER_ID));
						addPeopleJsonObject.put(Constants.KEY_INSTAGRAM_LINK, intent.getStringExtra(Constants.KEY_INSTAGRAM_LINK));
						addPeopleJsonObject.put(Constants.KEY_FACEBOOK_LINK,intent.getStringExtra(Constants.KEY_FACEBOOK_LINK));
						addPeopleJsonObject.put(Constants.KEY_TWITTER_LINK,intent.getStringExtra(Constants.KEY_TWITTER_LINK));
						
						updateJsonObject.put(Constants.KEY_ADD_PEOPLE, addPeopleJsonObject);
						Log.e("broadcast sent", "add people");
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
				}
				else if(updateAction.equals(Constants.KEY_REMOVE_PEOPLE)){
					JSONObject removePeopleJsonObject = new JSONObject();
					try {
						removePeopleJsonObject.put(Constants.KEY_USER_ID, intent.getStringExtra(Constants.KEY_USER_ID));
						updateJsonObject.put(Constants.KEY_REMOVE_PEOPLE, removePeopleJsonObject);
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
				}
				
				else if(updateAction.equals(Constants.KEY_ADD_JOOKE)){
					JSONObject updateVotesJsonObject = new JSONObject();
					try {
						updateVotesJsonObject.put(Constants.KEY_SONG_MD5, intent.getStringExtra(Constants.KEY_SONG_MD5)); 
						updateVotesJsonObject.put(Constants.KEY_JOOKE_ACTIONS, Constants.JOOKE_ACTIONS_CODE_ADD);
						updateJsonObject.put(Constants.KEY_JOOKE, updateVotesJsonObject);
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
				}
				else if(updateAction.equals(Constants.KEY_NORMAL_JOOKE)){
					JSONObject updateVotesJsonObject = new JSONObject();
					try {
						updateVotesJsonObject.put(Constants.KEY_SONG_MD5, intent.getStringExtra(Constants.KEY_SONG_MD5)); 
						updateVotesJsonObject.put(Constants.KEY_JOOKE_ACTIONS, Constants.JOOKE_ACTIONS_CODE_NORMAL);
						updateJsonObject.put(Constants.KEY_JOOKE, updateVotesJsonObject);
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
				}
				else if(updateAction.equals(Constants.KEY_UPDATE_PLAYLIST)){
					JSONObject updatePlaylistJsonObject = new JSONObject();
					try {
						
						updatePlaylistJsonObject.put(Constants.KEY_SONG_MD5, intent.getStringExtra(Constants.KEY_SONG_MD5)); 
						updatePlaylistJsonObject.put(Constants.KEY_SONG_ALBUM, intent.getStringExtra(Constants.KEY_SONG_ALBUM));
						updatePlaylistJsonObject.put(Constants.KEY_SONG_ARTIST, intent.getStringExtra(Constants.KEY_SONG_ARTIST));
						updatePlaylistJsonObject.put(Constants.KEY_SONG_TITLE, intent.getStringExtra(Constants.KEY_SONG_TITLE));
						updatePlaylistJsonObject.put(Constants.KEY_SONG_DURATION, intent.getStringExtra(Constants.KEY_SONG_DURATION));
						updateJsonObject.put(Constants.KEY_UPDATE_PLAYLIST, updatePlaylistJsonObject);
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
				}
				wsServer.sendToAll(updateJsonObject.toString());
			}
		};
		registerReceiver(updatesReceiver2Broadcast, new IntentFilter(Constants.KEY_BROCAST_INTENT));
		return 2;
	
		}
	}

