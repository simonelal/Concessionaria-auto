Questo progetto rappresenta una semplice applicazione Java console che simula una concessionaria auto.
L’utente può creare un’auto, aggiungere optional (anche raggruppati in pacchetti) e visualizzare il prezzo totale.

Tecnologie utilizzate:
-Java 17
-Maven
-JUnit 5
-Java Collections & Generics
-Java I/O
-java.util.logging

PATTERN DI PROGETTAZIONE UTILIZZATI:

-Factory Pattern
Classe: CarFactory
Utilizzato per creare oggetti Car in base al tipo di auto (CarType)
Permette di centralizzare la logica di creazione degli oggetti.

-Composite Pattern
Interfaccia: OptionComponent
Classi: OptionLeaf, OptionBundle
Permette di trattare optional singoli e pacchetti di optional in modo uniforme.

-Iterator Pattern
Classe: OptionIterator
Utilizzato per iterare sugli optional dell’auto, anche se annidati in pacchetti.
Implementa una visita in profondità (DFS)

-Exception Shielding
Classi: AppException, ExceptionShield
Le eccezioni vengono intercettate e gestite mostrando messaggi puliti all’utente
Gli errori tecnici vengono registrati tramite logging.


Gestione degli errori e Logging:
Gli errori applicativi sono gestiti tramite AppException.
La classe ExceptionShield intercetta le eccezioni e impedisce il crash del programma.
Il logging è implementato tramite java.util.logging con la classe AppLogger.


Java I/O:
Classe: CarFileRepository
Il riepilogo dell’auto viene salvato su file di testo (car.txt)
Il file viene creato nella root del progetto al momento dell’esecuzione

Test (JUnit):
sono stati implementati test unitari con JUnit 5.
Test sulla CarFactory per verificare la corretta creazione delle auto.
Test sul OptionBundle per verificare il calcolo corretto del prezzo totale.
I test possono essere eseguiti con:
mvn test


Possibili estensioni future:
Aggiunta di un menu interattivo per permettere all’utente di scegliere il tipo di auto e gli optional.
Gestione di più auto contemporaneamente.
Espansione della suite di test con test più completi e casi limite.
Aggiunta di una semplice interfaccia grafica.

