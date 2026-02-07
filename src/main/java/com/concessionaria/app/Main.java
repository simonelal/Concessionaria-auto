package com.concessionaria.app;

import com.concessionaria.exceptions.ExceptionShield;
import com.concessionaria.factory.CarFactory;
import com.concessionaria.iterator.OptionIterator;
import com.concessionaria.model.Car;
import com.concessionaria.model.CarType;
import com.concessionaria.model.OptionBundle;
import com.concessionaria.model.OptionComponent;
import com.concessionaria.model.OptionLeaf;
import com.concessionaria.persistence.CarFileRepository;

public class Main {
    public static void main(String[] args) {

        ExceptionShield.runSafely(() -> {

            Car car = CarFactory.create(CarType.SUV, "Kuga");

            OptionBundle sportPack = new OptionBundle("Sport Pack");
            sportPack.add(new OptionLeaf("Cerchi 19\"", 1200));
            sportPack.add(new OptionLeaf("Assetto sportivo", 900));

            car.addOption(sportPack);
            car.addOption(new OptionLeaf("Interni in pelle", 1500));

            CarFileRepository repo = new CarFileRepository();
            repo.save(car);

            System.out.println(car.printSummary());

            System.out.println("=== Lista optional (Iterator) ===");
            OptionIterator iterator = new OptionIterator(car.getOptionsRoot());
            while (iterator.hasNext()) {
                OptionComponent option = iterator.next();
                System.out.println("- " + option.getName() + " (EUR " + option.getPrice() + ")");
            }
        });
    }
}
