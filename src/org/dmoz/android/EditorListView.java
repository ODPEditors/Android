package org.dmoz.android;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class EditorListView {

	private static boolean	mDebug	= false;

	public EditorListView() {
		Shared.log("EditorListView()", mDebug);

	}

	public void onCreate() {
		Shared.log("EditorListView().onCreate()", mDebug);

		if (Shared.mCategory != null) {
			EditorAdapter adapter = new EditorAdapter(R.xml.home_editors_row, Shared.mCategory.editors);

			ListView listView = (ListView) Shared.mActivity.findViewById(R.id.editors).findViewById(R.id.list);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				}
			});
			listView.setAdapter(adapter);
		} else {
			Shared.log("EditorListView().onCreate()$Shared.mCategory is null", mDebug);
		}
	}
}