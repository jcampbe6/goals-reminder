package com.example.goals_reminder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditGoalActivity extends Activity
{
	private EditText mTitleText;
	private EditText mBodyText;
	private Long mRowId;
	private GoalsDbAdapter mDbHelper;
	
	private boolean cancel;
	private Button cancelButton;
	private Button saveButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mDbHelper = new GoalsDbAdapter(this);
		mDbHelper.open();
		
		setContentView(R.layout.edit_goal_activity);
		
		mTitleText = (EditText) findViewById(R.id.editTextTitle_Content);
		mBodyText = (EditText) findViewById(R.id.editTextDescription_Content);
		
		saveButton = (Button) findViewById(R.id.saveButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancel = false;
		
		mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
				.getSerializable(GoalsDbAdapter.KEY_ROWID);
		if (mRowId == null)
		{
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(GoalsDbAdapter.KEY_ROWID) : null;
		}
		
		populateFields();
		
		saveButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				String title = mTitleText.getText().toString();
				String body = mBodyText.getText().toString();
				if (!title.matches(""))
				{
					setResult(RESULT_OK);
					finish();
				}
				else if (title.matches("") && !body.matches(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter a title.",
							Toast.LENGTH_SHORT).show();
				}
				else
					Toast.makeText(getApplicationContext(), "You did not enter any information.",
							Toast.LENGTH_SHORT).show();
			}
		});
		
		this.cancelButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				cancel = true;
				Intent intent = new Intent(getApplicationContext(), Main.class);
				startActivity(intent);
			}
		});
	}
	
	private void populateFields()
	{
		if (mRowId != null)
		{
			Cursor goal = mDbHelper.fetchGoal(mRowId);
			startManagingCursor(goal);
			mTitleText.setText(goal.getString(goal.getColumnIndexOrThrow(GoalsDbAdapter.KEY_TITLE)));
			mBodyText.setText(goal.getString(goal.getColumnIndexOrThrow(GoalsDbAdapter.KEY_BODY)));
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(GoalsDbAdapter.KEY_ROWID, mRowId);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		saveState();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		populateFields();
	}
	
	private void saveState()
	{
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();
		if (mRowId == null && !title.matches("") && !cancel)
		{
			long id = mDbHelper.createGoal(title, body);
			if (id > 0)
			{
				mRowId = id;
			}
			cancel = false;
		}
		else if (title.matches("") || cancel)
		{
			Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
		}
		else
		{
			mDbHelper.updateGoal(mRowId, title, body);
			cancel = false;
		}
	}
}