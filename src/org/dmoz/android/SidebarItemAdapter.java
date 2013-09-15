package org.dmoz.android;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

class SidebarItemAdapter extends ArrayAdapter<SidebarItem> {

	private static boolean						mDebug	= false;

	private int												mResourceID;
	private ArrayList<SidebarItem>		mItems;
	@SuppressWarnings("unused")
	private final SidebarItemAdapter	mSelf;

	private ArrayList<SidebarItem>		mOriginalItems;

	@SuppressWarnings("unchecked")
	public SidebarItemAdapter(int resourceID, ArrayList<SidebarItem> items) {
		super(Shared.mActivity, resourceID, items);
		Shared.log("SidebarMenuAdapter()", mDebug);
		mResourceID = resourceID;
		mItems = items;
		mOriginalItems = (ArrayList<SidebarItem>) items.clone();
		mSelf = this;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Shared.log("CategoryAdapter().getView", mDebug);
		View row = convertView;
		Holder holder = null;
		SidebarItem item = mItems.get(position);

		if (row == null) {
			LayoutInflater inflater = Shared.mActivity.getLayoutInflater();
			row = inflater.inflate(mResourceID, parent, false);

			holder = new Holder();
			holder.label = (TextView) row.findViewById(R.id.label);
			holder.icon = (ImageView) row.findViewById(R.id.icon);

			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		holder.label.setText(item.mLabel);
		if (item.mIcon != 0 && false)// NO ICON!
			holder.icon.setImageResource(item.mIcon);
		return row;
	}

	static class Holder {
		TextView	label;
		ImageView	icon;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				ArrayList<SidebarItem> filtered = (ArrayList<SidebarItem>) results.values;
				notifyDataSetChanged();
				clear();
				for (int i = 0, l = filtered.size(); i < l; i++)
					add(filtered.get(i));
				notifyDataSetInvalidated();

			}

			@SuppressWarnings("unchecked")
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				final ArrayList<SidebarItem> results = new ArrayList<SidebarItem>();
				ArrayList<SidebarItem> items = new ArrayList<SidebarItem>();
				items = (ArrayList<SidebarItem>) mOriginalItems.clone();
				for (int i = 0; i < items.size(); i++) {
					SidebarItem item = items.get(i);
					if (Shared.searchEngineSearch(constraint.toString(), item.mDescription))
						results.add(item);
				}
				final FilterResults oReturn = new FilterResults();
				oReturn.values = results;
				oReturn.count = results.size();
				return oReturn;
			}
		};
	}
}