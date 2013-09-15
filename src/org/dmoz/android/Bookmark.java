package org.dmoz.android;

import java.util.ArrayList;

import org.dmoz.android.SQL.SQLSimple;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Bookmark {

	private static boolean	mDebug	= false;

	public void onSQLCreated(SQLiteDatabase db) {
		Shared.log("Bookmark().onSQLCreadted();", mDebug);

		db.execSQL("CREATE TABLE IF NOT EXISTS `bookmarks` (`bookmarks_id` INTEGER PRIMARY KEY AUTOINCREMENT, `bookmarks_radiation` INTEGER NOT NULL DEFAULT 0, `bookmarks_category` TEXT NOT NULL);");
		db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `bookmarks_category` ON `bookmarks` (`bookmarks_category` ASC) ");

		create("/Top/World", "0");
		create("/Kids_and_Teens/International", "0");

		create("/Top", "0");

		create("/Kids_and_Teens", "0");

		create("/Top/World/Català", "0");
		create("/Top/World/Dansk", "0");
		create("/Top/World/Deutsch", "0");
		create("/Top/World/Español", "0");
		create("/Top/World/Français", "0");
		create("/Top/World/Italiano", "0");
		create("/Top/World/Japanese", "0");
		create("/Top/World/Nederlands", "0");
		create("/Top/World/Polski", "0");
		create("/Top/World/Russian", "0");
		create("/Top/World/Svenska", "0");
	}

	public void create(String item, String radiation) {
		Shared.log("Bookmark().create();", mDebug);

		SQLSimple sql = Shared.mSQL;
		String[] values = { item, radiation };
		sql.query("insert into `bookmarks` (`bookmarks_category`,`bookmarks_radiation`) VALUES (?, ?)", values);
	}

	public boolean exists(String item) {
		Shared.log("Bookmark().exists();", mDebug);

		SQLSimple sql = Shared.mSQL;
		String[] values = { item };
		return sql.query("select * from `bookmarks` where bookmarks_category = ?", values).size() > 0;
	}

	public void delete(String item) {
		Shared.log("Bookmark().delete();", mDebug);

		SQLSimple sql = Shared.mSQL;
		String[] values = { item };
		sql.query("delete from `bookmarks` where `bookmarks_category` = ?", values);
	}

	public ArrayList<ContentValues> getAll() {
		Shared.log("Bookmark().getAll();", mDebug);

		SQLSimple sql = Shared.mSQL;
		return sql.query("select * from `bookmarks` order by bookmarks_radiation desc, bookmarks_category asc", null);
	}

	public void radiation(String item) {
		Shared.log("Bookmark().radiation();", mDebug);

		SQLSimple sql = Shared.mSQL;
		String[] values = { item };
		sql.query("update `bookmarks` set bookmarks_radiation = bookmarks_radiation+1 where `bookmarks_category` = ?", values);
	}
}
