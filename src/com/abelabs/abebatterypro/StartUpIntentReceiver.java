package com.abelabs.abebatterypro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StartUpIntentReceiver extends BroadcastReceiver {
	
	SharedPreferences preferences;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean checkbox = preferences.getBoolean("checkbox_boot_1", false);
		if (checkbox == true) {
			Intent startServiceIntent = new Intent(context, BatteryServiceNotifi.class);
        context.startService(startServiceIntent);
			
		}
		
		
	}

}
