package com.rameses.android;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.rameses.android.R;
import com.rameses.android.system.SettingsActivity;

public class SettingsMenuActivity extends ControlActivity 
{
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.settings:
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
		}
		return true;
	}
}
