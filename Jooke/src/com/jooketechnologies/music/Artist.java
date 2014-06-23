package com.jooketechnologies.music;

import java.util.ArrayList;

import android.graphics.Bitmap;


public class Artist {
	public String name;
	public int numberOfSongs;
	public Bitmap albumbBitmap;
	
	public ArrayList<Song> mSongList = new ArrayList<Song>();; // Location list
	public String getNameOfArtist(){
		return name;
	}
	public Bitmap getAlbumBitmap(){
		return albumbBitmap;
	}
	public Artist(String artistName, Bitmap bitmap) {
		
		name = artistName;
		albumbBitmap = bitmap;
		
	}
	public void insertSong(Song song){
		mSongList.add(song);
	}
	public ArrayList<Song> getSongList(){
		return mSongList;
	}
	public int getNumOfSongs(){
		numberOfSongs = mSongList.size();
		return numberOfSongs;
	}
}