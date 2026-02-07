package com.concessionaria.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.concessionaria.exceptions.AppException;
import com.concessionaria.model.Car;

public class CarFileRepository {

    private static final String FILE_NAME = "target/car.txt";

    public void save(Car car) {
        if (car == null) {
            throw new AppException("Auto nulla");
        }

        try {
            Files.writeString(Path.of(FILE_NAME), car.printSummary());
        } catch (IOException e) {
            throw new AppException("Errore salvataggio file", e);
        }
    }
}
