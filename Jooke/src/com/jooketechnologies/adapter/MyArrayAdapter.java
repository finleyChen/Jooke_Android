package com.jooketechnologies.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jooketechnologies.jooke.ArrayAdapterActivity;
import com.jooketechnologies.jooke.R;
import com.manuelpeinado.multichoiceadapter.extras.actionbarcompat.MultiChoiceArrayAdapter;

@SuppressLint("ViewConstructor")
public class MyArrayAdapter extends MultiChoiceArrayAdapter<String> {
	private Context mContext;
	protected static final String TAG = MyArrayAdapter.class.getSimpleName();
	private boolean cancelClicked = false;
	
	
	public MyArrayAdapter(Bundle savedInstanceState, Context context,
			ArrayList<String> songs) {
		super(savedInstanceState, context,
				R.layout.mca__simple_list_item_checkable_1, android.R.id.text1,
				songs);
		mContext = context;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.my_action_mode, menu);
		return true;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		Log.e("item.getItemId()", item.getItemId() + "");
		if (item.getItemId() == R.id.menu_cancel) {
			((ArrayAdapterActivity) mContext).deselectAll();
			cancelClicked = true;
			Log.e("aa", "aa" + cancelClicked);
			return true;
		}

		return false;
	}


	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {

	}

	@Override
	protected View getViewImpl(int position, View convertView, ViewGroup parent) {
		View view = super.getViewImpl(position, convertView, parent);
		view.setBackgroundResource(R.drawable.custom_list_item_background);
		return view;
	}
}
