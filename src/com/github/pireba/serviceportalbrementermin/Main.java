package com.github.pireba.serviceportalbrementermin;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {
	
	public static void main(String[] args) {
		if ( args.length < 2 ) {
			printUsage();
		}
		
		String url = args[0];
		try {
			new URL(url);
		} catch (MalformedURLException e) {
			printUsage("Es wurde keine gültige URL übergeben.");
		}
		
		String dateString = args[1];
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = null;
		try {
			date = LocalDate.parse(dateString, formatter);
		} catch (DateTimeParseException e) {
			printUsage("Es wurde kein gültiges Datum übergeben: YYYY-MM-DD");
		}
		
		try {
			if ( args.length == 3 ) {
				String location = args[2];
				new Terminprüfung(url, date, location);
			} else {
				new Terminprüfung(url, date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void printUsage() {
		printUsage(null);
	}
	
	private static void printUsage(String message) {
		StringBuilder sb = new StringBuilder();
		
		if ( message != null && ! message.isEmpty() ) {
			sb.append(message+"\n");
			sb.append("\n");
		}
		
		sb.append("ANWENDUNG\n");
		sb.append("    java -jar serviceportal-bremen-termin.jar [url] [datum]\n");
		sb.append("\n");
		sb.append("    Dieses Tool ruft den nächsten freien Termin aus dem Serviceportal\n");
		sb.append("    Bremen ab und prüft ob dieser vor dem übergebenen Datum liegt.\n");
		sb.append("\n");
		sb.append("ARGUMENTE:\n");
		sb.append("    [url]        Die URL zum Serviceportal Bremen.\n");
		sb.append("    [datum]      Das Datum gegen welches geprüft werden soll.\n");
		sb.append("                 Format: yyyy-mm-dd\n");
		sb.append("    [standort]   Der Standort der abgefragt werden soll. (Optional)\n");
		sb.append("\n");
		sb.append("BEISPIEL:\n");
		sb.append("    java -jar serviceportal-bremen-termin.jar \"https://www.service.bremen.de/dienstleistungen/internationalen-fuehrerschein-beantragen-8356\" \"2022-10-05\"\n");
		sb.append("\n");
		sb.append("    java -jar serviceportal-bremen-termin.jar \"https://www.service.bremen.de/dienstleistungen/internationalen-fuehrerschein-beantragen-8356\" \"2022-10-05\" \"BürgerServiceCenter-Mitte\"\n");
		
		System.err.println(sb.toString());
		System.exit(1);
	}
}