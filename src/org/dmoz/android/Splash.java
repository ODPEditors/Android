package org.dmoz.android;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activities {

	private static boolean		mDebug	= false;
	private static final long	mDelay	= 1500;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Shared.log("Splash.onCreated()", mDebug);

		setContentView(R.xml.splash);

		Task task = new Task();
		new Timer().schedule(task, mDelay);
	}

	class Task extends TimerTask {
		@Override
		public void run() {
			Shared.mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					startActivity(new Intent(getApplicationContext(), Home.class));
					Splash.this.finish();
				}
			});
		}
	}

}