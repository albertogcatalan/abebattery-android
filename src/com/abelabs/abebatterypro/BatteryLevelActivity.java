/*  
 *  AbeBattery - Controls battery of your device for Android
 *  Copyright (C) 2011 Alberto Gonzalez Catalan
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.abelabs.abebatterypro;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class BatteryLevelActivity extends GDActivity {
	
	private final Handler mHandler = new Handler();
	
	private TextView batterLevel;
    private TextView batterStatus;
    private TextView batterTemp;
    private TextView batterVolt;
    private TextView batterTech;
    private TextView batterHealth;
    private TextView batterStat;

    SharedPreferences preferences;
    
    private void getPrefs() {
    	preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean checkbox = preferences.getBoolean("checkbox_boot_2", true);
		
		if (checkbox == true) {
			startService(new Intent(this, BatteryServiceNotifi.class));
		}else{
			stopService(new Intent(this, BatteryServiceNotifi.class));
			
		}
    }
    
   
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
    
    private final String statusinfo(int x) {
    	if (x == 1){
    		return getResources().getString(R.string.estado_1);
    	}else if (x == 2){
    		return getResources().getString(R.string.estado_2);
    	}else if (x == 3){
    		return getResources().getString(R.string.estado_3);
    	}else if (x == 4){
    		return getResources().getString(R.string.estado_4);
    	}else if (x == 5){
    		return getResources().getString(R.string.estado_5);
    	}else {
    		return Integer.toString(x);
    	}
    }
    
    private final String healthinfo(int x) {
    	if (x == 1){
    		return getResources().getString(R.string.salud_1);
    	}else if (x == 2){
    		return getResources().getString(R.string.salud_2);
    	}else if (x == 3){
    		return getResources().getString(R.string.salud_3);
    	}else if (x == 4){
    		return getResources().getString(R.string.salud_4);
    	}else if (x == 5){
    		return getResources().getString(R.string.salud_5);
    	}else if (x == 5){
    		return getResources().getString(R.string.salud_6);
    	}else if (x == 5){
        	return getResources().getString(R.string.salud_7);
    	}else {
    		return Integer.toString(x);
    	}
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.battery);
        
        batterLevel = (TextView) this.findViewById(R.id.batteryLevel);
        batterStatus = (TextView) this.findViewById(R.id.batteryStatus);
        batterTemp = (TextView) this.findViewById(R.id.batteryTemp);
        batterVolt = (TextView) this.findViewById(R.id.batteryVolt);
        batterTech = (TextView) this.findViewById(R.id.batteryTech);
        batterHealth = (TextView) this.findViewById(R.id.batteryHealth);
        batterStat = (TextView) this.findViewById(R.id.batteryStat);
        
        addActionBarItem(Type.Refresh);
        addActionBarItem(Type.Settings);
        
        getPrefs();
		
    }
    
    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (position) {
            case 0:
            	final LoaderActionBarItem loaderItem = (LoaderActionBarItem) item;
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        loaderItem.setLoading(false);
                    }
                }, 2000);
            	IntentFilter filter = new IntentFilter();
 	            filter.addAction(Intent.ACTION_BATTERY_CHANGED);
 	            filter.addAction(Intent.ACTION_BATTERY_LOW);
 	            // filter.addAction(Intent.ACTION_BATTERY_CRITICAL);
 	            filter.addAction(Intent.ACTION_BATTERY_OKAY);
 	            filter.addAction(Intent.ACTION_POWER_CONNECTED);
 	            filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
 	            registerReceiver(mIntentReceiver, filter);
                break;

            case 1:
                Intent myIntent_1 = new Intent();
     			myIntent_1.setClassName("com.abelabs.abebatterypro", "com.abelabs.abebatterypro.Config");
     			myIntent_1.addCategory(Intent.CATEGORY_ALTERNATIVE);
     			startActivity(myIntent_1);
                
                break;

            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }

   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.MnuOpc1:
	        	IntentFilter filter = new IntentFilter();
 	            filter.addAction(Intent.ACTION_BATTERY_CHANGED);
 	            filter.addAction(Intent.ACTION_BATTERY_LOW);
 	            // filter.addAction(Intent.ACTION_BATTERY_CRITICAL);
 	            filter.addAction(Intent.ACTION_BATTERY_OKAY);
 	            filter.addAction(Intent.ACTION_POWER_CONNECTED);
 	            filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
 	            registerReceiver(mIntentReceiver, filter);
 				return true;
	        case R.id.MnuOpc2:
	        	try { 
	                Intent mainIntent = new Intent(Intent.ACTION_MAIN); 
	                    ComponentName batteryUseComponent = new ComponentName( 
		        "com.android.settings", "com.android.settings.fuelgauge.PowerUsageSummary"); 
		                    mainIntent.setComponent(batteryUseComponent); 
		                    startActivity(mainIntent); 
		        } catch (ActivityNotFoundException e) { 
	
		        } 
	
	    		return true;
	        case R.id.MnuOpc3:
	        	Intent myIntent_1 = new Intent();
     			myIntent_1.setClassName("com.abelabs.abebatterypro", "com.abelabs.abebatterypro.Config");
     			myIntent_1.addCategory(Intent.CATEGORY_ALTERNATIVE);
     			startActivity(myIntent_1);
     			return true;
            case R.id.MnuOpc4:
            	Intent myIntent = new Intent();
    			myIntent.setClassName("com.abelabs.abebatterypro", "com.abelabs.abebatterypro.About");
    			myIntent.addCategory(Intent.CATEGORY_ALTERNATIVE);
    			startActivity(myIntent);
    			return true;
             
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        // filter.addAction(Intent.ACTION_BATTERY_CRITICAL);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mIntentReceiver, filter);
        
        getPrefs();
        
    
    }
    
    @Override
    public void onRestart(){
    	super.onRestart();
    	IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        // filter.addAction(Intent.ACTION_BATTERY_CRITICAL);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mIntentReceiver, filter);
    
        getPrefs();
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	unregisterReceiver(mIntentReceiver);
    }
    
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	 int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
             int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
             int level = -1;
             
             if (rawlevel >= 0 && scale > 0) {
                 level = (rawlevel * 100) / scale;
             }     
             
             batterLevel.setText("" + level + "%");
             
             
             int plugged = -1;
             plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
             
             if (plugged == 0) {
             	batterStatus.setText(getResources().getString(R.string.conectado_AC_Off)+" ");
             }else{
             	if (plugged == 1) {
                 	batterStatus.setText(getResources().getString(R.string.conectado_AC)+" ");
             	}else{
             		batterStatus.setText(getResources().getString(R.string.conectado_USB)+" ");
             	}
             	
             }
             
             int stat = 0;
             stat = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
             batterStat.setText(""+statusinfo(stat)+" ");
             
             int health = 0;
             health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
             batterHealth.setText(""+healthinfo(health)+" ");
             
             int temp = -1;
             temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
             batterTemp.setText("" + fixtemp(temp) + getResources().getString(R.string.grados_2)+" ");
             
             int voltage;
             voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
             batterVolt.setText("" + fixvolt(voltage) + getResources().getString(R.string.volt_2)+" ");
             
             String technology = "";
             technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
             batterTech.setText("" + technology+" ");
             
        }
    };
     

}
   