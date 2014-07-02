package com.jooketechnologies.jooke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.jooketechnologies.music.Song;
import com.jooketechnologies.network.MessageConsumer;
import com.jooketechnologies.user.MySelf;
import com.jooketechnologies.user.User;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class JookeApplication extends Application {
	// @Qiuhan 
	
	// Rabbitmq server ip
	private static final String RABBITMQ_SERVER = "54.191.47.38";
	
	// Exchange name: we are going to use only one exchanger
	private static final String EXCHANGE_NAME = "Jooke Notification";
	
	// Hard-coded user id: this will be used as the routing key which identify the queue 
	private static final String userId = "2";
	
	// @End
	
	ArrayList<HashMap<String, String>> arraylist;
	public boolean isInEvent;
	private MessageConsumer mUnicastConsumer;
	public MySelf mMe;

	public static JookeApplication jookeApplication = null;
	public Location currentLocation;
	public String currentZipCode;
	public WebSocketConnection mConnection;

	@Override
	public void onCreate() {
		super.onCreate();
	}
	public void connectRabbitMQ(){
		if (mUnicastConsumer == null
				|| (mUnicastConsumer != null && !mUnicastConsumer.isConnected())) {
			
			// The only change in the application when new a new instance of the consumer: add one more parameter--userId
			mUnicastConsumer = new MessageConsumer(RABBITMQ_SERVER,
					EXCHANGE_NAME, "direct", userId);

			Log.e("unicast is connected", "is connected");
			mUnicastConsumer.connectToRabbitMQ();
			mUnicastConsumer
					.setOnReceiveMessageHandler(new MessageConsumer.OnReceiveMessageHandler() {

						public void onReceiveMessage(byte[] message) {
							
							// Received message from rabbitmq
							String msg = new String(message);
							Log.d("Jooke", "message: " + msg);
						}
				
					});

		}

		
	}
	public void startConnection(final String hostIpAddress,
			final Context context) {
		jookeApplication = this;

		Random randomPortGenerator = new Random();

		int randomPort;

		do {
			randomPort = randomPortGenerator.nextInt(Utils.MAX_PORT_NUMBER) + 1;
		} while (!Utils.isPortAvailable(randomPort));
		final String wsuri = "ws://" + hostIpAddress + ":" + 8069;
		SharedPreferenceUtils.storeWSUri(context, wsuri);
		mConnection = new WebSocketConnection();
		try {
			Log.e("before connection", "before connection");
			mConnection.connect(wsuri, new WebSocketHandler() {
				@Override
				public void onBinaryMessage(byte[] payload) {

				}

				@Override
				public void onOpen() {
					JSONObject joinJsonObject = new JSONObject();
					JSONObject valueObject = new JSONObject();
					// When join the event, the user needs to connect to the
					// host and let the server know the
					// the ip, id
					try {
						valueObject.put(Constants.KEY_USER_ID,
								SharedPreferenceUtils
										.getUserId(context));
						valueObject.put(Constants.KEY_USER_IP,
								Utils.getIPAddress(true));
						joinJsonObject.put(Constants.KEY_JOIN, valueObject);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Log.e("onOpen",SharedPreferenceUtils
							.getUserId(context)+","+Utils.getIPAddress(true));
					mConnection.sendTextMessage(joinJsonObject.toString());
				}

				// Handle all the broadcast information from the host.
				@Override
				public void onTextMessage(String jsonBroadcastString) {
					JSONObject broadcastJsonObject;
					try {
						broadcastJsonObject = new JSONObject(
								jsonBroadcastString);
						if (broadcastJsonObject
								.getJSONObject(Constants.KEY_ADD_PEOPLE) != null) {
							JSONObject addUserJsonObject = broadcastJsonObject
									.getJSONObject(Constants.KEY_ADD_PEOPLE);
							Log.e("print json string",addUserJsonObject.toString());
							User newUser = new User(
									addUserJsonObject
									.getString(Constants.KEY_USER_IP),
									addUserJsonObject
											.getString(Constants.KEY_USER_ID),
									addUserJsonObject
											.getString(Constants.KEY_FULL_NAME),
									addUserJsonObject
											.getString(Constants.KEY_PROFILE_IMG),
									addUserJsonObject
											.getString(Constants.KEY_FACEBOOK_LINK),
									addUserJsonObject
											.getString(Constants.KEY_TWITTER_LINK),
									addUserJsonObject
											.getString(Constants.KEY_INSTAGRAM_LINK));
							jookeApplication.mMe.addPublic(newUser);
							Log.e("new user broadcasted","new user broadcasted");

						} else if (broadcastJsonObject
								.getJSONObject(Constants.KEY_REMOVE_PEOPLE) != null) {
							JSONObject removeUserJsonObject = broadcastJsonObject
									.getJSONObject(Constants.KEY_REMOVE_PEOPLE);
							String userId = removeUserJsonObject
									.getString(Constants.KEY_USER_ID);

						} else if (broadcastJsonObject
								.getJSONObject(Constants.KEY_JOOKE) != null) {
							JSONObject jookeJsonObject = broadcastJsonObject
									.getJSONObject(Constants.KEY_JOOKE);
							String md5 = jookeJsonObject
									.getString(Constants.KEY_SONG_MD5);
							String jookeActions = jookeJsonObject
									.getString(Constants.KEY_JOOKE_ACTIONS);
							if (jookeActions.equals(String
									.valueOf(Constants.JOOKE_ACTIONS_CODE_ADD))) {

							} else if (jookeActions.equals(String
									.valueOf(Constants.JOOKE_ACTIONS_CODE_NORMAL))) {

							}
						}
						else if(broadcastJsonObject.getJSONObject(Constants.KEY_UPDATE_PLAYLIST)!=null){
								JSONObject updatePlaylistJsonObject = broadcastJsonObject
										.getJSONObject(Constants.KEY_UPDATE_PLAYLIST);
								try {
									Song newSong = new Song(updatePlaylistJsonObject.getString(Constants.KEY_SONG_ALBUM),
											updatePlaylistJsonObject.getString(Constants.KEY_SONG_ARTIST),updatePlaylistJsonObject.getString(Constants.KEY_SONG_TITLE),updatePlaylistJsonObject.getString(Constants.KEY_SONG_DURATION),null);
									
									
								} catch (JSONException e) {
									
									e.printStackTrace();
								}
						}
					} catch (JSONException e) {
						
						e.printStackTrace();
					}

				}

				@Override
				public void onClose(int code, String reason) {
				}
			});
		} catch (WebSocketException e) {

			Log.d("", e.toString());
		}
	}

	public void setIsInEvent(boolean isInEvent) {
		this.isInEvent = isInEvent;
	}

	public boolean isInEvent() {
		return isInEvent;
	}
}