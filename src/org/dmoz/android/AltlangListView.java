package org.dmoz.android;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class AltlangListView {

	private static boolean	mDebug	= false;

	public AltlangListView() {
		Shared.log("AltlangListView()", mDebug);
	}

	public void onCreate() {
		Shared.log("AltlangListView().onCreate()", mDebug);

		if (Shared.mCategory != null) {
			AltlangAdapter adapter = new AltlangAdapter(R.xml.home_altlangs_row, Shared.mCategory.altlangs);

			ListView listView = (ListView) Shared.mActivity.findViewById(R.id.altlangs).findViewById(R.id.list);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Shared.mHistory.change(Shared.mCategory.pathOriginal);
					new CategoryListView(Shared.mCategory.altlangs.get(position).pathOriginal, false);
					Shared.mHome.mViewPager.setCurrentItem(1);
				}
			});
			listView.setAdapter(adapter);

			if (Shared.mCategory.altlangs.size() > 0) {
				Shared.mActivity.findViewById(R.id.altlangs).findViewById(R.id.empty).setVisibility(View.GONE);
				Shared.mActivity.findViewById(R.id.altlangs).findViewById(R.id.list_container).setVisibility(View.VISIBLE);

			} else {
				((TextView) Shared.mActivity.findViewById(R.id.altlangs).findViewById(R.id.empty).findViewById(R.id.empty_info))
						.setText("There are no alternative languages in this category.");
				Shared.mActivity.findViewById(R.id.altlangs).findViewById(R.id.empty).setVisibility(View.VISIBLE);
				Shared.mActivity.findViewById(R.id.altlangs).findViewById(R.id.list_container).setVisibility(View.GONE);
			}
		} else {
			Shared.log("AltlangListView().onCreate()$Shared.mCategory is null", mDebug);
		}
	}
}