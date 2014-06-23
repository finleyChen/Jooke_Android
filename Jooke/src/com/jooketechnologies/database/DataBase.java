package com.jooketechnologies.database;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;


public class DataBase
{
  Context context;
  private DatabaseHelper dbh = null;
  
  public DataBase(Context paramContext)
  {
    this.context = paramContext;
    this.dbh = new DatabaseHelper(paramContext);
  }
  
  public boolean existsOneSong()
  {
    return this.dbh.getWritableDatabase().rawQuery("SELECT * FROM songs WHERE 1 LIMIT 1", null).moveToFirst();
  }
  
  public ArrayList<String> getDesactiveSongs()
  {
    Cursor localCursor = this.dbh.getWritableDatabase().rawQuery("SELECT * FROM songs WHERE active = 0", null);
    ArrayList <String> localArrayList = new ArrayList<String>();
    if (localCursor.moveToFirst()) {
      do
      {
        localArrayList.add(localCursor.getString(localCursor.getColumnIndex("key")));
      } while (localCursor.moveToNext());
    }
    return localArrayList;
  }
  
  public SQLiteCursorLoader getForList(String query, String sortType, String sortOrder)
  {
	  Log.e("getForList","get"+query+","+sortType+","+sortOrder);
	if(sortOrder.toUpperCase().equals("DESC") || sortOrder.toUpperCase().equals("ASC")){
		String orderString;
		String likeString;
		
		if(sortType.toUpperCase().equals("ALBUM"))
			orderString = "ORDER BY album COLLATE NOCASE " 
	  				+ sortOrder.toUpperCase() + ", track, title COLLATE NOCASE " 
	  				+ sortOrder.toUpperCase();
	  	
		else if(sortType.toUpperCase().equals("ARTIST"))
	  		orderString = "ORDER BY artist COLLATE NOCASE " 
	  				+ sortOrder.toUpperCase() + ", album COLLATE NOCASE " 
	  				+ sortOrder.toUpperCase() + ", track, title COLLATE NOCASE " 
	  				+ sortOrder.toUpperCase();
	  	
		else if (sortType.toUpperCase().equals("TITLE"))
	  		orderString = "ORDER BY title " + sortOrder.toUpperCase();
		else {
			Log.e("nulll","nulllb");
			return null;
		}
			
	  	
	  	if(query!=null && !query.isEmpty()){
	  		likeString = "AND (artist LIKE '%" + query + "%' OR title LIKE '%" + query + "%' OR album LIKE '%" + query + "%') ";
	  		String queryString;
		  	queryString = "SELECT id AS _id, title, artist, album FROM songs WHERE 1 " + likeString + " " + orderString;
		  	Log.e("looks ok","ok");
		  	return new SQLiteCursorLoader(context, dbh, queryString, null);
	  	}
	  	else{
	  		Log.e("looks ok","ok1");
	  		String queryString;
	  		queryString = "SELECT id AS _id, title, artist, album FROM songs "  + orderString;
	  		return new SQLiteCursorLoader(context, dbh, queryString, null);
	  	}
	  		
	  	
	  	
	  	
	}
	else{
		Log.e("nullal","nullla");
		return null;
	}
  	
  	
  }

  public Cursor getSongById(long id)
  {
    String str = "SELECT * FROM songs WHERE id = '" + id + "'";
    Cursor localCursor = this.dbh.getWritableDatabase().rawQuery(str, null);
    if (localCursor.moveToFirst()) {
      return localCursor;
    }
    return null;
  }
  
  public Cursor getSongByKey(String key)
  {
    String query = "SELECT * FROM songs WHERE key = '" + key + "'";
    Cursor cursor = this.dbh.getWritableDatabase().rawQuery(query, null);
    if (cursor.moveToFirst()) {
      return cursor;
    }
    return null;
  }
  
  public long getSongId(String paramString)
  {
    Cursor localCursor = getSongByKey(paramString);
    if (localCursor != null) {
      return localCursor.getLong(localCursor.getColumnIndex("id"));
    }
    return 0L;
  }
  
  public String getSongPath(String paramString)
  {
    Cursor localCursor = getSongByKey(paramString);
    if (localCursor != null) {
      return localCursor.getString(localCursor.getColumnIndex("song_path"));
    }
    return null;
  }
  
  public JSONArray getSongsJSON()
    throws JSONException
  {
    Cursor localCursor = this.dbh.getWritableDatabase().rawQuery("SELECT * FROM songs", null);
    JSONArray localJSONArray = new JSONArray();
    if (localCursor.moveToFirst()) {
      do
      {
        JSONObject localJSONObject = new JSONObject();
        localJSONObject.put("id", localCursor.getString(localCursor.getColumnIndex("key")));
        localJSONObject.put("title", localCursor.getString(localCursor.getColumnIndex("title")));
        localJSONObject.put("duration", localCursor.getString(localCursor.getColumnIndex("duration")));
        localJSONObject.put("artist", localCursor.getString(localCursor.getColumnIndex("artist")));
        localJSONObject.put("album", localCursor.getString(localCursor.getColumnIndex("album")));
        localJSONObject.put("track", localCursor.getString(localCursor.getColumnIndex("track")));
        localJSONObject.put("active", localCursor.getInt(localCursor.getColumnIndex("active")));
        localJSONArray.put(localJSONObject);
      } while (localCursor.moveToNext());
    }
    return localJSONArray;
  }
  
  public long saveSong(String key, String song_path, String title, String duration, String artist, String album, String track, int active)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("key",key);
    localContentValues.put("song_path", song_path);
    localContentValues.put("title", title);
    localContentValues.put("duration", duration);
    localContentValues.put("artist", artist);
    localContentValues.put("album", album);
    localContentValues.put("track", track);
    localContentValues.put("active", Integer.valueOf(active));
    
    return this.dbh.getWritableDatabase().insert("songs", null, localContentValues);
  }
  
  public int setActiveSong(String paramString, int paramInt)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("active", Integer.valueOf(paramInt));
    return this.dbh.getWritableDatabase().update("songs", localContentValues, "key = '" + paramString + "'", null);
  }
  
  public int toggleActiveSong(String key)
  {
    Cursor cursor = getSongByKey(key);
    int k = cursor.getInt(cursor.getColumnIndex("active"));
    return setActiveSong(key,k);
  }
  
  public void truncateSongs()
  {
    this.dbh.getWritableDatabase().execSQL("DELETE FROM songs WHERE 1");
  }
}
