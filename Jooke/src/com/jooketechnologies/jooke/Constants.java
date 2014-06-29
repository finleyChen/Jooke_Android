package com.jooketechnologies.jooke;

import java.util.Locale;

public class Constants {



	static public final String KEY_HOME_FEED_SUBJECT1 = "subject1";
	static public final String KEY_HOME_FEED_SUBJECT2 = "subject2";
	static public final String KEY_HOME_FEED_ACTION = "action";
	static public final String KEY_HOME_SUBJECT1_PROFILE_IMG = "subject1_img";
	static public final String KEY_HOME_PASS_TIME = "pass_time";
	static public final String KEY_HOME_FEED_SUBJECT2_CATEORY = "subject2_category";


	static public final String KEY_DISCOVER_EVENT_NAME = "discover_eventname";
	static public final String KEY_DISCOVER_HOST_NAME = "discover_hostname";
	static public final String KEY_DISCOVER_EVENT_CATEGORY = "discover_eventcategory";

	static public final int SIGNUP_TYPE_FACEBOOK = 1;
	static public final int SIGNUP_TYPE_TWITTER = 2;
	static public final int SIGNUP_TYPE_NORMAL = 0;

	public static final String ACCESS_KEY_ID = "CHANGETHIS";
	public static final String SECRET_KEY = "CHANGETHIS";

	static public final String KEY_EVENT_STATUS = "eventstatus";
	static public final String KEY_EVENT_CATEGORY = "eventcategory";

	static public final int TEXT_SIZE_SPLASH_LOGO = 80;
	static public final int TEXT_SIZE_ACTION_BAR_TITLE = 60;
	static public final int TEXT_SIZE_ACTION_BAR_LOGO = 80;
	static public final int TEXT_SIZE_ACTION_BAR_LOGO_IN_EVENT = 60;

	static public final String KEY_PROFILE_NAME = "profilename";

	public static final String CHAT_SERVER_URL = "http://jookee-env.elasticbeanstalk.com";
	//public static final String CHAT_SERVER_URL = "http://10.31.54.52:8080/RichServer";
	public static final String CHAT_SERVER_GET_FRIEND_URL = CHAT_SERVER_URL
			+ "/getFriendsLists.do";

	public static final int SIGNUP_ACTION_CODE = 0;
	public static final int LOGIN_ACTION_CODE = 1;
	public static final int CREATE_EVENT_MSG_ACTION_CODE = 2;
	public static final int JOIN_EVENT_ACTION_CODE = 3;
	public static final int KICK_OUT_ACTION_CODE = 4;
	public static final int LEAVE_ACTION_CODE = 5;
	public static final int SAVE_PROFILE_ACTION_CODE = 6;
	public static final int FORGET_PASSWORD_ACTION_CODE = 7;
	public static final int HEARTBEAT_ACTION_CODE = 8;
	public static final int PULLINFO_ACTION_CODE = 9;
	public static final int UPDATE_PEOPLE_ACTION_CODE = 10;
	public static final int GET_PROFILE_ACTION_CODE = 11;
	public static final int DISCOVER_ACTION_CODE = 12;
	public static final int ASYNCTASK_SUCCESS_CODE = 13;
	public static final int ASYNCTASK_FAILURE_CODE = 14;
	public static final int GET_HOST_IP_ACTION_CODE =15;
	public static final int JOOKE_ACTIONS_CODE_ADD = 16;
	public static final int JOOKE_ACTIONS_CODE_NORMAL = 17;
	
	public static final String SIGNUP_ACTION_NAME = "/signup";
	public static final String LOGIN_ACTION_NAME = "/login";
	public static final String CREATE_EVENT_ACTION_NAME = "/create_event";
	public static final String JOIN_EVENT_ACTION_NAME = "/join_event";
	public static final String KICK_OUT_ACTION_NAME = "/kick_out";
	public static final String LEAVE_ACTION_NAME = "/leave";
	public static final String SAVE_PROFILE_ACTION_NAME = "/edit_profile";
	public static final String FORGET_PASSWORD_ACTION_NAME = "/forget_password";
	public static final String HEARTBEAT_ACTION_NAME = "/heart_beat";
	public static final String PULLINFO_ACTION_NAME = "/pull_info";
	public static final String UPDATE_PEOPLE_ACTION_NAME = "/update_people";
	public static final String GET_PROFILE_ACTION_NAME = "/get_profile";
	public static final String DISCOVER_ACTION_NAME = "/discover";
	public static final String GET_HOST_IP_ACTION_NAME = "/host_ip";

