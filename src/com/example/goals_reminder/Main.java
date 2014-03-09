package com.example.goals_reminder;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class Main extends FragmentActivity implements TabListener
{
	// Variable declarations
	private ViewPager viewPager;
	private TabsPagerAdapter pagerAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Goals / Resolutions", "Reminders" };
	
	/**
	 * Initializes activity with an action bar, sets the tabs on the action bar,
	 * and sets the pager and pager adapter to change page when page swipe occurs.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		// Initialization
		actionBar = getActionBar();
		viewPager = (ViewPager) findViewById(R.id.pager);
		pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(pagerAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		/**
		 * Adds tabs to the action bar with their corresponding names.
		 */
		for (String tab_name : tabs)
		{
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}
		
		/**
		 * Sets a page change listener to change the page when page swiped.
		 */
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position)
			{
				actionBar.setSelectedNavigationItem(position);
			}
			
			//not used
			public void onPageScrollStateChanged(int arg0){}
			//not used
			public void onPageScrolled(int arg0, float arg1, int arg2){}	
		});
	}
	
	/**
	 * Shows the corresponding page when a tab is selected on action bar.
	 */
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		viewPager.setCurrentItem(tab.getPosition());
	}
	
	//not used
	public void onTabReselected(Tab tab, FragmentTransaction ft){}
	
	//not used
	public void onTabUnselected(Tab tab, FragmentTransaction ft){}
}
