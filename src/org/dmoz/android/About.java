package org.dmoz.android;

import android.os.Bundle;

public class About extends Activities {

	private static boolean	mDebug	= false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Shared.log("About().onCreated()", mDebug);

		setContentView(R.xml.about);
	}
}