package com.abelabs.abebatterypro;

import android.content.Context;
import android.content.Intent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;


public class Widget_1x1 extends AppWidgetProvider  {
	static int currentLayout = -1;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		context.startService(new Intent(context, BatteryService_1x1.class));
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (intent.getAction().equals("Main")) {
			Intent myIntent = new Intent();
			myIntent.setClassName("com.abelabs.abebatterypro", "com.abelabs.abebatterypro.BatteryLevelActivity");
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(myIntent);
			
	    }
	}
	
}
