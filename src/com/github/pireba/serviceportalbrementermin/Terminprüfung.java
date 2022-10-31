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
import org.jsoup.select.Elements;

public class Terminprüfung {
	
	private static final String DATE_FORMAT = "EE dd.MM.yy 'um' HH:mm";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.GERMAN);
	
	private final Document document;
	
	public Terminprüfung(String url, LocalDate currentDate) throws IOException, ParseException {
		Connection connection = Jsoup.connect(url);
		this.document = connection.get();
		
		LocalDateTime newDate = this.getNewDate();
		
		if ( this.checkTimestamp(currentDate, newDate) ) {
			String location = this.getLocation();
			String bookingLink = this.getBookingLink();
			
			System.out.println(this.createMessage(location, newDate, bookingLink));
		}
	}
	
	public Terminprüfung(String url, LocalDate currentDate, String location) throws IOException, ParseException {
		Connection connection = Jsoup.connect(url);
		this.document = connection.get();
		
		LocalDateTime newDate = this.getNewDateForLocation(location);
		
		if ( this.checkTimestamp(currentDate, newDate) ) {
			String bookingLink = this.getBookingLinkForLocation(location);
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
	
	private Elements getElementsForLocation(String location) {
		Elements elements = document.select(".list > li:has(a:containsOwn("+location+"))");
		
		if ( elements == null || elements.isEmpty() ) {
			throw new IllegalStateException("Es konnten keine Elemente zu einem Standort abgerufen werden.");
		}
		
		return elements;
	}
	
	private String getLocation() {
		String location = document.select("#collapse-stellen > div > strong").text();
		
		if ( location == null || location.isEmpty() ) {
			throw new IllegalStateException("Es konnte kein Standort abgerufen werden.");
		}
		
		return location;
	}
	
	private LocalDateTime getNewDate() {
		String timestamp = document.select("#collapse-stellen > div > a").text();
		return this.parseTimestamp(timestamp);
	}
	
	private LocalDateTime getNewDateForLocation(String location) {
		Elements elements = this.getElementsForLocation(location);
		
		if ( elements.isEmpty() || elements.size() < 1 ) {
			throw new IllegalStateException("Es konnte kein Datum abgerufen werden.");
		}
		
		String timestamp = elements.get(0).ownText();
		return this.parseTimestamp(timestamp);
	}
	
	private LocalDateTime parseTimestamp(String timestamp) {
		if ( timestamp == null || timestamp.isEmpty() ) {
			throw new IllegalStateException("Das Datum ist leer und kann nicht umgewandelt werden.");
		}
		
		return LocalDateTime.parse(timestamp, DATE_FORMATTER);
	}
	
	private String getBookingLinkForLocation(String location) {
		Elements elements = this.getElementsForLocation(location);
		String bookingLink = elements.select("a:containsOwn(Frühestmöglicher Termin)").attr("href");
		
		if ( bookingLink == null || bookingLink.isEmpty() ) {
			throw new IllegalStateException("Es konnte kein Buchungslink abgerufen werden.");
		}
		
		return bookingLink;
	}
}