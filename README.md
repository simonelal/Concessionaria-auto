# Concessionaria Auto

Applicazione Java SE a console che simula una concessionaria auto.
L'utente può configurare un'auto scegliendo il tipo, aggiungere optional singoli o in pacchetti, visualizzare il riepilogo con il prezzo totale e salvarlo su file.


## Setup ed esecuzione

**Requisiti:** Java 17, Maven 3.8+

```bash
# Compilare ed eseguire i test
mvn clean verifygit 
=======
mvn clean verify
# Avviare l'applicazione
mvn exec:java
```

L'output viene stampato a console e salvato in `target/car.txt`.

---

## Struttura del progetto

```
src/
├── main/java/com/concessionaria/
│   ├── app/          Main.java
│   ├── catalog/      CarCatalog.java
│   ├── exceptions/   AppException.java, ExceptionShield.java
│   ├── factory/      CarFactory.java
│   ├── iterator/     OptionIterator.java
│   ├── model/        Car.java, CarType.java, OptionComponent.java,
│   │                 OptionLeaf.java, OptionBundle.java
│   ├── persistence/  CarFileRepository.java
│   ├── repository/   Repository.java
│   └── util/         AppLogger.java
└── test/java/com/concessionaria/
    └── AppTest.java
```

---

## Diagramma architetturale

```
┌─────────────────────────────────────────────────┐
│                     Main                        │
│  (punto di ingresso, orchestra tutti i moduli)  │
└────┬──────────┬──────────┬───────────┬──────────┘
     │          │          │           │
     ▼          ▼          ▼           ▼
CarFactory  CarCatalog  Repository  CarFileRepository
     │      (Singleton) (Generics)   (Java I/O)
     ▼
   Car
     │
     └──► OptionBundle (Composite root)
               ├──► OptionLeaf
               ├──► OptionLeaf
               └──► OptionBundle (nested)
                         └──► OptionLeaf
                              OptionIterator (DFS)

ExceptionShield ──► avvolge tutto il blocco main
AppLogger       ──► usato da ExceptionShield
```

---

## Diagramma UML delle classi

```
«interface»
OptionComponent
+ getName(): String
+ getPrice(): double
+ print(indent: String): String
        ▲               ▲
        │               │
  OptionLeaf      OptionBundle
  - name: String  - name: String
  - price: double - children: List<OptionComponent>
                  + add(OptionComponent)
                  + getChildren(): List

Car
- modelName: String
- type: CarType
- basePrice: double
- optionsRoot: OptionBundle
+ addOption(OptionComponent)
+ getTotalPrice(): double
+ printSummary(): String

CarFactory
+ create(type: CarType, modelName: String): Car   «static»

OptionIterator «implements» Iterator<OptionComponent>
- stack: Deque<Iterator<OptionComponent>>
- next: OptionComponent
+ hasNext(): boolean
+ next(): OptionComponent

Repository<T>
- store: Map<String, T>
+ save(key: String, value: T)
+ find(key: String): Optional<T>
+ remove(key: String)
+ size(): int

CarCatalog  «Singleton»
- instance: CarCatalog            «static»
- cars: List<Car>
+ getInstance(): CarCatalog       «static»
+ add(car: Car)
+ findByType(type: CarType): List<Car>
+ averagePrice(): double

CarFileRepository
- filePath: String
+ save(car: Car)
+ load(): String

AppException «extends» RuntimeException
ExceptionShield
+ runSafely(action: SafeRunnable)  «static»
«interface» SafeRunnable
+ run() throws Exception
```

---

## Pattern di progettazione

### Factory — `CarFactory`
Centralizza la creazione degli oggetti `Car`. Il chiamante passa solo il tipo e il nome; la logica del prezzo base è incapsulata nella factory.
Se il prezzo base di un SUV cambia, si modifica un solo punto nel codice invece di cercare ogni `new Car(...)` nel progetto.

---

### Composite — `OptionComponent`, `OptionLeaf`, `OptionBundle`
Permette di trattare optional singoli e pacchetti di optional in modo uniforme tramite l'interfaccia `OptionComponent`. `getPrice()` funziona ricorsivamente su tutta la gerarchia.
Il Composite supporta strutture ad albero di profondità arbitraria senza che il codice chiamante debba distinguere tra foglie e nodi.

---

### Iterator — `OptionIterator`
Scorre tutti gli optional dell'auto in profondità (DFS) senza esporre la struttura interna dell'albero. Usa un `ArrayDeque` come stack per gestire la navigazione ricorsiva in modo iterativo.
Il DFS mostra prima il pacchetto e poi i suoi contenuti, che è l'ordine in cui un cliente pensa agli optional.

---

### Exception Shielding — `ExceptionShield`, `AppException`
Intercetta tutte le eccezioni prima che raggiungano l'utente. L'utente vede solo messaggi puliti, mai stack trace.

`SafeRunnable` è un'interfaccia funzionale custom con `throws Exception`. Usare il `Runnable` standard non permetterebbe checked exceptions — verrebbero avvolte in `UndeclaredThrowableException` bypassando lo shield.

`AppException` estende `RuntimeException` per non dover dichiarare `throws` ovunque, mantenendo comunque la possibilità di distinguerla dalle eccezioni generiche nel catch.

---

## Tecnologie utilizzate

| Tecnologia | Dove | Motivazione |
|---|---|---|
| Collections Framework | `OptionBundle` (ArrayList), `OptionIterator` (ArrayDeque), `Repository` (HashMap), `CarCatalog` (List) | Ogni collezione scelta per le sue caratteristiche specifiche |
| Generics | `Repository<T>` | Classe generica custom riutilizzabile con qualsiasi tipo |
| Java I/O | `CarFileRepository` | Salvataggio e lettura del riepilogo auto su file di testo |
| Logging | `AppLogger`, `ExceptionShield` | Log separati per errori applicativi (WARNING) e imprevisti (SEVERE) |
| JUnit 5 | `AppTest` | Test unitari su tutti i moduli principali |

### Pattern opzionali aggiunti

| Pattern | Dove | Punti |
|---|---|---|
| Singleton | `CarCatalog` | Catalogo unico condiviso per tutto il ciclo di vita dell'applicazione |
| Stream API + Lambdas | `OptionBundle`, `CarCatalog`, `Main` | Sostituzione dei loop manuali con codice dichiarativo |

---

## Sicurezza

- **Nessun stack trace visibile all'utente** — `ExceptionShield` li intercetta tutti
- **Nessuna credenziale hardcoded** — il path del file è configurabile nel costruttore
- **Input sanitization** — ogni costruttore valida null, stringhe vuote e prezzi negativi
- **Exception propagation controllata** — solo `AppException` sale intenzionalmente; tutto il resto viene loggato e assorbito
- **Logging con placeholder** — si usa `{0}` invece di concatenazione stringa per evitare costruzione inutile della stringa

---

## Limitazioni note e sviluppi futuri

- Il `CarCatalog` è solo in memoria — al riavvio dell'applicazione si svuota. Per persistenza vera servirebbe un database o serializzazione JSON
- Il `Singleton` non è thread-safe: in contesto multi-thread servirebbe inizializzazione eager (`private static final CarCatalog INSTANCE = new CarCatalog()`)
- Nessun menu interattivo — il flusso è hardcoded in `Main.java`
- Nessuna interfaccia grafica

- La suite di test copre i casi principali ma non tutti i casi limite