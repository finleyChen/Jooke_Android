package com.jooketechnologies.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jooketechnologies.fragment.CreateEventFragment;
import com.jooketechnologies.fragment.DiscoverFragment;

public class MainPageAdapter extends FragmentPagerAdapter {

	private int mCount = 2;

	public MainPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
    public Fragment getItem(int position) {
    	switch(position){
    	case 0:
    		return new CreateEventFragment();
    	case 1:
    		return new DiscoverFragment();
    	}
        return null;
    }

	@Override
	public int getCount() {
		return mCount;
	}


	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
	
}