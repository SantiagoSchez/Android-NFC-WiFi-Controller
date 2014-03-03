package sanchezsobrino.multimedia.anwc.persistence;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizedStringManager {
	public static String get(String key, Locale locale) {
		return ResourceBundle.getBundle("lang", locale).getString(key);
	}
}
