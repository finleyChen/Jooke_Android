package com.jooketechnologies.database;

import java.util.Comparator;

import com.jooketechnologies.music.Song;

public class SongComparator implements Comparator<Song> {
    @Override
    public int compare(Song o1, Song o2) {
        return o1.title.compareTo(o2.title);
    }
}