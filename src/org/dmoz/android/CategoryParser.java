package org.dmoz.android;

//had to check this file
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.AsyncTask;

@SuppressLint("DefaultLocale")
public class CategoryParser {

	private static boolean	mDebug						= false;

	private String					mURL							= null;
	private boolean					mInvalidateCache	= false;
	private String					mCategoryPath;

	public CategoryParser(String categoryPath, boolean invalidateCache) {
		Shared.log("CategoryParser()", mDebug);

		mInvalidateCache = invalidateCache;
		mCategoryPath = categoryPath;

		if (!invalidateCache && Shared.mCategory != null && Shared.mCategory.pathOriginal != null && Shared.mCategory.pathOriginal.equals(categoryPath)) {

			Shared.log("CategoryParser().cachedInMemory = true;", mDebug);
			Shared.mHome.mViewPager.getAdapter().notifyDataSetChanged();

		} else {

			if (Shared.mCurrentTask != null)
				Shared.mCurrentTask.cancel(true);
			mURL = "http://www.godzuki.com.uy/client.php?id=" + Shared.encodeURIComponent(categoryPath);
			Shared.log("CategoryParser().readURL() " + mURL, mDebug);
			Shared.mCurrentTask = new ReadURL();
			Shared.mCurrentTask.execute();
		}
	}

	@SuppressLint("DefaultLocale")
	private class ReadURL extends AsyncTask<Void, Void, Void> {

		private String	mError			= null;
		private int			progressMax	= 100;
		private int			progressSet	= 0;

		@Override
		protected void onPreExecute() {
			Shared.log("CategoryParser().onPreExecute();", mDebug);
			// publishProgress();
		}

