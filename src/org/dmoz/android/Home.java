package org.dmoz.android;

//had to review this file
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;

public class Home extends Activities {

	private static boolean			mDebug	= false;

	public MyPagerAdapter				mPagerAdapter;
	public ViewPager						mViewPager;
	public PagerTitleStrip			mViewPagerTitleStrip;

	public ShareActionProvider	mShareActionProvider;

	public Menu									mMenu;

	private Sidebar							mSlideMenu;

	public Home() {
		Shared.log("-----------------------------------------------------------------------------------", mDebug);
		Shared.log("Home()", mDebug);
		Shared.mHome = this;
		Shared.mActivity = this;
		
		//receive Intents from external applications
		Shared.mIntentReceiver = new IntentReceiver();
	}

	@Override
	public void onResume() {
		super.onResume();
		Shared.mActivity = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Shared.log("Home().onCreate()", mDebug);
		
		Shared.mIntentReceiver.receive();

		setContentView(R.xml.home);

		// the "tabs" of the main window
		mPagerAdapter = new MyPagerAdapter();
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(1);
		// /the tab's titles
		mViewPagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);

		Progress.onCreateProgressBar();
		Progress.onCreateAnimation();

		// dispatch the last focused category
		String category = Shared.getPrefS("last.focused.category");
		if (category == "")
			category = "/Top/World";
		CategoryListView categoryListView = new CategoryListView(category, false);

		// slide page and menu
		mSlideMenu = new Sidebar(this);
		mSlideMenu.checkEnabled();

		// handlers for when the menus are updated
		EventHandler.addOnMenuUpdate(categoryListView);
		EventHandler.addOnSlideMenuUpdate(this);

