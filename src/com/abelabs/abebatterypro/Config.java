package com.abelabs.abebatterypro;

import android.os.Bundle;
import android.preference.PreferenceActivity;



public class Config extends PreferenceActivity {
	 
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
          addPreferencesFromResource(R.xml.preferences);
          
	  }
	 
	  
	}