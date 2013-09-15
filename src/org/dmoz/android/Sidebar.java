// This class was taken from this URL: http://stackoverflow.com/questions/11234375/how-did-google-manage-to-do-this-slide-actionbar-in-android-application

package org.dmoz.android;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;




public class Sidebar {

	static int									mTransitionsDuration	= 212;
	static int									mMenuSize							= 250;

	static boolean							menuShown							= false;
	private static View					menu;
	private static LinearLayout	content;
	private static FrameLayout	parent;
	private static int					menuSize;
	private static int					statusHeight					= 0;
	private Activity						act;

	Sidebar(Activity act) {
		this.act = Shared.mActivity;
	}

	// call this in your onCreate() for screen rotation
	public void checkEnabled() {
		if (menuShown)
			this.show(false);
	}

	public void show() {
		// get the height of the status bar
		if (statusHeight == 0) {
			Rect rectgle = new Rect();
			Window window = act.getWindow();
			window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
			statusHeight = rectgle.top;
		}
		this.show(true);
	}

	public static int dpToPx(int dp, Context ctx) {
		Resources r = ctx.getResources();
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
	}

	// originally:
	// http://stackoverflow.com/questions/5418510/disable-the-touch-events-for-all-the-views
	// modified for the needs here
	public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
		int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = viewGroup.getChildAt(i);
			if (view.isFocusable())
				view.setEnabled(enabled);
			if (view instanceof ViewGroup) {
				enableDisableViewGroup((ViewGroup) view, enabled);
			} else if (view instanceof ListView) {
				if (view.isFocusable())
					view.setEnabled(enabled);
				ListView listView = (ListView) view;
				int listChildCount = listView.getChildCount();
				for (int j = 0; j < listChildCount; j++) {
					if (view.isFocusable())
						listView.getChildAt(j).setEnabled(false);
				}
			}
		}
	}

	public void show(boolean animate) {
		
		menuSize = dpToPx(mMenuSize, act);
		if(Build.VERSION.SDK_INT < 18) 
			content = ((LinearLayout) act.findViewById(android.R.id.content).getParent());
		else
			content = ((LinearLayout) act.findViewById(android.R.id.content).getParent());
		
		FrameLayout.LayoutParams parm = (FrameLayout.LayoutParams) content.getLayoutParams();
		((MarginLayoutParams) parm).setMargins(menuSize, 0, -menuSize, 0);
		content.setLayoutParams(parm);
		// animation for smooth slide-out
		TranslateAnimation ta = new TranslateAnimation(-menuSize, 0, 0, 0);
		ta.setDuration(mTransitionsDuration);
		if (animate)
			content.startAnimation(ta);
		parent = (FrameLayout) content.getParent();
		LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menu = inflater.inflate(R.xml.sidebar, null);
		FrameLayout.LayoutParams lays = new FrameLayout.LayoutParams(-1, -1, 3);
		lays.setMargins(0, statusHeight, 0, 0);
		menu.setLayoutParams(lays);
		parent.addView(menu);
    
		if (animate)
			menu.startAnimation(ta);
		menu.findViewById(R.id.overlay).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Sidebar.this.hide();
			}
		});
		enableDisableViewGroup((LinearLayout) parent.findViewById(android.R.id.content).getParent(), false);
    ((ViewPager) act.findViewById(R.id.pager)).setEnabled(false);
    ((PagerTitleStrip) act.findViewById(R.id.pager_title_strip)).setEnabled(false);
  
		menuShown = true;
		this.fill();
	}

	public void fill() {
		EventHandler.onSlideMenuUpdate();
	}

	public void hide() {
		TranslateAnimation ta = new TranslateAnimation(0, -menuSize, 0, 0);
		ta.setDuration(mTransitionsDuration);
		menu.startAnimation(ta);
		parent.removeView(menu);

		TranslateAnimation tra = new TranslateAnimation(menuSize, 0, 0, 0);
		tra.setDuration(mTransitionsDuration);
		content.startAnimation(tra);
		FrameLayout.LayoutParams parm = (FrameLayout.LayoutParams) content.getLayoutParams();
		parm.setMargins(0, 0, 0, 0);
		content.setLayoutParams(parm);
		enableDisableViewGroup((LinearLayout) parent.findViewById(android.R.id.content).getParent(), true);
   // ((ExtendedViewPager) act.findViewById(R.id.pager)).setPagingEnabled(false);

	  //  ((ExtendedPagerTabStrip) act.findViewById(R.id.pager_title_strip)).setNavEnabled(false);
	  
		menuShown = false;
	}

	// source:
	// http://blog.svpino.com/2011/08/disabling-pagingswiping-on-android.html

	public class ExtendedPagerTabStrip extends PagerTabStrip {

		private boolean	enabled;

		public ExtendedPagerTabStrip(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.enabled = true;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (this.enabled) {
				return super.onTouchEvent(event);
			}

			return false;
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent event) {
			if (this.enabled) {
				return super.onInterceptTouchEvent(event);
			}

			return false;
		}

		public void setNavEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public class ExtendedViewPager extends ViewPager {

		private boolean	enabled;

		public ExtendedViewPager(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.enabled = true;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (this.enabled) {
				return super.onTouchEvent(event);
			}

			return false;
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent event) {
			if (this.enabled) {
				return super.onInterceptTouchEvent(event);
			}

			return false;
		}

		public void setPagingEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

}