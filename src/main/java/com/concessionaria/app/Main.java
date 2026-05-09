package com.concessionaria.app;

import com.concessionaria.catalog.CarCatalog;
import com.concessionaria.exceptions.ExceptionShield;
import com.concessionaria.factory.CarFactory;
import com.concessionaria.iterator.OptionIterator;
import com.concessionaria.model.Car;
import com.concessionaria.model.CarType;
import com.concessionaria.model.OptionBundle;
import com.concessionaria.model.OptionComponent;
import com.concessionaria.model.OptionLeaf;
import com.concessionaria.persistence.CarFileRepository;
import com.concessionaria.repository.Repository;

public class Main {
    public static void main(String[] args) {

        ExceptionShield.runSafely(() -> {

            // Factory pattern — crea auto in base al tipo
            Car car = CarFactory.create(CarType.SUV, "Kuga");

            // Composite pattern — optional singoli e pacchetti trattati allo stesso modo
            OptionBundle sportPack = new OptionBundle("Sport Pack");
            sportPack.add(new OptionLeaf("Cerchi 19\"", 1200));
            sportPack.add(new OptionLeaf("Assetto sportivo", 900));

            car.addOption(sportPack);
            car.addOption(new OptionLeaf("Interni in pelle", 1500));

            // Java I/O — salva su file
            CarFileRepository fileRepo = new CarFileRepository();
            fileRepo.save(car);

            // Generics — repository generico in memoria
            Repository<Car> memRepo = new Repository<>();
            memRepo.save(car.getModelName(), car);

            // Singleton + Stream API — catalogo globale con ricerca
            CarCatalog catalog = CarCatalog.getInstance();
            catalog.add(car);
            catalog.add(CarFactory.create(CarType.CITY, "Panda"));
            catalog.add(CarFactory.create(CarType.ELECTRIC, "BYD"));

            // stampa riepilogo auto
            System.out.println(car.printSummary());

            // Iterator pattern — scorre gli optional in profondità (DFS)
            System.out.println("=== Lista optional (Iterator) ===");
            OptionIterator iterator = new OptionIterator(car.getOptionsRoot());
            while (iterator.hasNext()) {
                OptionComponent option = iterator.next();
                System.out.println("- " + option.getName() + " (EUR " + option.getPrice() + ")");
            }

            // Stream API — filtra per tipo e calcola media
            System.out.println("\n=== Catalogo (Stream API) ===");
            System.out.println("Auto elettriche: " + catalog.findByType(CarType.ELECTRIC).size());
            System.out.println("Prezzo medio catalogo: EUR " + catalog.averagePrice());
            System.out.println("Auto in memoria (Repository): " + memRepo.size());
        });
    }
}