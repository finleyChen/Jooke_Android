package com.jooketechnologies.user;

import java.util.ArrayList;

import com.jooketechnologies.music.Song;



public class SongList {
	public ArrayList<Song> songList= new ArrayList<Song>();
	public boolean AllowVoting;
	public void addAll(ArrayList<Song> songList){
		songList.addAll(songList);
	}

}