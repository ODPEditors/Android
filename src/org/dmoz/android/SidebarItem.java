package org.dmoz.android;

public class SidebarItem {
	String	mLabel				= "";
	String	mLocation			= "";
	String	mDescription	= "";
	int			mIcon					= 0;

	public SidebarItem(String label, String location, String description, int icon) {
		mLabel = label;
		mLocation = location;
		mDescription = description;
		mIcon = icon;
	}

	@Override
	public String toString() {
		return mDescription;
	}

}