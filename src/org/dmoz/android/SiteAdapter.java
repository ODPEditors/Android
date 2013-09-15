package org.dmoz.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SiteAdapter extends ArrayAdapter<Site> {

	private static boolean	mDebug	= false;

	private int							mResourceID;
	private ArrayList<Site>	mItems;

	public SiteAdapter(int resourceID, ArrayList<Site> items) {
		super(Shared.mActivity, resourceID, items);
		Shared.log("SiteAdapter()", mDebug);
		mResourceID = resourceID;
		mItems = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Holder holder = null;
		Site site = mItems.get(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) Shared.mActivity).getLayoutInflater();
			row = inflater.inflate(mResourceID, parent, false);

			holder = new Holder();

			holder.title = (TextView) row.findViewById(R.id.title);
			holder.url = (TextView) row.findViewById(R.id.url);
			holder.description = (TextView) row.findViewById(R.id.description);

			holder.cool = (ImageView) row.findViewById(R.id.cool);
			holder.rss = (ImageView) row.findViewById(R.id.rss);
			holder.pdf = (ImageView) row.findViewById(R.id.pdf);
			holder.atom = (ImageView) row.findViewById(R.id.atom);

			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		String siteTitle;
		List<String> title = new ArrayList<String>();

		if (site.category.isKidsAndTeens) {
			if (site.kids)
				title.add("kids");
			if (site.teens)
				title.add("teens");
			if (site.mteens)
				title.add("mteens");
			siteTitle = site.title + " [" + (Shared.join(title, "/")) + "]";
		} else {
			siteTitle = site.title;
		}

		if (!site.mediadate.equals(""))
			siteTitle += " (" + site.mediadate + ")";

		holder.title.setText(siteTitle);
		holder.url.setText(site.url);
		holder.description.setText(site.description);

		if (site.cool)
			holder.cool.setVisibility(View.VISIBLE);
		else
			holder.cool.setVisibility(View.GONE);

		if (site.rss)
			holder.rss.setVisibility(View.VISIBLE);
		else
			holder.rss.setVisibility(View.GONE);

		if (site.pdf)
			holder.pdf.setVisibility(View.VISIBLE);
		else
			holder.pdf.setVisibility(View.GONE);

		if (site.atom)
			holder.atom.setVisibility(View.VISIBLE);
		else
			holder.atom.setVisibility(View.GONE);

		return row;
	}

	class Holder {
		TextView	title;
		TextView	url;
		TextView	description;

		ImageView	cool;
		ImageView	rss;
		ImageView	pdf;
		ImageView	atom;
	}
}