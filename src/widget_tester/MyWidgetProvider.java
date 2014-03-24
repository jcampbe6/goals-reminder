package widget_tester;

import java.util.Random;
import com.goalsreminder.app.GoalsDbAdapter;
import com.goalsreminder.app.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider
{
	
	private static final String ACTION_CLICK = "ACTION_CLICK";
	
	//test variables
	private GoalsDbAdapter databaseHelper;
	
	@Override
	public void onEnabled(Context context)
	{
		databaseHelper = new GoalsDbAdapter(context);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		// Get all ids
		ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds)
		{
			// create some random data
			////int number = (new Random().nextInt(100));
			
			Cursor cursor = databaseHelper.getCount();
			String title = String.valueOf(cursor.getCount());
			
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			////Log.w("WidgetExample", String.valueOf(number));
			
			// Set the text
			remoteViews.setTextViewText(R.id.update, title);
			
			// Register an onClickListener
			Intent intent = new Intent(context, MyWidgetProvider.class);
			
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
	
	private void getGoal()
	{
		
	}
}
