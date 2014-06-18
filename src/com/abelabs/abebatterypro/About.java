package com.abelabs.abebatterypro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class About extends Activity {

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.about);

		findViewById(R.id.btnCerrar).setOnClickListener(exitButton);
		
		Intent i = getIntent();

		if (i.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
			setTitle(R.string.acerca);
			
		}
		
	}

	private OnClickListener exitButton = new OnClickListener() {
		public void onClick(View v) 
		{
			finish();
		}
	};
}