		// give to the home button the arrow style
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// update the slide menu if the device was rotated
		onSlideMenuUpdate();

	}

	class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object instantiateItem(ViewGroup collection, int position) {
			Shared.log("Home().onCreate().MyPagerAdapter().instantiateItem()", mDebug);
			LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			int resId = 0;
			switch (position) {
				case 0:
					resId = R.xml.home_sites;
					Shared.log("Creating view home_sites", mDebug);
					break;
				case 1:
					resId = R.xml.home_categories;
					Shared.log("Creating view home_categories", mDebug);
					break;
				case 2:
					resId = R.xml.home_altlangs;
					Shared.log("Creating view home_altlangs", mDebug);
					break;
			/*
			 * case 3: resId = R.xml.home_editors;
			 * Shared.log("Creating view home_editors", mDebug); break;
			 */
			}

			View view = inflater.inflate(resId, null);
			((ViewPager) collection).addView(view, 0);

			switch (position) {
				case 0:
					new SiteListView().onCreate();
					break;
				case 1:
					Shared.mCategoryListView.onCreate();
					break;
				case 2:
					new AltlangListView().onCreate();
					break;
			/*
			 * case 3: new EditorListView().onCreate(); break;
			 */
			}

			return view;
		}

		@Override
		public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
			Shared.log("Home().onCreate().MyPagerAdapter().destroyItem()", mDebug);
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// Shared.log("Home().onCreate().MyPagerAdapter().isViewFromObject()");
			return arg0 == ((View) arg1);
		}

		@Override
		public Parcelable saveState() {
			Shared.log("Home().onCreate().MyPagerAdapter().saveState()", mDebug);
			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// Shared.log("Home().onCreate().MyPagerAdapter().getPageTitle()");
			switch (position) {
				case 0:
					if (Shared.mCategory != null && Shared.mCategory.sites != null)
						return "Sites (" + Shared.numberLocale(Shared.mCategory.sites.size()) + ")";
					else
						return "Sites";
				case 1:
					if (Shared.mCategory != null && Shared.mCategory.name != null)
						return Shared.mCategory.name + " (" + Shared.numberLocale(Shared.mCategory.countSites) + ")";
					else
						return "Category";
				case 2:
					if (Shared.mCategory != null && Shared.mCategory.altlangs != null)
						return "Languages (" + Shared.numberLocale(Shared.mCategory.altlangs.size()) + ")";
					else
						return "Languages";
					/*
					 * case 3: if (Shared.mCategory != null && Shared.mCategory.editors !=
					 * null) return "Editors ( " + Shared.mCategory.editors.size() + " )";
					 * else return "Editors";
					 */
			}
			return null;
		}

		public void updateTitles() {
			Shared.log("Home().onCreate().MyPagerAdapter().updateTitles()", mDebug);
			mViewPager.getAdapter().notifyDataSetChanged();
			mViewPagerTitleStrip.setTextSpacing(mViewPagerTitleStrip.getTextSpacing());
		}

		@Override
		public void notifyDataSetChanged() {
			Shared.log("Home().onCreate().MyPagerAdapter().notifyDataSetChanged()", mDebug);
			super.notifyDataSetChanged();

		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	public void onSlideMenuUpdate() {

		if ((ListView) findViewById(R.id.sidebar_menu) != null) {

			// filing sidebar menu
			final ArrayList<SidebarItem> sidebarMenuItems = new ArrayList<SidebarItem>();
			sidebarMenuItems.add(new SidebarItem("ODP Guidelines", "http://www.dmoz.org/guidelines/", "", 0));
			sidebarMenuItems.add(new SidebarItem("ODP Forums", "http://forums.dmoz.org/", "", 0));
			sidebarMenuItems.add(new SidebarItem("Become an Editor", "http://www.dmoz.org/", "", 0));

			final SidebarItemAdapter sidebarMenuAdapter = new SidebarItemAdapter(R.xml.sidebar_menu_row, sidebarMenuItems);
			final ListView sidebarMenuListView = (ListView) findViewById(R.id.sidebar_menu);
			sidebarMenuListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					mSlideMenu.hide();
					if (position == 0 || position == 1) {
						Shared.openInBrowser(sidebarMenuItems.get(position).mLocation);
					} else {
						if (Shared.mCategory.canApply)
							Shared.openInBrowser("http://www.dmoz.org/public/apply?cat=" + Shared.encodeURIComponent(Shared.mCategoryPath));
						else
							Shared.openInBrowser("http://www.dmoz.org/help/become.html");
					}
				}
			});
			sidebarMenuListView.setAdapter(sidebarMenuAdapter);

			// filling bookmarks

			final ArrayList<SidebarItem> items = new ArrayList<SidebarItem>();

			ArrayList<ContentValues> bookmarks = new Bookmark().getAll();

			for (int i = 0; i < bookmarks.size(); i++) {
				String path = (String) bookmarks.get(i).get("bookmarks_category");
				items.add(new SidebarItem(Shared.mODP.categoryAbbreviate(path), path, path.replace("/", " ").replace("_", " ").replace("-", " "), 0));
			}

			final SidebarItemAdapter adapter = new SidebarItemAdapter(R.xml.sidebar_categories_row, items);
			final ListView listView = (ListView) findViewById(R.id.sidebar_categories);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					mSlideMenu.hide();
					String location = items.get(position).mLocation;
					Shared.mHistory.change(location);
					new CategoryListView(location, false);
					Shared.mHome.mViewPager.setCurrentItem(1);
					new Bookmark().radiation(location);

				}
			});

			// text filter
			final EditText filter = (EditText) findViewById(R.id.sidebar_category_filter);
			// this removes the error SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a
			// zero length spaming the console.
			filter.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			filter.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
					// listView.setFilterText(s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// this will use the built int filter of words, which has a
					// very strange search method..
					adapter.getFilter().filter(s.toString());
				}
			});
			// "onblur" hide the keyboard
			filter.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(filter.getWindowToken(), 0);
					}
				}
			});
			listView.setTextFilterEnabled(true);
			listView.setAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		Shared.log("Home().onCreateOptionsMenu()", mDebug);

		// POPULATE menu.xml
		getMenuInflater().inflate(R.xml.menu, menu);

		// SEARCH
		// SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
		// .getActionView();

		// CREATE SHARE ACTIONS
		MenuItem item = menu.findItem(R.id.menu_share);
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		
		if(Shared.mIntentReceiver.mDomain != null && Shared.mIntentReceiver.mDomain != ""){
			menu.findItem(R.id.menu_add_site).setTitleCondensed("Add "+Shared.mIntentReceiver.mDomain+" to ODP");
			menu.findItem(R.id.menu_add_site).setTitle("Add "+Shared.mIntentReceiver.mDomain+" to ODP");
			menu.findItem(R.id.menu_add_site).setVisible(true);
		} else {
			menu.findItem(R.id.menu_add_site).setVisible(false);
		}
			
		menu.findItem(R.id.menu_browse_up).setEnabled(false);
		menu.findItem(R.id.menu_browse_back).setEnabled(false);
		menu.findItem(R.id.menu_browse_forward).setEnabled(false);

		menu.findItem(R.id.menu_browse_up).getIcon().setAlpha(64);
		menu.findItem(R.id.menu_browse_back).getIcon().setAlpha(64);
		menu.findItem(R.id.menu_browse_forward).getIcon().setAlpha(64);

		EventHandler.mMenuCreated = true;
		EventHandler.onMenuUpdate();

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Shared.log("Home().onPrepareOptionsMenu()", mDebug);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Shared.log("Home().onOptionsItemSelected()", mDebug);

		switch (item.getItemId()) {
		/*
		 * case R.id.menu_settings: Intent preferences = new Intent(this,
		 * Preferences.class);
		 * preferences.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		 * startActivity(preferences); break;
		 */
		/*
		 * case R.id.menu_about: Intent about = new Intent(this, About.class);
		 * about.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); startActivity(about);
		 * break;
		 */
			case R.id.menu_browse_up:
				Shared.mCategoryListView.goUp();
				break;
			case R.id.menu_browse_back:
				Shared.mCategoryListView.goBack();
				break;
			case R.id.menu_browse_forward:
				Shared.mCategoryListView.goForward();
				break;
			case R.id.menu_refresh:
				Shared.mCategoryListView.refresh();
				break;
			case R.id.menu_open_in_browser_public:
				Shared.openInBrowser("http://www.dmoz.org/" + Shared.encodeCategory(Shared.mCategoryPath) + "/");
				break;
			case R.id.menu_open_in_browser_edit:
				Shared.openInBrowser("http://www.dmoz.org/editors/editcat/index?cat=" + Shared.encodeURIComponent(Shared.mCategoryPath));
				break;
			case R.id.menu_open_in_browser_abuse:
				Shared.openInBrowser("http://www.dmoz.org/public/abuse?cat=" + Shared.encodeURIComponent(Shared.mCategoryPath) + "&lang=en");
				break;
			case R.id.menu_apply:
				if (Shared.mCategory.canApply)
					Shared.openInBrowser("http://www.dmoz.org/public/apply?cat=" + Shared.encodeURIComponent(Shared.mCategoryPath));
				else
					Shared.openInBrowser("http://www.dmoz.org/help/become.html");
				break;
			case R.id.menu_suggest:
				if (Shared.mCategory.canSubmit)
					Shared.openInBrowser("http://www.dmoz.org/public/suggest?cat=" + Shared.encodeURIComponent(Shared.mCategoryPath));
				else
					Shared.openInBrowser("http://www.dmoz.org/help/submit.html");
				break;
			case R.id.menu_copy_category_url_to_clipboard:
				Shared.copyToClipboard("http://www.dmoz.org/" + Shared.encodeCategory(Shared.mCategoryPath));
				Shared.alert("Category URL copied to clipboard");
				break;
			case R.id.menu_copy_category_to_clipboard:
				Shared.copyToClipboard(Shared.mCategoryPath);
				Shared.alert("Category copied to clipboard");
				break;
			case R.id.menu_bookmark:
				Bookmark bookmark = new Bookmark();
				if (bookmark.exists(Shared.mCategoryPathOriginal))
					bookmark.delete(Shared.mCategoryPathOriginal);
				else
					bookmark.create(Shared.mCategoryPathOriginal, "1");
				break;
			case android.R.id.home:
				mSlideMenu.show();
				break;
			case R.id.menu_add_site:
				Shared.openInBrowser("http://www.dmoz.org/editors/editurl/add?url="+Shared.encodeURIComponent(Shared.mIntentReceiver.mURL)+"&cat=" + Shared.encodeURIComponent(Shared.mCategoryPath));
				Shared.mIntentReceiver.mDomain = "";
				Shared.mIntentReceiver.mURL = "";
				break;
			default:
				Shared.log("Home().onOptionsItemSelected()$ menuitem not found!", mDebug);
				Shared.log(item.getTitle(), mDebug);
				break;
		}
		EventHandler.onMenuUpdate();
		return super.onOptionsItemSelected(item);
	}

}