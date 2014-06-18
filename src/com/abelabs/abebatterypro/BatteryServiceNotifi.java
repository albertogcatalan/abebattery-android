package com.abelabs.abebatterypro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;


public class BatteryServiceNotifi extends Service {
	
	private final String fixtemp(int x) {
    	if (x >= 100){
    		int fixed = x / 10;
    		return Integer.toString(fixed);
    	}else{
    		return Integer.toString(x);
    	}
    }
    
    private final String fixvolt(int x) {
    	if (x >= 1000){
    		return Integer.toString(x / 1000) + "." + (x % 1000);
    	}else{
    		return Integer.toString(x);
    	}
    }
    
    

    
	Notification notification;
	CharSequence tickerText;
	int icon;
	CharSequence text = "AbeBattery";
	CharSequence contentTitle = "AbeBattery";
	CharSequence contentText = "AbeBattery Notifications";
	long when = System.currentTimeMillis();
	NotificationManager notificationManager;

	private int NOTIFICATION_ID=1;

	static int scale, level, voltage, temp, plugged, health, status = -1;
	String tech = "Unknown";

	SharedPreferences preferences;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.registerReceiver(br, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

	}
	
	/*@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, NOTIFICATION_ID);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean checkbox = preferences.getBoolean("checkbox_boot_2", true);

	}*/
	@Override
	public void onDestroy() {
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean checkbox = preferences.getBoolean("checkbox_boot_2", true);
	     
	     if (checkbox == false) {
	    	 
	    	 notificationManager.cancel(NOTIFICATION_ID);
			 notificationManager.cancelAll();
	     }
		
	}
		
	private BroadcastReceiver br = new BroadcastReceiver(){

		@Override 
		public void onReceive(Context context, Intent intent) {
			
			tech = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
			plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
			level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
			voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
			health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
			status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			update();
		}

	};
	
	protected void update() {
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean checkbox = preferences.getBoolean("checkbox_boot_2", true);
		/*boolean checkbox_2 = preferences.getBoolean("checkbox_notifi_1", true);
		
			if (checkbox_2 == true) {
				icon = R.drawable.b000;
			}else{
				icon = R.drawable.a044;
				/*if (level >= 31) {
					icon = R.drawable.a043;
				}else if (level <= 30 && level > 15 ){
					icon = R.drawable.a_outline;
				}else if (level <= 15) {
					icon = R.drawable.r_outline;
				}
				
			}*/
		icon = R.drawable.b000;

			Intent intent = new Intent(this, BatteryLevelActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

			contentText = getBatteryHealth() +" | "
											 +fixtemp(temp)+"¼C"
											 +" | "
											 +fixvolt(voltage)+"mV"
											 +" | "
											 +tech;
			contentTitle = getPluggedStatus();
			
			notification = new Notification(icon+level,text,when);
			notification.flags = Notification.FLAG_ONGOING_EVENT;
			notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
		if (checkbox == true) {
			notificationManager.notify(NOTIFICATION_ID, notification);
		}else{
			notificationManager.cancel(NOTIFICATION_ID);
			notificationManager.cancelAll();
		}
		
		

	}

	public static int getLevel() {return level;}
	
	private String getPluggedStatus() {
		String p = "";
		switch(plugged)
		{
		case BatteryManager.BATTERY_PLUGGED_USB:
			p = getBatteryStatus()+" via "+getResources().getString(R.string.conectado_USB); break;
		case BatteryManager.BATTERY_PLUGGED_AC: 
			p = getBatteryStatus()+" via "+getResources().getString(R.string.conectado_AC); break;
		default: p = getResources().getString(R.string.estado_4);
		}
		
		return p;
	}

	private String getBatteryHealth() {
		String healthString = "";

          switch (health) {
          case BatteryManager.BATTERY_HEALTH_UNKNOWN:
              healthString = getResources().getString(R.string.salud_1);
              break;
          case BatteryManager.BATTERY_HEALTH_GOOD:
        	  healthString = getResources().getString(R.string.salud_2);
              break;
          case BatteryManager.BATTERY_HEALTH_OVERHEAT:
        	  healthString = getResources().getString(R.string.salud_3);
              break;
          case BatteryManager.BATTERY_HEALTH_DEAD:
        	  healthString = getResources().getString(R.string.salud_4);
              break;
          case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
        	  healthString = getResources().getString(R.string.salud_5);
              break;
          case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
        	  healthString = getResources().getString(R.string.salud_6);
              break;
          }
		return healthString;
	}

	private String getBatteryStatus() {
		String statusString = "";

         switch (status) {
         case BatteryManager.BATTERY_STATUS_UNKNOWN:
             statusString = getResources().getString(R.string.estado_1);
             break;
         case BatteryManager.BATTERY_STATUS_CHARGING:
             statusString = getResources().getString(R.string.estado_2);
             break;
         case BatteryManager.BATTERY_STATUS_DISCHARGING:
             statusString = getResources().getString(R.string.estado_3);
             break;
         case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
             statusString = getResources().getString(R.string.estado_4);
             break;
         case BatteryManager.BATTERY_STATUS_FULL:
             statusString = getResources().getString(R.string.estado_5);
             break;
         }
		return statusString;
	}
	
	
	
	

}