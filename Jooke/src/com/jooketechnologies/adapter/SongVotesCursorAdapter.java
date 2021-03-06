package com.jooketechnologies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.jooketechnologies.holder.SongVotesListViewHolder;

public class SongVotesCursorAdapter extends SimpleCursorAdapter {
	public int layout;
	public Cursor cursor;
	public Context mContext;

	public SongVotesCursorAdapter(Context context, int layoutParam,
			Cursor cursorParam, String[] from, int[] to, int arg7) {

		super(context, layoutParam, cursorParam, from, to, 0);
		mContext = context;
		layout = layoutParam;

		cursor = cursorParam;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		View localView = paramView;
		if (localView == null)
			localView = LayoutInflater.from(mContext).inflate(layout,
					paramViewGroup, false);
		SongVotesListViewHolder localSongListViewHolder = (SongVotesListViewHolder) localView
				.getTag();
		if (localSongListViewHolder == null) {
//			localSongListViewHolder = new SongVotesListViewHolder();
//			localSongListViewHolder.activeMark = localView
//					.findViewById(R.id.active_mark);
//			localSongListViewHolder.title = ((TextView) localView
//					.findViewById(R.id.title));
//			localSongListViewHolder.artist = ((TextView) localView
//					.findViewById(R.id.artist));
//			localSongListViewHolder.album = ((TextView) localView
//					.findViewById(R.id.album));
//			localSongListViewHolder.votes = ((TextView) localView
//					.findViewById(R.id.votes));

			localView.setTag(localSongListViewHolder);
		}
		cursor.moveToPosition(paramInt);

		localSongListViewHolder.title.setText(cursor.getString(1));
		localSongListViewHolder.artist.setText(cursor.getString(2));
		localSongListViewHolder.album.setText(cursor.getString(3));
		if(!cursor.getString(4).equals("0")){
			localSongListViewHolder.votes.setText(cursor.getString(4));
		}
		

		localSongListViewHolder.activeMark.setVisibility(4);
		return localView;
	}

	public Cursor swapCursor(Cursor paramCursor) {
		cursor = paramCursor;
		return super.swapCursor(paramCursor);
	}
}
