package sanchezsobrino.multimedia.anwc.business;

import java.util.Locale;

import sanchezsobrino.multimedia.anwc.persistence.LocalizedStringManager;

public class LocalizedString {
	private String localizedString;
	private Locale locale;
	
	public LocalizedString(String key) {
		this.locale = Locale.getDefault();
		this.localizedString = LocalizedStringManager.get(key, this.locale);
	}
	
	public LocalizedString(String key, String language, String country) {
		this.locale = new Locale(language, country);
		this.localizedString = LocalizedStringManager.get(key, this.locale);
	}
	
	public Locale getLocale() {
		return this.locale;
	}
	
	public String toString() {
		return localizedString;
	}
}