package com.example.goals_reminder;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class Main extends FragmentActivity implements ActionBar.TabListener
{
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Resolutions / Goals", "Reminders" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Adding Tabs
		for (String tab_name : tabs)
		{
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}
		
		/**
		 * on swiping the viewpager make respective tab selected
		 */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position)
			{
				actionBar.setSelectedNavigationItem(position);
			}

			public void onPageScrollStateChanged(int arg0){}
					
			public void onPageScrolled(int arg0, float arg1, int arg2){}	
		});
	}
	
	public void onTabReselected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction)
	{
	}
	
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		// show respected fragment view when tab selected
		viewPager.setCurrentItem(tab.getPosition());
	}
	
	public void onTabUnselected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction)
	{
	}
}
