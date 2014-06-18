package com.abelabs.abebatterypro;

//import android.app.PendingIntent;

import com.cyrilmottier.android.greendroid.R;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.widget.RemoteViews;


public class BatteryService_1x1_2 extends Service {
	private BroadcastReceiver batteryWidget;
	private Integer currentLevel = 0;
	private Integer currentTemp = 0;
	private String Temper;
	
	@Override
	public void onStart(Intent intent, int startId) {
		if (this.batteryWidget == null) {
			this.batteryWidget = batteryLevelReceiver();
			IntentFilter filter = new IntentFilter();
	        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
	        filter.addAction(Intent.ACTION_BATTERY_LOW);
	        filter.addAction(Intent.ACTION_BATTERY_OKAY);
	        filter.addAction(Intent.ACTION_POWER_CONNECTED);
	        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
			registerReceiver(this.batteryWidget, filter);
		}

		// Build the widget update.
		RemoteViews updateViews = buildUpdate(this);

		// Push update for this widget to the home screen
		ComponentName batteryWidget = new ComponentName(this,
				Widget_1x1_2.class);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		appWidgetManager.updateAppWidget(batteryWidget, updateViews);
	}

	
	private BroadcastReceiver batteryLevelReceiver() {
		return new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				boolean needsUpdate = false;
				String action = intent.getAction();
				
				if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
					int level = intent.getIntExtra("level", 0);
					int temp = -1;
			        temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
			        currentTemp = temp;
			        
			        if (currentLevel != level) {
						currentLevel = level;						
						needsUpdate = true;
					}		
			        
			        if (currentTemp >= 100){
			    		int fixed = currentTemp / 10;
			    		
			    		Temper = fixed+"";
			    	}else{
			    		Temper = currentTemp.toString();
			    	}
					
					if (currentTemp != temp) {
						currentTemp = temp;						
						needsUpdate = true;
					}					

				}
				

				if (needsUpdate) {
					Intent statusChanged = new Intent(context,
							BatteryService_1x1_2.class);
					context.startService(statusChanged);
				}
			}
		};
	}

	/**
	 * Build a widget update to show the current battery level.
	 */
	private RemoteViews buildUpdate(Context context) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_1x1_2);

		Intent intent = new Intent(context, Widget_1x1_2.class);
	    intent.setAction("Main");
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	    views.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);
	    
	    int currentTemp2 = currentTemp / 10;
	    views.setTextViewText(R.id.batteryTempW, this.Temper+getResources().getString(R.string.grados_2));
		views.setProgressBar(R.id.progressBarT, 65, currentTemp2, false);
		
		ComponentName cn = new ComponentName(context,
				Widget_1x1_2.class);
		AppWidgetManager.getInstance(context).updateAppWidget(cn, views);
		return views;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// We don't need to bind to this service
		return null;
	}

}
