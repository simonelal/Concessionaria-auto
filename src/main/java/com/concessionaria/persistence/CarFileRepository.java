package com.concessionaria.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.concessionaria.exceptions.AppException;
import com.concessionaria.model.Car;

public class CarFileRepository {

    private final String filePath;

    // Default constructor keeps the original behaviour
    public CarFileRepository() {
        this("target/car.txt");
    }

    // Configurable path — useful for testing with @TempDir
    public CarFileRepository(String filePath) {
        if (filePath == null || filePath.isBlank()) throw new AppException("File path non valido");
        this.filePath = filePath;
    }

    public void save(Car car) {
        if (car == null) throw new AppException("Auto nulla");
        try {
            Path path = Path.of(filePath);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            Files.writeString(path, car.printSummary());
        } catch (IOException e) {
            throw new AppException("Errore salvataggio file", e);
        }
    }

    public String load() {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new AppException("Errore lettura file", e);
        }
    }
}