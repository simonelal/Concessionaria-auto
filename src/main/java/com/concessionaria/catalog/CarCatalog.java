package com.concessionaria.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.concessionaria.model.Car;
import com.concessionaria.model.CarType;

// Singleton — one catalog per application lifecycle.
// Stream API used intentionally for filtering.
public class CarCatalog {

    private static CarCatalog instance;

    private final List<Car> cars = new ArrayList<>();

    private CarCatalog() {}

    public static CarCatalog getInstance() {
        if (instance == null) {
            instance = new CarCatalog();
        }
        return instance;
    }

    public void add(Car car) {
        if (car == null) throw new IllegalArgumentException("car must not be null");
        cars.add(car);
    }

    public List<Car> findByType(CarType type) {
        return cars.stream()
            .filter(c -> c.getType() == type)
            .collect(Collectors.toList());
    }

    public double averagePrice() {
        return cars.stream()
            .mapToDouble(Car::getTotalPrice)
            .average()
            .orElse(0);
    }

    public List<Car> all() {
        return List.copyOf(cars);
    }
}