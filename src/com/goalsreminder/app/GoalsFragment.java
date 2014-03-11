package com.goalsreminder.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class GoalsFragment extends ListFragment
{
	// variable declarations
	private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
	private Button deleteButton;
	private Button newGoalButton;
	private GoalsDbAdapter databaseHelper;
	private ListView goalsListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		
		View rootView = inflater.inflate(R.layout.goals_fragment, container, false);
		databaseHelper = new GoalsDbAdapter(getActivity());
		databaseHelper.open();
		fillListData();
		
		newGoalButton = (Button) rootView.findViewById(R.id.newGoalButton);
		newGoalButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				createGoal();
			}
		});
		
		deleteButton = (Button) rootView.findViewById(R.id.deleteGoalsButton);
		deleteButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				// Checks for an empty goals list and alerts user if list is empty
				if (goalsListView.getCount() == 0)
					Toast.makeText(getActivity(), "Nothing to delete", Toast.LENGTH_SHORT).show();
				
				/*
				 *  If the list is not empty, then it checks to see if any list items are selected, and deletes
				 *  any selected items.
				 */
				else if (goalsListView.getCheckedItemIds().length != 0)
				{
					for (long id: goalsListView.getCheckedItemIds())
					{
						databaseHelper.deleteGoal(id);
					}
					fillListData();
				}
				
				// If the list is not empty and none are selected, alerts user that nothing is selected
				else
					Toast.makeText(getActivity(), "Nothing selected", Toast.LENGTH_SHORT).show();
			}
		});
		
		return rootView;
	}
	
	/**
	 * called when activity is first created
	 */
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		goalsListView = getListView();
		
		// Sets a long click listener on the list view to open edit goal activity 
		goalsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id)
			{
				// vibrate phone for specified period of time when item is long clicked
				((Vibrator) getActivity().getSystemService("vibrator")).vibrate(25);
				
				// initiates the intent to edit goal
				Intent intent = new Intent(getActivity(), EditGoalActivity.class);
				intent.putExtra(GoalsDbAdapter.KEY_ROWID, id);
				startActivityForResult(intent, ACTIVITY_EDIT);
				
				return true;
			}
		});
	}
	
	/**
	 * Populates the list with goals data from the database
	 */
	private void fillListData()
	{
		// Get all of the rows from the database and create the item list
		Cursor goalsCursor = databaseHelper.fetchAllGoals();
		getActivity().startManagingCursor(goalsCursor);
		
		// Create an array to specify the fields we want to display in the list (only TITLE)
		String[] from = {GoalsDbAdapter.KEY_TITLE};
		
		// and an array of the fields we want to bind those fields to (in this case just text1)
		int[] to = {R.id.text1};
		
		// creates a simple cursor adapter and sets it to display goals with the selected item row layout
		setListAdapter(new SimpleCursorAdapter(getActivity(), R.layout.simple_list_item_multiple_choice, goalsCursor, from, to));
	}
	
	/**
	 * Opens the activity to add/edit a goal
	 */
	private void createGoal()
	{
		Intent intent = new Intent(getActivity(), EditGoalActivity.class);
		startActivityForResult(intent, ACTIVITY_CREATE);
	}
	
	@Override
	public void onListItemClick(ListView lv, View view, int position, long id)
	{
		super.onListItemClick(lv, view, position, id);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		fillListData();
	}
}