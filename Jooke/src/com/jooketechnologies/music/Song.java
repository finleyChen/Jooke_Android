package com.jooketechnologies.music;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Song implements Comparable<Song>{
	public String album;
	public String artist;
	public String title;
	public String duration;
	public String md5;
	public Integer vote;
	// if upcoming, this song should be in the upcoming list. 
	// if not upcoming, this song should be in the play list. 
	public boolean upcoming;
	//It could be a local file path or a url to stream. 
	public String filePath; 
	
	private String generateMD5(String combination){
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(combination.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while(hashtext.length() < 32 ){
			  hashtext = "0"+hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public Song(String albumStr, String artistStr, String titleStr, String durationStr, String path) {
		album = albumStr;
		artist = artistStr;
		title = titleStr;
		duration = durationStr;
		filePath = path;
		md5 = generateMD5(album+","+artist+","+title+","+duration);
	}
	
	public String getMD5(){
		return md5;
	}
	@Override
	public int compareTo(Song song) {
		// TODO Auto-generated method stub
		return vote.compareTo(song.vote);
	}

}