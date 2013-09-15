package org.dmoz.android;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;

public class Progress {

	private static boolean	mDebug	= false;

	protected Progress() {}

	private static ProgressBar			mProgressBar;
	private static int							mMax	= 0;
	private static RotateAnimation	mAnimation;

	public static void onCreateProgressBar() {
		Shared.log("Progress().onCreate()", mDebug);
		Progress.mProgressBar = (ProgressBar) Shared.mActivity.findViewById(R.id.progress_bar);
		if (Progress.mProgressBar != null) {
			Progress.mProgressBar.setMax(100);
			Progress.mProgressBar.setProgress(100);
		}
	}

	public static void onCreateAnimation() {
		View icon = Shared.mActivity.findViewById(R.id.menu_refresh);
		if (icon != null) {
			Progress.mAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			Progress.mAnimation.setRepeatCount(Animation.INFINITE);
			Progress.mAnimation.setDuration(1500);
		}
	}

	public static void setMax(int i) {
		// Shared.log("Progress().setMax()$"+Shared.toString(i), mDebug);

		if (Progress.mAnimation == null)
			Progress.onCreateAnimation();
		if (Progress.mProgressBar == null)
			Progress.onCreateProgressBar();

		mMax = i;
		if (Progress.mProgressBar != null) {
			Progress.mProgressBar.setMax(i);
		}

	}

	public static void setProgress(int i) {
		// Shared.log("Progress().setProgress()$"+Shared.toString(i), mDebug);

		if (Progress.mAnimation == null)
			Progress.onCreateAnimation();
		if (Progress.mProgressBar == null)
			Progress.onCreateProgressBar();

		if (Progress.mAnimation != null) {
			if (i < Progress.mMax)
				Progress.startAnimation();
			else
				Progress.mAnimation.setRepeatCount(0);
		}
		if (Progress.mProgressBar != null)
			Progress.mProgressBar.setProgress(i);
	}

	private static void startAnimation() {
		if ((!Progress.mAnimation.hasStarted() || Progress.mAnimation.hasEnded()) || Progress.mAnimation.hasStarted() && Progress.mAnimation.hasEnded()) {
			Progress.mAnimation.reset();
			Shared.log("Progress().startAnimation()", mDebug);
			View icon = Shared.mActivity.findViewById(R.id.menu_refresh);
			icon.startAnimation(Progress.mAnimation);
			Progress.mAnimation.setRepeatCount(Animation.INFINITE);
		} else {
			// Shared.log(Progress.mAnimation.hasStarted(), mDebug);
			// Shared.log(Progress.mAnimation.hasEnded(), mDebug);
		}

	}

}
