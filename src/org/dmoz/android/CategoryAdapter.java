package org.dmoz.android;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryAdapter extends ArrayAdapter<Category> {

	private static boolean			mDebug	= false;

	private int									mResourceID;
	private ArrayList<Category>	mItems;

	public CategoryAdapter(int resourceID, ArrayList<Category> items) {
		super(Shared.mActivity, resourceID, items);
		Shared.log("CategoryAdapter()", mDebug);
		mResourceID = resourceID;
		mItems = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Shared.log("CategoryAdapter().getView", mDebug);
		View row = convertView;
		Holder holder = null;
		Category category = mItems.get(position);

		if (row == null) {
			LayoutInflater inflater = Shared.mActivity.getLayoutInflater();
			row = inflater.inflate(mResourceID, parent, false);

			holder = new Holder();
			holder.name = (TextView) row.findViewById(R.id.title);

			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		if (category.relationType == 1)
			holder.name.setText("@" + category.name + " (" + Shared.numberLocale(category.countSites) + ")");
		else
			holder.name.setText(category.name + " (" + Shared.numberLocale(category.countSites) + ")");
		return row;
	}

	static class Holder {
		TextView	name;
	}
}