package org.dmoz.android;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.dmoz.android.SQL.SQLSimple;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

//had to review this file
@SuppressLint("DefaultLocale")
public class Shared {

	private static boolean										_fakeConstructor	= false;

	private static boolean										mDebugAll					= false;
	private static boolean										mDebug						= false;

	public static History											mHistory;

	public static Category										mCategory;
	public static String											mCategoryPathOriginal;
	public static String											mCategoryPath;

	// public static Home mHome;

	public static CategoryListView						mCategoryListView;

	public static AsyncTask<Void, Void, Void>	mCurrentTask;

	public static SQLiteDatabase							mDatabase;
	public static SQLSimple										mSQL;

	public static String											mAppName;
	public static App													mApplication;

	public static ODP													mODP;

	public static Activities									mActivity;

	public static Menu												mMenu;

	public static Home												mHome;

	private static Locale											mLocale;

	protected Shared() {}

	public static void _fakeConstructor() {
		if (!Shared._fakeConstructor) {
			Shared._fakeConstructor = true;
			Shared.mAppName = Shared.mApplication.getPackageName();
			if (Shared.mDebug || Shared.mDebugAll)
				Log.d(Shared.mAppName, "Shared()");
			Shared.mLocale = Locale.getDefault();
			// the first time SQL is instantiated automatically runs onCreate
			// which allows to create the tables to store the data.
			new SQL().open();
			Shared.mHistory = new History();
			Shared.mODP = new ODP();

		}
	}

	public static void log(String string, boolean mDebug) {
		if (mDebug || Shared.mDebugAll)
			Log.d(Shared.mAppName, "ODP: " + string);
	}

	public static void log(boolean bool, boolean mDebug) {
		if (mDebug || Shared.mDebugAll)
			Log.d(Shared.mAppName, "ODP: " + (bool ? "true" : "false"));
	}

	public static void log(int integer, boolean mDebug) {
		if (mDebug || Shared.mDebugAll)
			Log.d(Shared.mAppName, "ODP: " + (Integer.toString(integer)));
	}

	public static void log(JSONObject Object, boolean mDebug) {
		if (mDebug || Shared.mDebugAll)
			Log.d(Shared.mAppName, "ODP: " + Object.toString());
	}

	public static void log(CharSequence Object, boolean mDebug) {
		if (mDebug || Shared.mDebugAll)
			Log.d(Shared.mAppName, "ODP: " + Object.toString());
	}

	public static String toString(String string) {
		return string;
	}

	public static String toString(boolean bool) {
		return bool ? "true" : "false";
	}

	public static String toString(int integer) {
		return Integer.toString(integer);
	}

	public static String toString(JSONObject Object) {
		return Object.toString();
	}

	public static String encodeURIComponent(String string) {
		try {
			return URLEncoder.encode(string, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\%21", "!").replaceAll("\\%27", "'").replaceAll("\\%28", "(")
					.replaceAll("\\%29", ")").replaceAll("\\%7E", "~");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String decodeURIComponent(String string) {
		String result = null;
		try {
			result = URLDecoder.decode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return string;
		}
		return result;
	}

	public static String encodeCategory(String string) {
		return Shared.encodeURIComponent(string).replace("%2F", "/");
	}

	public static ClipData copyToClipboardURI(String string) {
		return ClipData.newUri(Shared.mActivity.getContentResolver(), "URI", Uri.parse(string));
	}

	@SuppressWarnings("deprecation")
	public static void copyToClipboard(String string) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) Shared.mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(string);
		} else {
			android.content.ClipboardManager clipboard = (ClipboardManager) Shared.mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
			android.content.ClipData clip = android.content.ClipData.newPlainText(string, string);
			clipboard.setPrimaryClip(clip);
		}
	}

