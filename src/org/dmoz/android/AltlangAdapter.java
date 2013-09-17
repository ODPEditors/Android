package org.dmoz.android;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AltlangAdapter extends ArrayAdapter<Category> {

	private static boolean			mDebug	= false;

	private int									mResourceID;
	private ArrayList<Category>	mItems;

	public AltlangAdapter(int resourceID, ArrayList<Category> items) {
		super(Shared.mActivity, resourceID, items);
		Shared.log("AltlangAdapter()", mDebug);

		mResourceID = resourceID;
		mItems = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Shared.log("AltlangAdapter().getView()", mDebug);

		View row = convertView;
		Holder holder = null;

		if (row == null) {
			LayoutInflater inflater = Shared.mActivity.getLayoutInflater();
			row = inflater.inflate(mResourceID, parent, false);

			holder = new Holder();
			holder.title = (TextView) row.findViewById(R.id.title);

			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		Category category = mItems.get(position);
		holder.title.setText(category.name + " (" + Shared.numberLocale(category.countSites) + ")");
		return row;
	}

	static class Holder {
		TextView	title;
	}
}