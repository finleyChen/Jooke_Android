package com.jooketechnologies.database;

import java.util.Comparator;

import com.jooketechnologies.music.Artist;

public class ArtistComparator implements Comparator<Artist> {
    @Override
    public int compare(Artist o1, Artist o2) {
        return o1.getNameOfArtist().compareTo(o2.getNameOfArtist());
    }
}