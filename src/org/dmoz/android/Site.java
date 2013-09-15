package org.dmoz.android;

import java.io.Serializable;

public class Site implements Serializable {

	private static final long	serialVersionUID	= -7621556837951971316L;

	public int								id;
	public int								nodeURLId;
	public int								domainId;

	public String							url;
	public String							title;
	public String							description;

	public boolean						cool							= false;

	public boolean						uksite						= false;

	public boolean						kids							= false;
	public boolean						teens							= false;
	public boolean						mteens						= false;

	public boolean						rss								= false;
	public boolean						pdf								= false;
	public boolean						atom							= false;

	public String							mediadate					= "";

	public Category						category;

	public String							colors						= "";

	public Site() {

	}
}
