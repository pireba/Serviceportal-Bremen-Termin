package com.github.pireba.serviceportalbrementermin;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Terminprüfung {
	
	private static final String DATE_FORMAT = "EE dd.MM.yy 'um' HH:mm";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.GERMAN);
	
	private final Document document;
	
	public Terminprüfung(String url, LocalDate currentDate) throws IOException, ParseException {
		Connection connection = Jsoup.connect(url);
		this.document = connection.get();
		
		String newDateText = this.getNewDateText();
		if ( newDateText.isEmpty() ) {
			throw new IllegalStateException("Es konnte kein Datum abgerufen werden.");
		}
		LocalDateTime newDate = this.parseTimestamp(newDateText);
		
		if ( this.checkTimestamp(currentDate, newDate) ) {
			String location = this.getLocation();
			if ( location.isEmpty() ) {
				throw new IllegalStateException("Es konnte kein Standort abgerufen werden.");
			}
			
			String bookingLink = this.getBookingLink();
			if ( bookingLink.isEmpty() ) {
				throw new IllegalStateException("Es konnte kein Buchungslink abgerufen werden.");
			}
			
			System.out.println(this.createMessage(location, newDate, bookingLink));
		}
	}
	
	private boolean checkTimestamp(LocalDate currentDate, LocalDateTime newDate) {
		return newDate.toLocalDate().isBefore(currentDate);
	}
	
	private String createMessage(String location, LocalDateTime newDate, String bookingLink) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Es ist ein früherer Termin im ");
		sb.append(location);
		sb.append(" verfügbar:\n");
		sb.append(DATE_FORMATTER.format(newDate)+"\n");
		sb.append("\n");
		sb.append(bookingLink);
		
		return sb.toString();
	}
	
	private String getBookingLink() {
		return document.select("#collapse-stellen > div > a").attr("href");
	}
	
	private String getLocation() {
		return document.select("#collapse-stellen > div > strong").text();
	}
	
	private String getNewDateText() {
		return document.select("#collapse-stellen > div > a").text();
	}
	
	private LocalDateTime parseTimestamp(String timestamp) {
		return LocalDateTime.parse(timestamp, DATE_FORMATTER);
	}
}