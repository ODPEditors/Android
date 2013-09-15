package org.dmoz.android;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;

public class Preferences extends PreferenceActivity {

	private static boolean	mDebug	= false;

	public static class PreferencesFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Shared.log("Preferences().PreferencesFragment().onCreate()", mDebug);

			addPreferencesFromResource(R.xml.preferences);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Shared.log("Preferences().onCreateOptionsMenu()", mDebug);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Shared.log("Preferences().onOptionsItemSelected()", mDebug);
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(this, Home.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Shared.log("Preferences().onCreate()", mDebug);

		getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
	}

}