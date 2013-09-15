package org.dmoz.android;

public class ODP {

	// private static boolean mDebug = false;

	public String categoryAbbreviate(String aCategory) {
		// Shared.log("ODP().categoryAbbreviate()", mDebug);

		aCategory = aCategory.replaceAll("^/Top/", "");

		int deep = aCategory.split("/").length;

		// language abbreviation
		if (deep > 2 && (aCategory.indexOf("World/") != -1 || aCategory.indexOf("/International/") != -1)) {
			String[] find = { "Íslenska", "Česky", "Afrikaans", "Arabic", "Armenian", "Asturianu", "Azerbaijani", "Bahasa_Indonesia", "Bahasa_Melayu", "Bangla",
					"Belarusian", "Bosanski", "Brezhoneg", "Bulgarian", "Català", "Chinese_Simplified", "Chinese_Traditional", "Cymraeg", "Dansk", "Deutsch", "Eesti",
					"Español", "Esperanto", "Euskara", "Føroyskt", "Farsi", "Français", "Frysk", "Gàidhlig", "Gaeilge", "Galego", "Greek", "Gujarati", "Hebrew", "Hindi",
					"Hrvatski", "Interlingua", "Italiano", "Japanese", "Kannada", "Kaszëbsczi", "Kazakh", "Kiswahili", "Korean", "Kurdî", "Lëtzebuergesch", "Latviski",
					"Lietuvių", "Lingua_Latina", "Magyar", "Makedonski", "Marathi", "Nederlands", "Norsk", "Occitan", "Polska", "Português", "Punjabi", "Română",
					"Rumantsch", "Russian", "Sardu", "Shqip", "Sicilianu", "Slovensko", "Slovensky", "Srpski", "Suomi", "Svenska", "Türkçe", "Tagalog", "Taiwanese",
					"Tamil", "Tatarça", "Telugu", "Thai", "Ukrainian", "Vietnamese" };
			String[] replace = { "IS", "CS", "AF", "AR", "HY", "AST", "AZ", "ID", "MS", "BA", "BE", "BS", "BR", "BG", "CA", "CHS", "CHT", "CY", "DA", "DE", "ET",
					"ES", "EO", "EU", "FO", "Farsi", "FR", "FY", "GD", "GA", "GL", "EL", "GU", "HE", "HI", "HR", "IA", "IT", "JA", "KN", "CSB", "KK", "SW", "KO", "KU",
					"LB", "Latviski", "LT", "LL", "HU", "Makedonski", "MR", "NL", "NO", "OC", "PL", "PT", "PA", "RO", "RM", "RU", "SC", "SQ", "SCN", "Slovensko",
					"Slovensky", "SH", "FI", "SV", "TR", "TL", "TW", "TA", "TT", "TE", "TH", "UK", "VI" };

			for (int i = 0; i < find.length; i++)
				aCategory = aCategory.replace(find[i], replace[i]);
		}

		// World
		aCategory = aCategory.replaceAll("^World/", "").replace("World/", "W/")
		// RTL languagues
				.replaceAll("^Arabic/", "").replaceAll("^Persian/", "")
				// Kids and teens
				.replace("/International/", "/I/");

		// do not abbreviate short categories
		if (deep < 3)
			return aCategory.replaceAll("^/", "").replace("_", " ");

		// user designated abbreviation
		String[] find = { "/Artes_y_entretenimiento", "Arts/", "Business/", "Computers/", "Games/", "Health/", "Home/", "News/", "Recreation/", "Reference/",
				"Science/", "Shopping/", "Society/", "Sports/", "Adult/", "/Open_Directory_Project", "Kids_and_Teens/", "Regional/", "Bookmarks/", "World_Test/",
				"Test/", "United_States/", "North_America/", "Internet/", "Industries/", "/Business_and_Economy", "Localities/", "/Computers_and_Internet",
				"Government/", "Development", "Search_Engines", "/Ciencia_y_tecnología", "/Computación_e_Internet", "/Deportes_y_tiempo_libre", "/Economía_y_negocios",
				"/Guías_y_directorios", "/Noticias_y_medios", "/Viajes_y_turismo", "/Medios_de_comunicación", "/Argentina", "/Bolivia", "/Chile", "/Colombia",
				"/Costa_Rica", "/Cuba", "/Ecuador", "/España", "/Estados_Unidos", "/Guatemala", "/Guinea_Ecuatorial", "/Honduras", "/México", "/Nicaragua", "/Panamá",
				"/Paraguay", "/Perú", "/Puerto_Rico", "/El_Salvador", "/Uruguay", "/Venezuela", "/Departamentos", "/Provincias", "/América", "/Europa", "/Gobierno",
				"/Educación", "/Universidades", "/Facultades_y_Escuelas", "/Software", "/Education" };

		String[] replace = { "/Art", "Art/", "Bus/", "C/", "G/", "He/", "Ho/", "N/", "Rec/", "Ref/", "Sci/", "Sh/", "Soc/", "Sp/", "Ad/", "/ODP", "K&T/", "R/",
				"B/", "WT/", "T/", "USA/", "NA/", "I/", "Ind/", "/B&E", "Loc/", "/CyI", "Gov/", "Dev", "SE", "/CyT", "/CeI", "/DyTL", "/EyN", "/GyD", "/NyM", "/VyT",
				"/MdC", "/AR", "/BO", "/CL", "/CO", "/CR", "/CU", "/EC", "/ES", "/USA", "/GT", "/GQ", "/HN", "/MX", "/NI", "/PA", "/PY", "/PE", "/PR", "/SV", "/UY",
				"/VE", "/Dptos", "/Prov", "/A", "/E", "/Gob", "/EDU", "/Univer", "/FyE", "/Soft", "/EDU" };

		for (int i = 0; i < find.length; i++)
			aCategory = aCategory.replace(find[i], replace[i]);

		return aCategory.replaceAll("^/", "").replace("_", " ");
	}
}
