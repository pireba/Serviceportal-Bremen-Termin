# Serviceportal-Bremen-Termin

Dieses Tool ruft den nächsten freien Termin aus dem Serviceportal Bremen ab und prüft ob dieser vor dem übergebenen Datum liegt.

# Intention

Auf einen freien Termin beim BürgerServiceCenter in Bremen muss man in der Regel ca. 3 Monate warten.
Es werden allerdings regelmäßig Termine abgesagt, die dann wieder zur Verfügung stehen.
Ich rufe dieses Tool alle x Minuten über die Crontab auf und lasse mir die Ausgabe per Mail senden. Somit erhalte ich sofort eine Mitteilung sobald ein Termin frei geworden ist.

# Anwendung

```
ANWENDUNG
    java -jar serviceportal-bremen-termin.jar [url] [datum]

    Dieses Tool ruft den nächsten freien Termin aus dem Serviceportal
    Bremen ab und prüft ob dieser vor dem übergebenen Datum liegt.

ARGUMENTE:
    [url]        Die URL zum Serviceportal Bremen.
    [datum]      Das Datum gegen welches geprüft werden soll.
                 Format: yyyy-mm-dd
BEISPIEL:
    java -jar serviceportal-bremen-termin.jar https://www.service.bremen.de/dienstleistungen/internationalen-fuehrerschein-beantragen-8356 2022-10-05
```

# Beispiel

```
java -jar serviceportal-bremen-termin.jar "https://www.service.bremen.de/dienstleistungen/internationalen-fuehrerschein-beantragen-8356" "2022-10-05"
```

Ausgabe:

```
Es ist ein früherer Termin im BürgerServiceCenter-Mitte verfügbar:
Mo. 22.08.22 um 08:45

https://termin.bremen.de/termine/directentry?mdt=5&loc=4&cnc-140=1&date=2022-08-22&time=08:45
```

# Build

```
mvn clean install
```