	public static final String KEY_EVENT_NAME = "event_name";
	public static final String KEY_EVENT_MODE = "event_mode";
	public static final String KEY_EVENT_ZIP_CODE = "event_zip_code";
	public static final String KEY_EVENT_TIME = "event_time";
	public static final String KEY_HOST_ID = "host_id";
	public static final String KEY_HOST_IP = "host_ip";
	public static final String KEY_JOIN = "join";
	public static final String KEY_JOOKE = "jooke_song";
	public static final String KEY_USER_ID = "userid";
	public static final String KEY_USER_IP = "user_ip";
	public static final String KEY_JOIN_TIME = "join_time";
	public static final String KEY_FULL_NAME = "fullname";
	public static final String KEY_FACEBOOK_LINK = "facebook_link";
	public static final String KEY_TWITTER_LINK = "twitter_link";
	public static final String KEY_INSTAGRAM_LINK = "instagram_link";
	public static final String KEY_PROFILE_IMG = "profile_img";
	public static final String KEY_EVENT_ID = "event_id";
	public static final String KEY_LAT = "lat";
	public static final String KEY_LON = "lon";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_IS_HOST = "is_host";
	public static final String KEY_HOST_NAME = "host_name";
	public static final String KEY_HOST_PROFILE_IMG = "host_profile_img";
	public static final String KEY_THIRD_ID = "thirdparty_id";
	public static final String KEY_SIGNUP_TYPE = "signup_type";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_ALLOW_ADDSONGS = "allow_add";
	public static final String KEY_SONG_ALBUM = "album";
	public static final String KEY_SONG_ARTIST ="artist";
	public static final String KEY_SONG_TITLE ="title";
	public static final String KEY_SONG_DURATION ="duration";
	public static final String KEY_SONG_MD5 = "md5";
	public static final String KEY_SONG_PATH = "path";
	public static final String KEY_LEAVE_EVENT = "leave_event";
	
	public static final String KEY_UPDATE_LIST = "update_list";
	public static final String KEY_UPDATE_ADD_SONG = "add_songs";
	public static final String KEY_UPDATE_REMOVE_SONG = "remove_song";
	public static final String KEY_BROCAST_INTENT = "broadcast_intent";
	public static final String KEY_NORMAL_JOOKE  = "normal_jooke"; 
	public static final String KEY_UPDATE_PLAYLIST = "update_playlist";
	public static final String KEY_ADD_JOOKE 	= "add_jooke";
	public static final String KEY_ADD_PEOPLE = "add_people";
	public static final String KEY_ADD_STREAM = "add_stream";
	public static final String KEY_REMOVE_PEOPLE = "remove_people";
	public static final String KEY_JOOKE_ACTIONS = "jooke_actions";
	
	public static final String FACEBOOK_APP_ID = "474386306039917";
	public static final String APP_NAMESPACE = "jooketechnologies";

	public static final String PICTURE_BUCKET = "jooke-storage";
	public static final String PICTURE_NAME = "profile/uid.png";

	public static final String KEY_LEADERBOARD_TITLE = "key_leaderboard_title";
	public static final String KEY_LEADERBOARD_ARTIST = "key_leaderboard_artist";
	public static final String KEY_LEADERBOARD_DURATION = "key_leaderboard_duration";
	public static final String KEY_LEADERBOARD_ALBUM_IMG = "key_leaderboard_album";
	
	public static String getPictureBucket() {
		return ("my-unique-name" + ACCESS_KEY_ID + PICTURE_BUCKET)
				.toLowerCase(Locale.US);
	}
}
