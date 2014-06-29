package com.jooketechnologies.network;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jooketechnologies.jooke.Constants;
import com.jooketechnologies.jooke.JookeApplication;
import com.jooketechnologies.music.Song;
import com.jooketechnologies.user.User;

public class JookeWebSocketServer extends WebSocketServer {
	private static int counter = 0;
	Context context;
	private JookeApplication jookeApplication;
	private JookeWebSocketServer wsServer;
	public JookeWebSocketServer(int paramInt, Draft paramDraft,
			Context paramContext, JookeApplication jookeApplication) {

		super(new InetSocketAddress(paramInt), Collections
				.singletonList(paramDraft));
		this.jookeApplication = jookeApplication;
		this.context = paramContext;
		wsServer = this;
	}

	public JookeWebSocketServer(InetSocketAddress paramInetSocketAddress,
			Draft paramDraft, Context paramContext) {
		super(paramInetSocketAddress, Collections.singletonList(paramDraft));
		this.context = paramContext;
	}

	public int getCounter() {
		return counter;
	}

	public void onClose(WebSocket paramWebSocket, int paramInt,
			String paramString, boolean paramBoolean) {

		this.start();
	}

	public void onError(WebSocket paramWebSocket, Exception paramException) {
		Log.e("onError", "onError" + paramException);
	}

