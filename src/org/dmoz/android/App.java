package org.dmoz.android;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;

public class App extends Application {

	private static boolean	mDebug	= false;

	public App() {}

	@Override
	public void onCreate() {
		super.onCreate();
		Shared.mApplication = this;// do not move this line!
		Shared.log("App().onCreate()", mDebug);

		// handler for when the sql database is created
		EventHandler.addOnSQLCreate(new Bookmark());
		EventHandler.addOnSQLCreate(new Cache());

		Shared._fakeConstructor();// do not move this line!
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Shared.log("App().onConfigurationChanged()", mDebug);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Shared.log("App().onLowMemory()", mDebug);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Shared.log("App().onTerminate()", mDebug);
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		Shared.log("App().onTrimMemory()", mDebug);
	}

	@Override
	public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
		super.registerActivityLifecycleCallbacks(callback);
		Shared.log("App().registerActivityLifecycleCallbacks()", mDebug);
	}

	@Override
	public void registerComponentCallbacks(ComponentCallbacks callback) {
		super.registerComponentCallbacks(callback);
		Shared.log("App().registerComponentCallbacks()", mDebug);
	}

	@Override
	public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
		super.unregisterActivityLifecycleCallbacks(callback);
		Shared.log("App().unregisterActivityLifecycleCallbacks()", mDebug);
	}

	@Override
	public void unregisterComponentCallbacks(ComponentCallbacks callback) {
		super.unregisterComponentCallbacks(callback);
		Shared.log("App().unregisterComponentCallbacks()", mDebug);
	}

}
