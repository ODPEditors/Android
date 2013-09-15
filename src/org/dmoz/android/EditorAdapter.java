package org.dmoz.android;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EditorAdapter extends ArrayAdapter<Editor> {

	private static boolean		mDebug	= false;

	private int								mResourceID;
	private ArrayList<Editor>	mItems;

	public EditorAdapter(int resourceID, ArrayList<Editor> items) {
		super(Shared.mActivity, resourceID, items);
		Shared.log("EditorAdapter()", mDebug);

		mResourceID = resourceID;
		mItems = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Shared.log("EditorAdapter().getView()", mDebug);

		View row = convertView;
		Holder holder = null;

		if (row == null) {
			LayoutInflater inflater = Shared.mActivity.getLayoutInflater();
			row = inflater.inflate(mResourceID, parent, false);

			holder = new Holder();
			holder.editor = (TextView) row.findViewById(R.id.editor);

			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		Editor editor = mItems.get(position);
		holder.editor.setText(editor.editor);
		return row;
	}

	static class Holder {
		TextView	editor;
	}
}