	// Handle individual public user requests. 
	// Four actions: 
	// Join. 
	// Jooke(Normal or Add). 
	// Leave. 
	// Add songs from public local phones.
	// These three actions will be broadcasted to all the users as well. 
	// NOT TESTED. 
	public void onMessage(WebSocket paramWebSocket, String incomingMessage) {
		Log.e("onMessage", "onMessage" + incomingMessage);
		try {
			JSONObject incomingJsonObject = new JSONObject(incomingMessage);
			JSONObject updateJsonObject = new JSONObject();
			// JOIN
			if (incomingJsonObject.getJSONObject(Constants.KEY_JOIN) != null) {
			
				JSONObject joinJsonObject = incomingJsonObject
						.getJSONObject(Constants.KEY_JOIN);

				String userId = joinJsonObject.getString(Constants.KEY_USER_ID);
				String userIp = joinJsonObject.getString(Constants.KEY_USER_IP);
				Log.e("joined userid",userId);
				Log.e("joined userIp",userIp);
				
				User newUser = ServerUtilities.getProfile(userId, userIp);
				// ADD to the user list and broadcast to everyone.
				if (jookeApplication.mMe.addPublic(newUser)) {
					Intent addUserIntent = new Intent(
							Constants.KEY_BROCAST_INTENT);
					addUserIntent.setAction(Constants.KEY_ADD_PEOPLE);
					addUserIntent.putExtra(Constants.KEY_USER_ID, userId);
					addUserIntent.putExtra(Constants.KEY_FULL_NAME,
							newUser.userName);
					addUserIntent.putExtra(Constants.KEY_PROFILE_IMG,
							newUser.userProfileImgUrl);
					addUserIntent.putExtra(Constants.KEY_INSTAGRAM_LINK,
							newUser.instagramUrl);
					addUserIntent.putExtra(Constants.KEY_TWITTER_LINK,
							newUser.twitterUrl);
					addUserIntent.putExtra(Constants.KEY_FACEBOOK_LINK,
							newUser.facebookUrl);
					context.sendBroadcast(addUserIntent);
					
					JSONObject addPeopleJsonObject = new JSONObject();
					try {
						addPeopleJsonObject.put(Constants.KEY_USER_ID, userId);
						addPeopleJsonObject.put(Constants.KEY_FULL_NAME, newUser.userName);
						addPeopleJsonObject.put(Constants.KEY_PROFILE_IMG, newUser.userProfileImgUrl);
						addPeopleJsonObject.put(Constants.KEY_USER_ID, userId);
						addPeopleJsonObject.put(Constants.KEY_INSTAGRAM_LINK, newUser.instagramUrl);
						addPeopleJsonObject.put(Constants.KEY_FACEBOOK_LINK,newUser.facebookUrl);
						addPeopleJsonObject.put(Constants.KEY_TWITTER_LINK,newUser.twitterUrl);
						
						updateJsonObject.put(Constants.KEY_ADD_PEOPLE, addPeopleJsonObject);
						Log.e("broadcast sent", "add people");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// LEAVE
			else if (incomingJsonObject
					.getJSONObject(Constants.KEY_LEAVE_EVENT) != null) {
				JSONObject leaveJsonObject = incomingJsonObject
						.getJSONObject(Constants.KEY_LEAVE_EVENT);

				String userId = leaveJsonObject.getString(Constants.KEY_USER_ID);
				// REMOVE a user from the user list and broadcast.
				if (jookeApplication.mMe.removeUser(userId)) {
					Intent removeUserIntent = new Intent(
							Constants.KEY_BROCAST_INTENT);
					removeUserIntent.setAction(Constants.KEY_REMOVE_PEOPLE);
					removeUserIntent.putExtra(Constants.KEY_USER_ID, userId);
					context.sendBroadcast(removeUserIntent);
				}
			}
			// JOOKE
			else if (incomingJsonObject.getJSONObject(Constants.KEY_JOOKE) != null) {
				JSONObject jookeJsonObject = incomingJsonObject
						.getJSONObject(Constants.KEY_JOOKE);

				String md5 = jookeJsonObject.getString(Constants.KEY_SONG_MD5);
				String addOrNormal = jookeJsonObject
						.getString(Constants.KEY_JOOKE_ACTIONS);
				if (jookeApplication.mMe.updateJookeResult(md5)) {
					Intent jookeSongIntent = new Intent(
							Constants.KEY_BROCAST_INTENT);
					// ADD a song to the upcoming list from the playlist and jooke it. 
					// and broadcast this information.
					if (addOrNormal.equals(String
							.valueOf(Constants.JOOKE_ACTIONS_CODE_ADD))) {
						jookeSongIntent.setAction(Constants.KEY_ADD_JOOKE);
						jookeSongIntent.putExtra(Constants.KEY_SONG_MD5, md5);
						context.sendBroadcast(jookeSongIntent);
					}
					// VOTE or SELECT a song from the upcoming list. 
					// and broadcast this information. 
					if (addOrNormal.equals(String
							.valueOf(Constants.JOOKE_ACTIONS_CODE_NORMAL))) {
						jookeSongIntent.setAction(Constants.KEY_NORMAL_JOOKE);
						jookeSongIntent.putExtra(Constants.KEY_SONG_MD5, md5);
						context.sendBroadcast(jookeSongIntent);
					}

				}

			}
			else if (incomingJsonObject.getJSONObject(Constants.KEY_ADD_STREAM) != null) {
				JSONObject addStreamJsonObject = incomingJsonObject
						.getJSONObject(Constants.KEY_ADD_STREAM);

				String md5 = addStreamJsonObject.getString(Constants.KEY_SONG_MD5);

				String album = addStreamJsonObject.getString(Constants.KEY_SONG_ALBUM);
				String artist = addStreamJsonObject.getString(Constants.KEY_SONG_ARTIST);
				String title = addStreamJsonObject.getString(Constants.KEY_SONG_TITLE);
				String duration =  addStreamJsonObject.getString(Constants.KEY_SONG_DURATION);
				String path = addStreamJsonObject.getString(Constants.KEY_SONG_PATH);
				
				Song newSong = new Song(album, artist,title,duration,path);
				
				
				if (jookeApplication.mMe.updatePlaylist(newSong)) {
					Intent updatePlaylistIntent = new Intent(
							Constants.KEY_BROCAST_INTENT);
					updatePlaylistIntent.setAction(Constants.KEY_UPDATE_PLAYLIST);
					updatePlaylistIntent.putExtra(Constants.KEY_SONG_MD5, md5);
					updatePlaylistIntent.putExtra(Constants.KEY_SONG_ALBUM, album);
					updatePlaylistIntent.putExtra(Constants.KEY_SONG_DURATION, duration);
					updatePlaylistIntent.putExtra(Constants.KEY_SONG_PATH, path);
					context.sendBroadcast(updatePlaylistIntent);
				}

			}

		} catch (JSONException e) {
			Log.e("userId userIp", e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onMessage(WebSocket paramWebSocket, ByteBuffer paramByteBuffer) {
		Log.e("onMessage", "onMessage");
	}

	public void onOpen(WebSocket paramWebSocket,
			ClientHandshake paramClientHandshake) {
		Log.e("onOpen", "onOpen" + paramClientHandshake.toString());
	}

	public void onWebsocketMessageFragment(WebSocket paramWebSocket,
			Framedata paramFramedata) {
	}

	// Broadcast to all the users.
	// We broadcast the list updates (people and song) every two seconds.
	public void sendToAll(String listUpdatesJsonString) {

		Collection<WebSocket> localCollection = connections();
		try {
			Iterator<WebSocket> localIterator = localCollection.iterator();
			if (localIterator.hasNext())
				((WebSocket) localIterator.next()).send(listUpdatesJsonString);
			return;
		} finally {
		}

	}
}