	public static void openInBrowser(String string) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(string));
		Shared.mActivity.startActivity(browserIntent);
	}

	public static void alert(String string) {
		Toast.makeText(Shared.mActivity, string, Toast.LENGTH_LONG).show();
	}

	public static String serialize(Category obj) throws IOException {
		Shared.log("Serialize().serialize();", Shared.mDebug);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, 0);
		ObjectOutputStream oos = new ObjectOutputStream(base64OutputStream);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		return byteArrayOutputStream.toString();
	}

	public static Category unserialize(String string) throws IOException, ClassNotFoundException {
		Shared.log("Serialize().unserialize();", Shared.mDebug);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(string.getBytes());
		Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream, 0);
		ObjectInputStream iis = new ObjectInputStream(base64InputStream);
		return (Category) iis.readObject();
	}

	public static String getPrefS(String aName) {
		SharedPreferences preferences = Shared.mApplication.getSharedPreferences(Shared.mAppName, Context.MODE_PRIVATE);
		return preferences.getString(aName, "");
	}

	public static int getPrefI(String aName) {
		SharedPreferences preferences = Shared.mApplication.getSharedPreferences(Shared.mAppName, Context.MODE_PRIVATE);
		return preferences.getInt(aName, 0);
	}

	public static boolean getPrefB(String aName) {
		SharedPreferences preferences = Shared.mApplication.getSharedPreferences(Shared.mAppName, Context.MODE_PRIVATE);
		return preferences.getBoolean(aName, false);
	}

	public static void setPrefS(String aName, String aValue) {
		SharedPreferences preferences = Shared.mApplication.getSharedPreferences(Shared.mAppName, Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putString(aName, aValue);
		edit.commit();
	}

	public static void setPrefI(String aName, int aValue) {
		SharedPreferences preferences = Shared.mApplication.getSharedPreferences(Shared.mAppName, Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putInt(aName, aValue);
		edit.commit();
	}

	public static void setPrefB(String aName, boolean aValue) {
		SharedPreferences preferences = Shared.mApplication.getSharedPreferences(Shared.mAppName, Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putBoolean(aName, aValue);
		edit.commit();
	}

	// WTF JAVA!?
	public static String join(List<String> list, String delim) {
		StringBuilder sb = new StringBuilder();
		String loopDelim = "";

		for (String s : list) {
			sb.append(loopDelim);
			sb.append(s);
			loopDelim = delim;
		}

		return sb.toString();
	}

	public static String removeNewLines(String string) {
		return string.replace("\\u000d", " ").replace("\\u000a", " ").replace("\r", " ").replace("\n", " ");
	}

	public static ArrayList<String> reverse(ArrayList<String> array) {
		ArrayList<String> copy = new ArrayList<String>(array);
		Collections.reverse(copy);
		return copy;
	}

	public static String numberLocale(int interger) {
		return NumberFormat.getNumberInstance(Shared.mLocale).format(interger);
	}

	// returns true or false if searchQuery is found in aString
	public static boolean searchEngineSearch(String searchQuery, String aString) {
		// normalizing aString - search is caseinsensitive
		aString = aString.trim().toLowerCase();

		if (searchQuery.equals(""))
			return true;
		// finding "or" conditions
		searchQuery = searchQuery.trim().toLowerCase().replaceAll(" or ", ",").replaceAll(" +", " ");
		String[] query = searchQuery.split(",");
		// for each "or" condition - if no "or" conditions was found, this will loop
		// one time.
		for (int i = 0; i < query.length; i++) {
			// getting words
			String[] subQuery = query[i].trim().split(" ");
			// found flag
			boolean found = false;

			// foreach word to search
			for (int a = 0; a < subQuery.length; a++) {
				String word = subQuery[a].trim();

				if (word.equals("") || word.equals("-") || word.equals("+")) {
					continue;
				} else if (word.startsWith("-") && aString.indexOf(word.replaceAll("^-", "")) != -1) {
					found = false;
					break;
				} else if (!word.startsWith("-") && aString.indexOf(word) == -1) {
					found = false;
					break;
				} else {
					found = true;
				}
			}
			if (found)
				return true;
		}
		return false;
	}

	/*
	 * public static void prompt(String title, String ok, String cancel, Context
	 * context) { AlertDialog.Builder builder = new AlertDialog.Builder(context);
	 * builder.setMessage(title) .setPositiveButton(ok, new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int id) {
	 * 
	 * } }) .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int id) {
	 * 
	 * } }); builder.show(); }
	 */
}