package com.example.goals_reminder;

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
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
	private Button deleteButton;
	private Button newGoalButton;
	private long[] itemIds;
	private GoalsDbAdapter mDbHelper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		
		View rootView = inflater.inflate(R.layout.goals_fragment, container, false);
		mDbHelper = new GoalsDbAdapter(getActivity());
		mDbHelper.open();
		itemIds = new long[0];
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
				if (itemIds.length != 0)
				{
					long[] arrayOfLong = itemIds;
					int i = arrayOfLong.length;
					for (int j = 0;; j++)
					{
						if (j >= i)
						{
							GoalsFragment.this.itemIds = new long[0];
							GoalsFragment.this.fillListData();
							return;
						}
						long l = arrayOfLong[j];
						mDbHelper.deleteGoal(l);
					}
				}
				
				if (getListView().getAdapter().getCount() > 0)
					Toast.makeText(getActivity(), "Nothing selected", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getActivity(), "Nothing to delete", Toast.LENGTH_SHORT).show();
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
		//registerForContextMenu(getListView());
		
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id)
			{
				((Vibrator) getActivity().getSystemService("vibrator")).vibrate(25);
				Intent intent = new Intent(getActivity(), EditGoalActivity.class);
				intent.putExtra(GoalsDbAdapter.KEY_ROWID, id);
				startActivityForResult(intent, ACTIVITY_EDIT);
				return false;
			}
		});
	}
	
	private void fillListData()
	{
		// Get all of the rows from the database and create the item list
		Cursor goalsCursor = mDbHelper.fetchAllGoals();
		getActivity().startManagingCursor(goalsCursor);
		
		// Create an array to specify the fields we want to display in the list (only TITLE)
		String[] from = {GoalsDbAdapter.KEY_TITLE};
		
		// and an array of the fields we want to bind those fields to (in this case just text1)
		int[] to = {R.id.text1};
		
		// creates a simple cursor adapter and sets it to display
		setListAdapter(new SimpleCursorAdapter(getActivity(), R.layout.simple_list_item_multiple_choice, goalsCursor, from, to));
	}
	
	private void createGoal()
	{
		Intent i = new Intent(getActivity(), EditGoalActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id)
	{
		super.onListItemClick(lv, v, position, id);
		itemIds = lv.getCheckedItemIds();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		fillListData();
	}
}