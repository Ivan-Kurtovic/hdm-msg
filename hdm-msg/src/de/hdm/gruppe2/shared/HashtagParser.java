package de.hdm.gruppe2.shared;

import java.util.ArrayList;

import de.hdm.gruppe2.shared.bo.Hashtag;

/**
 * Eine statische Hilfsklasse zum extrahieren von Hashtags aus �bergebenen
 * Strings. Sie wird verwendet um die Hashtag-Liste f�r die Message Objekte
 * zu generieren. Die statische Struktur wurde dabei gew�hlt, da die enthaltene
 * Methode projektweit und ohne notwendige Instanziierung der enthaltenen Klasse
 * verwendet werden soll.
 * 
 * @author Sari
 *
 */
public class HashtagParser {
	
	/**
	 * Diese Methode extrahiert alle vorkommenden Strings eines �bergebenen
	 * Textes. Dazu wird der �bergebene String zun�chst an jedem '#' Zeichen
	 * gesplittet und die daraus resultierenden Substrings nochmals verarbeitet.
	 * 
	 * @param text Der Text der auf enthaltene Hashtags untersucht werden soll.
	 * @return Eine Liste aller Hashtag Objekte die aus dem Text extrahiert wurden.
	 */
	public static ArrayList<Hashtag> checkForHashtags(String text) {

		String[] substrings = text.split("#");
		ArrayList<Hashtag> hashtags = new ArrayList<Hashtag>();
		
		// Alle erhaltenen Substrings sollen nun auf ihre Schlagw�rter analysiert
		// und daraus ein Hashtag Objekt erzeugt werden.
		for(String s : substrings) {
			Hashtag h = new Hashtag();
			
			// Der erste enthaltene Substring ist entweder leer oder enth�lt
			// keinen hashtag, da "hallo #Welt" gesplittet wird in "hallo " und "Welt".
			if(substrings[0] == s) {
				continue;
			}
			
			// Falls das letzte enthaltene Zeichen des Substrings ein Leerzeichen ist
			// soll das Schlagwort des Hashtags der Inhalt des Strings bis zu dem Leerzeichen
			// sein.
			// Andernfalls soll der String um ein Leerzeichen erg�nzt und anschlie�end selbiges
			// ausgef�hrt werden.
			if(s.charAt(s.length() - 1) == ' ') {
				h.setKeyword(s.subSequence(0, s.indexOf(" ")).toString());
			} else {
				s += " ";
				h.setKeyword(s.subSequence(0, s.indexOf(" ")).toString());
			}
			
			// Zuletzt f�gen wir das generierte Hashtag der Ergebnismenge hinzu.
			hashtags.add(h);
		}

		return hashtags;
	}
}
