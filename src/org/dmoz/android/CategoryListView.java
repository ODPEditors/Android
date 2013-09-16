package org.dmoz.android;

//had to check this file...
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryListView {

	private static boolean	mDebug	= false;

	public CategoryListView(String categoryPath, boolean invalidateCache) {
		Shared.log("CategoryListView()", mDebug);

		Shared.mCategoryPathOriginal = categoryPath;
		Shared.mCategoryPath = categoryPath.replaceAll("^/Top/", "");
		Shared.mCategoryListView = this;
		Shared.setPrefS("last.focused.category", categoryPath);
		new CategoryParser(categoryPath, invalidateCache);
	}

	public void onCreate() {
		Shared.log("CategoryListView().onCreate()", mDebug);

		if (Shared.mCategory != null) {

			// TextView categoryPath = (TextView)
			// Shared.mActivity.findViewById(R.id.title_bar).findViewById(R.id.title);
			// categoryPath.setText(Shared.mCategory.pathAbreviated);

			CategoryAdapter adapter = new CategoryAdapter(R.xml.home_categories_row, Shared.mCategory.subcategories);

			ListView listView = (ListView) Shared.mActivity.findViewById(R.id.categories).findViewById(R.id.list);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					Shared.mHistory.change(Shared.mCategory.pathOriginal);
					new CategoryListView(Shared.mCategory.subcategories.get(position).pathOriginal, false);
				}
			});
			listView.setAdapter(adapter);

			if (Shared.mCategory.subcategories.size() > 0) {
				Shared.mActivity.findViewById(R.id.categories).findViewById(R.id.empty).setVisibility(View.GONE);
				Shared.mActivity.findViewById(R.id.categories).findViewById(R.id.list_container).setVisibility(View.VISIBLE);
			} else {
				((TextView) Shared.mActivity.findViewById(R.id.categories).findViewById(R.id.empty).findViewById(R.id.empty_info))
						.setText("There are no subcategories in this category.");

				Shared.mActivity.findViewById(R.id.categories).findViewById(R.id.empty).setVisibility(View.VISIBLE);
				Shared.mActivity.findViewById(R.id.categories).findViewById(R.id.list_container).setVisibility(View.GONE);
			}

			EventHandler.onMenuUpdate();

		} else {
			Shared.log("CategoryListView().onCreate()$Shared.mCategory is null", mDebug);
		}
	}

	public void goUp() {
		if (canGoUp()) {
			Shared.mHistory.change(Shared.mCategoryPathOriginal);
			new CategoryListView(Shared.mCategoryPathOriginal.replaceAll("/[^/]+$", ""), false);
		}
	}

	public boolean canGoUp() {
		return Shared.mCategoryPathOriginal.replaceAll("/[^/]+$", "") != "";
	}

	public void goBack() {
		if (Shared.mHistory.canGoBack(Shared.mCategoryPathOriginal))
			new CategoryListView(Shared.mHistory.goBack(Shared.mCategoryPathOriginal), false);
	}

	public void goForward() {
		if (Shared.mHistory.canGoForward(Shared.mCategoryPathOriginal))
			new CategoryListView(Shared.mHistory.goForward(Shared.mCategoryPathOriginal), false);
	}

	public void refresh() {
		new CategoryListView(Shared.mCategoryPathOriginal, true);
	}

	public void onMenuUpdate() {
		if (Shared.mCategory != null) {
			Shared.log("CategoryListView().onMenuUpdate()", mDebug);

			// set share intent
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.dmoz.org/" + Shared.encodeCategory(Shared.mCategoryPath));
			// some times this function gets called before onCreateMenuOptions
			if (Shared.mHome.mShareActionProvider != null)
				Shared.mHome.mShareActionProvider.setShareIntent(shareIntent);

			// enable disable menuitems
			Shared.mMenu.findItem(R.id.menu_browse_up).setEnabled(canGoUp());
			Shared.mMenu.findItem(R.id.menu_browse_back).setEnabled(Shared.mHistory.canGoBack(Shared.mCategoryPathOriginal));
			Shared.mMenu.findItem(R.id.menu_browse_forward).setEnabled(Shared.mHistory.canGoForward(Shared.mCategoryPathOriginal));
			Shared.mMenu.findItem(R.id.menu_bookmark).setEnabled(true);

			Shared.mMenu.findItem(R.id.menu_browse_up).getIcon().setAlpha(canGoUp() ? 255 : 64);
			Shared.mMenu.findItem(R.id.menu_browse_back).getIcon().setAlpha(Shared.mHistory.canGoBack(Shared.mCategoryPathOriginal) ? 255 : 64);
			Shared.mMenu.findItem(R.id.menu_browse_forward).getIcon().setAlpha(Shared.mHistory.canGoForward(Shared.mCategoryPathOriginal) ? 255 : 64);

			Bookmark bookmark = new Bookmark();
			if (bookmark.exists(Shared.mCategoryPathOriginal)) {
				// Shared.log("Bookmark exists: " +
				// Shared.mCategoryPathOriginal, mDebug);
				Shared.mMenu.findItem(R.id.menu_bookmark).setIcon(R.drawable.bookmark);
				Shared.mMenu.findItem(R.id.menu_bookmark).setTitle("Remove Bookmark");
			} else {
				// Shared.log("Bookmark does not exists: " +
				// Shared.mCategoryPathOriginal, mDebug);
				Shared.mMenu.findItem(R.id.menu_bookmark).setIcon(R.drawable.unbookmark);
				Shared.mMenu.findItem(R.id.menu_bookmark).setTitle("Bookmark Category");
			}
		}
	}
}
