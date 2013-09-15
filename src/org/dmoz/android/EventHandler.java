package org.dmoz.android;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;

@SuppressWarnings("rawtypes")
public class EventHandler {

	private static boolean	mDebug							= false;

	public static boolean		mMenuCreated				= false;

	public static ArrayList	mOnMenuUpdate				= new ArrayList();
	public static ArrayList	mOnSlideMenuUpdate	= new ArrayList();
	public static ArrayList	mOnSQLCreated				= new ArrayList();

	@SuppressWarnings("unchecked")
	public static void addOnMenuUpdate(CategoryListView Object) {
		Shared.log("EventHandler().addOnMenuUpdate()", mDebug);
		mOnMenuUpdate.add(Object);
	}

	@SuppressWarnings("unchecked")
	public static void addOnSlideMenuUpdate(Home Object) {
		Shared.log("EventHandler().addOnSlideMenuUpdate()", mDebug);
		mOnSlideMenuUpdate.add(Object);
	}

	@SuppressWarnings("unchecked")
	public static void addOnSQLCreate(Bookmark Object) {
		Shared.log("EventHandler().addOnSQLCreate()", mDebug);
		mOnSQLCreated.add(Object);
	}

	@SuppressWarnings("unchecked")
	public static void addOnSQLCreate(Cache Object) {
		Shared.log("EventHandler().addOnSQLCreate()", mDebug);
		mOnSQLCreated.add(Object);
	}

	public static void onMenuUpdate() {
		Shared.log("EventHandler().onMenuUpdate()", mDebug);
		if (mMenuCreated) {
			for (int i = 0; i < mOnMenuUpdate.size(); i++) {
				String className = mOnMenuUpdate.get(i).getClass().getSimpleName();

				if (className.equals("CategoryListView"))
					((CategoryListView) mOnMenuUpdate.get(i)).onMenuUpdate();
			}
		}
	}

	public static void onSlideMenuUpdate() {
		Shared.log("EventHandler().onSlideMenuUpdate()", mDebug);

		for (int i = 0; i < mOnSlideMenuUpdate.size(); i++) {
			String className = mOnSlideMenuUpdate.get(i).getClass().getSimpleName();

			if (className.equals("Home"))
				((Home) mOnSlideMenuUpdate.get(i)).onSlideMenuUpdate();
		}
	}

	public static void dispatchOnSQLCreated(SQLiteDatabase database) {
		Shared.log("EventHandler().onSQLCreated()", mDebug);

		for (int i = 0; i < mOnSQLCreated.size(); i++) {
			String className = mOnSQLCreated.get(i).getClass().getSimpleName();

			if (className.equals("Bookmark"))
				((Bookmark) mOnSQLCreated.get(i)).onSQLCreated(database);
			else if (className.equals("Cache"))
				((Cache) mOnSQLCreated.get(i)).onSQLCreated(database);
		}
	}

	public static void reset() {
		mMenuCreated = false;
		mOnMenuUpdate = new ArrayList();
		mOnSlideMenuUpdate = new ArrayList();
		mOnSQLCreated = new ArrayList();
	}
}
