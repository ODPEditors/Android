package org.dmoz.android;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SiteListView {

	private static boolean	mDebug	= false;

	public SiteListView() {
		Shared.log("SiteListView()", mDebug);
	}

	public void onCreate() {
		Shared.log("SiteListView().onCreated()", mDebug);

		if (Shared.mCategory != null) {
			SiteAdapter adapter = new SiteAdapter(R.xml.home_sites_row, Shared.mCategory.sites);

			ListView listView = (ListView) Shared.mActivity.findViewById(R.id.sites).findViewById(R.id.list);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					Shared.openInBrowser(Shared.mCategory.sites.get(position).url);

				}
			});
			listView.setAdapter(adapter);

			if (Shared.mCategory.sites.size() > 0) {
				Shared.mActivity.findViewById(R.id.sites).findViewById(R.id.empty).setVisibility(View.GONE);
				Shared.mActivity.findViewById(R.id.sites).findViewById(R.id.list_container).setVisibility(View.VISIBLE);

			} else {
				((TextView) Shared.mActivity.findViewById(R.id.sites).findViewById(R.id.empty).findViewById(R.id.empty_info))
						.setText("There is no sites in this category.");

				Shared.mActivity.findViewById(R.id.sites).findViewById(R.id.empty).setVisibility(View.VISIBLE);
				Shared.mActivity.findViewById(R.id.sites).findViewById(R.id.list_container).setVisibility(View.GONE);

			}
		}
	}
}