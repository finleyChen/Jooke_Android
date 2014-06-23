package com.jooketechnologies.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "jooke";
	public static final String TITLE = "title";
	public static final String ARTIST = "artist";
	public static final String ALBUM = "album";
	

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE constants (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, value REAL);");
		db.execSQL("CREATE TABLE songs (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, key TEXT NOT NULL, song_path TEXT NOT NULL, title TEXT, duration TEXT, artist TEXT, album TEXT, track TEXT)");
		db.execSQL("CREATE UNIQUE INDEX key ON songs(key ASC)");
		db.execSQL("ALTER TABLE songs ADD COLUMN active INTEGER NOT NULL DEFAULT '1'");
		db.execSQL("CREATE TABLE IF NOT EXISTS playlists (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL)");
		db.execSQL("CREATE TABLE IF NOT EXISTS playlists_songs (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playlist_id INTEGER NOT NULL, song_key TEXT NOT NULL)");
		db.execSQL("CREATE TABLE IF NOT EXISTS playlists_songs (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playlist_id INTEGER NOT NULL, song_key TEXT NOT NULL)");
		db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS song_key ON playlists_songs(song_key ASC)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		android.util.Log.w("Constants",
				"Upgrading database, which will destroy all old data");
		if (newVersion == 2) {
			db.execSQL("ALTER TABLE songs ADD COLUMN active INTEGER NOT NULL DEFAULT '1'");
			db.execSQL("CREATE TABLE IF NOT EXISTS playlists (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL)");
			db.execSQL("CREATE TABLE IF NOT EXISTS playlists_songs (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playlist_id INTEGER NOT NULL, song_key TEXT NOT NULL)");
			db.execSQL("CREATE TABLE IF NOT EXISTS playlists_songs (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playlist_id INTEGER NOT NULL, song_key TEXT NOT NULL)");
			db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS song_key ON playlists_songs(song_key ASC)");
		}
	}
}