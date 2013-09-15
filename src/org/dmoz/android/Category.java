package org.dmoz.android;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Category implements Serializable {

	private static final long		serialVersionUID		= -7548434087156334682L;

	private static boolean			mDebug							= true;

	public int									id;
	public String								name								= "";
	public String								path								= "";
	public String								pathOriginal				= "";
	public String								pathAbreviated;

	public String								description					= "";

	public int									countSitesPublish		= 0;
	public int									countSitesUnreview	= 0;
	public int									countSitesError			= 0;
	public int									countSitesGreenbust	= 0;
	public int									countSitesUpdate		= 0;
	public int									countSites					= 0;

	public String								updatedAt						= "";

	public int									priority						= 0;
	public int									relationType				= 0;

	public ArrayList<Editor>		editors							= new ArrayList<Editor>();

	public ArrayList<Category>	subcategories				= new ArrayList<Category>();
	public ArrayList<Category>	altlangs						= new ArrayList<Category>();
	public ArrayList<Category>	relcats							= new ArrayList<Category>();
	public ArrayList<Category>	links								= new ArrayList<Category>();

	public ArrayList<Site>			sites								= new ArrayList<Site>();

	public String								mozzie							= "";

	public boolean							canApply						= false;
	public boolean							canSubmit						= false;

	public boolean							isKidsAndTeens			= false;
	public boolean							isAdult							= false;

	public boolean							isLink							= false;
	public boolean							isRelated						= false;
	public boolean							isAltlang						= false;

	public boolean							isAlphaBar					= false;

	public boolean							hasFAQ							= false;

	public Category(JSONObject Object) {

		try {
			//id = Object.getInt("id");

			pathOriginal = Object.getString("location");
			path = Object.getString("location").replaceAll("^/Top/", "");
			pathAbreviated = Shared.mODP.categoryAbbreviate(path);

			relationType = Object.getInt("relationType");

			// @link name
			if (Object.getInt("relationType") == 1) {
				name = Object.getString("relationDetail").replace("_", " ");
				isLink = true;
				// altlang
			} else if (Object.getInt("relationType") == 2) {
				isAltlang = true;
				// instead of displaying altlangs in the current
				// language, display as the "Path" is reporting
				if (pathOriginal.indexOf("/Top/World/") == 0 || pathOriginal.indexOf("/Kids_and_Teens/International/") == 0)
					name = pathOriginal.split("/")[3].replace("_", " ").replaceAll("<[^>]+>", "");
				else if (pathOriginal.indexOf("/Kids_and_Teens/") == 0 || pathOriginal.indexOf("/Top/World") == -1)
					name = "English";
				else
					name = Object.getString("relationDetail").replace("_", " ").replaceAll("<[^>]+>", "");
			} else {
				name = Object.getString("name").replace("_", " ").replaceAll("<[^>]+>", "");
			}

			priority = Object.getInt("priority");

			if (Object.has("description"))
				description = Object.getString("description");

			if (Object.has("isalphamap"))
				isAlphaBar = Object.getBoolean("isalphamap");

			countSitesPublish = Object.getInt("childrenCount");
			//countSitesUnreview = Object.getInt("queueCount");
			//countSitesError = Object.getInt("errorCount");

			//countSitesUpdate = Object.getInt("updateCount");
			//countSitesGreenbust = Object.getInt("greenbustCount");
			countSites = Object.getInt("urlCount");

			if (Object.has("updateAt"))
				updatedAt = Object.getString("updateAt");

			priority = Object.getInt("priority");

			if (Object.has("image"))
				mozzie = Object.getString("image");

			String featureList;
			try {
				featureList = Object.getJSONObject("featureList").toString();
			} catch (Exception e) {
				featureList = "";
			}
			canApply = featureList.indexOf("no_become_editor") == -1;
			canSubmit = featureList.indexOf("no_add_url") == -1;

			isKidsAndTeens = pathOriginal.indexOf("/Kids") == 0;
			isAdult = pathOriginal.indexOf("/Adult") == 0;

			hasFAQ = Object.getBoolean("isPublishFaq");

		} catch (JSONException e) {
			Shared.log("Category()$" + e.getMessage(), mDebug);
		}
	}

	public Category() {

	}

}