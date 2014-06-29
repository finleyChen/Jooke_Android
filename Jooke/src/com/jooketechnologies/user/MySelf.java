package com.jooketechnologies.user;

import java.io.IOException;
import java.util.Collections;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.jooketechnologies.event.Event;
import com.jooketechnologies.music.Song;

public class MySelf extends User {
	public boolean isHost;
	public Event event;
	MediaPlayer mPlayer = null;

	public MySelf(String userIp, String userId) {
		super(userIp, userId);
	}

	// Public actions.
	public void addSongToPlaylistPublic(Song song) {

	}

	public void leaveEvent(String eventId) {

	}

	// Host actions.
	// play the next random song when the upcoming song list is empty.
	public void playRandomSongFromPlaylist() {

	}

	// play the next song in the upcoming list so that I can skip the current
	// song.
	// skip to the next song so that the first song in the upcoming list
	// will be played after stop the current playing song.
	public void skipToNextSong() {
		stopCurrentSong();
		playTheNextSong();
	}

	public boolean removeUser(String userId) {

		for (User user : Event.mUserList) {
			if (user.userId.equals(userId)) {
				Event.mUserList.remove(user);
				Collections.sort(Event.mUserList);
				return true;
			}

		}
		return false;

	}

	private void stopCurrentSong() {
		mPlayer.stop();
		mPlayer = null;
	}

	
	// in the event, user can still choose to add songs into the playlist. 
	// the playlist can be updated by the host 
	// or can be updated by the public to stream songs. 
	public boolean updatePlaylist(Song newSong) {
		Event.mSongList.add(newSong);
		return true;
	}
	
	// depends on what mode you are in,
	// you can update the song list by first come first serve,
	// or the most voted result.
	public boolean updateJookeResult(String md5) {
		if (Boolean.valueOf(event.eventMode)) {
			for (Song song : Event.mSongList) {
				if (song.md5.equals(md5)) {
					song.vote = song.vote + 1;
				}
			}
			Collections.sort(Event.mSongList);
			return true;
		} else {
			// don't use the vote of each song here.
		}
		return false;
	}

	// play the next song without any interaction.
	// it will be called whenever the current song stops playing.
	// assume this song is local now.
	// it will return the new song so that
	// the now playing tab could be inflated it properly.
	public Song playTheNextSong() {
		// 0. kickout the current song.
		Event.mSongList.remove(0);
		// 1. sort it by the votes.
		Collections.sort(Event.mSongList);
		// 2. get the song with the most vote.
		Song songToPlay = Event.mSongList.get(0);
		mPlayer = new MediaPlayer();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mPlayer.setDataSource(songToPlay.filePath);
			mPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPlayer.start();
		return songToPlay;

	}

	public void endEvent(String eventId) {

	}

	public boolean addPublic(User newUser) {
		Event.mUserList.add(newUser);
		return true;
	}

	public void addSongToPlaylistHost(Song song) {

	}

	// Shared Operations.
	public void setEvent(Event event) {
		this.event = event;
		this.event.initUserList(this);
	}
}