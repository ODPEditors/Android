package org.dmoz.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class Activities extends Activity {

	private static boolean	mDebug	= false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Shared.log("Activities().onCreate()", mDebug);
		Shared.mActivity = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Shared.log("Activities().onCreateOptionsMenu()", mDebug);
		Shared.mMenu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Shared.log("Activities().onStart();", mDebug);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Shared.log("Activities().onRestart();", mDebug);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Shared.log("Activities().onResume();", mDebug);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Shared.log("Activities().onPause();", mDebug);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Shared.log("Activities().onStop();", mDebug);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Shared.log("Activities().onDestroy();", mDebug);
	}

	public void onInternetConnectionChange() {

	}

}
