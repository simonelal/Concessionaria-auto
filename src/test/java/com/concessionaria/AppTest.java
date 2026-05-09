package com.concessionaria;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.concessionaria.catalog.CarCatalog;
import com.concessionaria.exceptions.AppException;
import com.concessionaria.exceptions.ExceptionShield;
import com.concessionaria.factory.CarFactory;
import com.concessionaria.iterator.OptionIterator;
import com.concessionaria.model.Car;
import com.concessionaria.model.CarType;
import com.concessionaria.model.OptionBundle;
import com.concessionaria.model.OptionLeaf;
import com.concessionaria.persistence.CarFileRepository;
import com.concessionaria.repository.Repository;

class AppTest {

    // factory

    @Test
    void factory_allTypes_correctPrices() {
        assertEquals(16000, CarFactory.create(CarType.CITY,     "Panda").getBasePrice());
        assertEquals(28000, CarFactory.create(CarType.SUV,      "Kuga").getBasePrice());
        assertEquals(45000, CarFactory.create(CarType.SPORT,    "Lotus").getBasePrice());
        assertEquals(38000, CarFactory.create(CarType.ELECTRIC, "BYD").getBasePrice());
    }

    @Test
    void factory_invalidInput_throws() {
        assertThrows(AppException.class, () -> CarFactory.create(null, "X"));
        assertThrows(AppException.class, () -> CarFactory.create(CarType.SUV, ""));
    }

    // composite

    @Test
    void bundle_totalPrice_includesNested() {
        OptionBundle inner = new OptionBundle("Inner");
        inner.add(new OptionLeaf("A", 500));

        OptionBundle outer = new OptionBundle("Outer");
        outer.add(inner);
        outer.add(new OptionLeaf("B", 300));

        assertEquals(800, outer.getPrice());
    }

    @Test
    void bundle_nullChild_throws() {
        assertThrows(AppException.class, () -> new OptionBundle("Pack").add(null));
    }

    // iterator

    @Test
    void iterator_flattensDFS() {
        OptionBundle root = new OptionBundle("Root");
        OptionBundle sub  = new OptionBundle("Sub");
        sub.add(new OptionLeaf("A", 100));
        root.add(sub);
        root.add(new OptionLeaf("B", 200));

        OptionIterator it = new OptionIterator(root);
        assertEquals("Sub", it.next().getName());
        assertEquals("A",   it.next().getName());
        assertEquals("B",   it.next().getName());
        assertFalse(it.hasNext());
    }

    // exception shielding

    @Test
    void shield_neverCrashes() {
        assertDoesNotThrow(() -> ExceptionShield.runSafely(() -> { throw new AppException("err"); }));
        assertDoesNotThrow(() -> ExceptionShield.runSafely(() -> { throw new RuntimeException("boom"); }));
    }

    // java i/o

    @Test
    void fileRepo_saveAndLoad(@TempDir Path tmp) {
        Car car = CarFactory.create(CarType.SUV, "Kuga");
        car.addOption(new OptionLeaf("Pelle", 1500));

        CarFileRepository repo = new CarFileRepository(tmp.resolve("car.txt").toString());
        repo.save(car);

        String content = repo.load();
        assertTrue(content.contains("Kuga"));
        assertTrue(content.contains("Pelle"));
    }

    // generics

    @Test
    void genericRepo_saveAndFind() {
        Repository<Car> repo = new Repository<>();
        Car car = CarFactory.create(CarType.CITY, "Panda");
        repo.save("panda", car);

        assertTrue(repo.find("panda").isPresent());
        assertFalse(repo.find("ghost").isPresent());
    }

    // singleton + streams

    @Test
    void catalog_singleton_andSearch() {
        CarCatalog catalog = CarCatalog.getInstance();
        catalog.add(CarFactory.create(CarType.ELECTRIC, "BYD"));

        assertSame(catalog, CarCatalog.getInstance());
        assertTrue(catalog.findByType(CarType.ELECTRIC)
            .stream().anyMatch(c -> c.getModelName().equals("BYD")));
    }
}