		@SuppressLint("DefaultLocale")
		@Override
		protected Void doInBackground(Void... params) {

			// check if category exists in database (if not invalidating)
			if (!mInvalidateCache) {

				ArrayList<ContentValues> cache = new Cache().get(mCategoryPath);

				if (cache.size() > 0) {
					Shared.log("CategoryParser().cachedInDatabase = true;", mDebug);
					try {
						Shared.mCategory = Shared.unserialize((String) cache.get(0).get("cache_data"));
					} catch (IOException e) {
						Shared.log("CategoryParser().cache.database", mDebug);
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						Shared.log("CategoryParser().cache.database", mDebug);
						e.printStackTrace();
					}
					progressSet = 100;
					progressMax = 100;
					publishProgress();
					return null;
				}
			}

			try {
				Shared.log("CategoryParser().doInBackground();", mDebug);

				progressSet = 4;
				publishProgress();

				if (isCancelled())
					return null;

				HttpGet httpGet = new HttpGet(mURL);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(httpGet);
				Header[] clHeaders = response.getHeaders("Content-Length");
				Header header = clHeaders[0];
				int totalSize = Integer.parseInt(header.getValue());
				String content = "";
				InputStream inputStream = response.getEntity().getContent();
				Writer writer = new StringWriter();

				Shared.log("Total size: " + Shared.toString(totalSize), mDebug);

				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
					char[] buffer = new char[1024 * 1024];
					int count;
					int total = 0;
					while ((count = reader.read(buffer)) != -1) {
						if (isCancelled()) {
							writer.close();
							inputStream.close();
							progressMax = totalSize;
							progressSet = totalSize;
							publishProgress();
							return null;
						}
						total += count;
						progressMax = totalSize;
						progressSet = total;
						publishProgress();
						// Shared.log(total);
						writer.write(buffer, 0, count);
					}
					content = writer.toString();
				} finally {
					writer.close();
					inputStream.close();
				}
				client.getConnectionManager().shutdown();

				progressMax = totalSize;
				progressSet = totalSize;
				publishProgress();

				if (isCancelled())
					return null;

				JSONObject top = new JSONObject(content).getJSONObject("response").getJSONObject("results").getJSONObject("rows").getJSONObject("1");

				if (isCancelled())
					return null;

				JSONObject currentCategory = top.getJSONObject("detail").getJSONObject("1");

				if (isCancelled())
					return null;

				Category mCategory = new Category(currentCategory);
				JSONObject item;
				JSONObject tmp;
				Site site;

				if (isCancelled())
					return null;

				// Shared.log("editors");
				try {
					tmp = top.getJSONObject("editors");
					for (int i = 1; i <= tmp.length(); i++) {
						item = tmp.getJSONObject("" + i);
						try {
							Editor editor = new Editor();
							editor.editor = item.getString("userName");
							editor.displayName = item.getString("displayName").trim();
							try {
								editor.homepage = item.getString("homepage").trim();
							} catch (Exception e) {

							}
							editor.photoURL = item.getString("photoURL").trim();
							mCategory.editors.add(editor);
						} catch (JSONException e) {
							Shared.log("CategoryParser().doInBackground()$editors:" + e.getMessage(), mDebug);
							Shared.log(tmp.getJSONObject("" + i).toString(), mDebug);
						}
					}
				} catch (JSONException e) {
					// Shared.log(e.getMessage());
				}
				if (isCancelled())
					return null;

				// Shared.log("urls");
				ArrayList<Site> noSortedSites = new ArrayList<Site>();

				try {
					tmp = top.getJSONObject("urls");
					for (int i = 1; i <= tmp.length(); i++) {
						try {
							item = tmp.getJSONObject("" + i);
							site = new Site();
						
							site.url = Shared.removeNewLines(item.getString("url")).replace("\t", " ").trim();
							site.title = Shared.removeNewLines(item.getString("title")).replace("\t", " ").trim();
							site.description = Shared.removeNewLines(item.getString("description")).replace("\t", " ").trim();

							site.cool = item.getBoolean("cool");
							site.uksite = item.getBoolean("uksite");

							site.kids = item.getBoolean("kids");
							site.teens = item.getBoolean("teens");
							site.mteens = item.getBoolean("mteens");

							if (item.has("mediaDate"))
								site.mediadate = item.getString("mediaDate").trim();

							if (item.has("contentType")) {
								site.rss = item.getString("contentType").toLowerCase().indexOf("rss") != -1;
								site.pdf = item.getString("contentType").toLowerCase().indexOf("pdf") != -1;
								site.atom = item.getString("contentType").toLowerCase().indexOf("atom") != -1;
							}
							site.category = mCategory;
							noSortedSites.add(site);
						} catch (JSONException e) {
							Shared.log("CategoryParser().doInBackground()$urls:" + e.getMessage(), mDebug);
							Shared.log(tmp.getJSONObject("" + i).toString(), mDebug);
						}
					}
				} catch (JSONException e) {
					// Shared.log(e.getMessage());
				}

				// sorting sites by title
				Comparator<Site> sortSiteByTitle = new Comparator<Site>() {
					Collator	collator	= Collator.getInstance();

					@Override
					public int compare(Site obj1, Site obj2) {
						return collator.compare(obj1.mediadate + " " + obj1.title, obj2.mediadate + " " + obj2.title);
					}
				};
				Collections.sort(noSortedSites, sortSiteByTitle);

				// very cool about sorting by title.. but cool sites go first...

				// cool sites
				for (int i = 0; i < noSortedSites.size(); i++) {
					site = noSortedSites.get(i);
					if (site.cool)
						mCategory.sites.add(site);
				}

				// then, no atom, rss, or mediadate
				for (int i = 0; i < noSortedSites.size(); i++) {
					site = noSortedSites.get(i);
					if (!site.cool && !site.atom && !site.rss && site.mediadate.equals(""))
						mCategory.sites.add(site);
				}

				// then, atom, rss, but mediadate
				for (int i = 0; i < noSortedSites.size(); i++) {
					site = noSortedSites.get(i);
					if (!site.cool && (site.atom || site.rss) && site.mediadate.equals(""))
						mCategory.sites.add(site);
				}

				// ohh mediadate!
				for (int i = 0; i < noSortedSites.size(); i++) {
					site = noSortedSites.get(i);
					if (!site.cool && !site.mediadate.equals(""))
						mCategory.sites.add(site);
				}

				if (isCancelled())
					return null;

				// Shared.log("children");
				try {
					tmp = top.getJSONObject("children");
					for (int i = 1; i <= tmp.length(); i++) {
						try {
							item = tmp.getJSONObject("" + i);
							Category category = new Category(item);
							if (category.pathOriginal.equals("/Top/AOL") || category.pathOriginal.equals("/Top/Netscape"))
								continue;
							mCategory.subcategories.add(category);
						} catch (JSONException e) {
							Shared.log("CategoryParser().doInBackground()$children:" + e.getMessage(), mDebug);
							Shared.log(tmp.getJSONObject("" + i).toString(), mDebug);
						}
					}
				} catch (JSONException e) {
					// Shared.log(e.getMessage());
				}
				if (isCancelled())
					return null;

				// Shared.log("altlang");
				try {
					tmp = top.getJSONObject("altlang");
					for (int i = 1; i <= tmp.length(); i++) {
						try {
							item = tmp.getJSONObject("" + i);
							Category category = new Category(item);
							mCategory.altlangs.add(category);
						} catch (JSONException e) {
							Shared.log("CategoryParser().doInBackground()$altlang:" + e.getMessage(), mDebug);
							Shared.log(tmp.getJSONObject("" + i).toString(), mDebug);
						}
					}
				} catch (JSONException e) {
					// Shared.log(e.getMessage());
				}
				if (isCancelled())
					return null;

				// Shared.log("related");
				try {
					tmp = top.getJSONObject("related");
					for (int i = 1; i <= tmp.length(); i++) {
						try {
							item = tmp.getJSONObject("" + i);
							Category category = new Category(item);
							mCategory.relcats.add(category);
						} catch (JSONException e) {
							Shared.log("CategoryParser().doInBackground()$related:" + e.getMessage(), mDebug);
							Shared.log(tmp.getJSONObject("" + i).toString(), mDebug);
						}
					}
				} catch (JSONException e) {
					// Shared.log(e.getMessage());
				}
				if (isCancelled())
					return null;

				// Shared.log("symbolic");
				try {
					tmp = top.getJSONObject("symbolic");
					for (int i = 1; i <= tmp.length(); i++) {
						try {
							item = tmp.getJSONObject("" + i);
							Category category = new Category(item);
							mCategory.links.add(category);
							mCategory.subcategories.add(category);
						} catch (JSONException e) {
							Shared.log("CategoryParser().doInBackground()$symbolic:" + e.getMessage(), mDebug);
							Shared.log(tmp.getJSONObject("" + i).toString(), mDebug);
						}
					}
				} catch (JSONException e) {
					// Shared.log(e.getMessage());
				}
				if (isCancelled())
					return null;

				// sorting ( user locale dependent, love this ;) )
				Comparator<Category> sortCategoryByName = new Comparator<Category>() {
					Collator	collator	= Collator.getInstance();

					@Override
					public int compare(Category obj1, Category obj2) {
						return collator.compare(obj1.name, obj2.name);
					}
				};

				Collections.sort(mCategory.subcategories, sortCategoryByName);
				Collections.sort(mCategory.links, sortCategoryByName);
				Collections.sort(mCategory.relcats, sortCategoryByName);
				Collections.sort(mCategory.altlangs, sortCategoryByName);

				// categories sorted now lets ... check sort priority

				for (int i = 0; i < mCategory.subcategories.size(); i++)
					Shared.log(mCategory.subcategories.get(i).toString(), mDebug);

				Comparator<Editor> sortEditorByName = new Comparator<Editor>() {
					Collator	collator	= Collator.getInstance();

					@Override
					public int compare(Editor obj1, Editor obj2) {
						return collator.compare(obj1.editor, obj2.editor);
					}
				};
				Collections.sort(mCategory.editors, sortEditorByName);

				/*
				 * //for testing the sorting! :)
				 * 
				 * for(int i=0;i<mCategory.subcategories.size();i++)
				 * Shared.log(mCategory.subcategories.get(i).name, mDebug);
				 */

				new Cache().update(mCategory.pathOriginal, Shared.serialize(mCategory));
				Shared.mCategory = mCategory;

			} catch (ClientProtocolException e) {
				mError = e.getMessage();
				Shared.log("CategoryParser().ReadURL().doItBackground()$catch ClientProtocolException", mDebug);
				Shared.log(mError, mDebug);
				cancel(true);
			} catch (IOException e) {
				mError = e.getMessage();
				Shared.log("CategoryParser().ReadURL().doItBackground()$catch IOException", mDebug);
				Shared.log(mError, mDebug);
				cancel(true);
			} catch (JSONException e) {
				mError = e.getMessage();
				Shared.log("CategoryParser().ReadURL().doItBackground()$catch JSONException", mDebug);
				Shared.log(mError, mDebug);
				cancel(true);
			}
			progressSet = 100;
			progressMax = 100;
			publishProgress();
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... data) {
			Progress.setMax(progressMax);
			Progress.setProgress(progressSet);
		}

		@Override
		protected void onCancelled(Void unused) {
			Shared.log("CategoryParser().onCancelled();", mDebug);

			Progress.setMax(100);
			Progress.setProgress(100);
		}

		@Override
		protected void onPostExecute(Void unused) {
			Shared.log("CategoryParser().onPostExecute();", mDebug);
			if (!isCancelled()) {
				if (mError != null) {
					Shared.log(mError, mDebug);
				} else {
					Shared.mHome.mViewPager.getAdapter().notifyDataSetChanged();
				}
			}
		}

	}
}
