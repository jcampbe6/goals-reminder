package com.goalsreminder.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter
{
	public TabsPagerAdapter(FragmentManager fm)
	{
		super(fm);
	}
	
	@Override
	public int getCount()
	{
		// get item count - equal to number of tabs
		return 2;
	}
	
	@Override
	public Fragment getItem(int index)
	{
		switch (index)
		{
			case 0:
				// goals fragment activity
				return new GoalsFragment();
			case 1:
				// reminders fragment activity
				return new RemindersFragment();
		}
		
		return null;
	}
}
