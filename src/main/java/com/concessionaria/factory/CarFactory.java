package com.concessionaria.factory;

import com.concessionaria.exceptions.AppException;
import com.concessionaria.model.Car;
import com.concessionaria.model.CarType;

public class CarFactory {

    private CarFactory() {}

    public static Car create(CarType type, String modelName) {
        if (type == null) {
            throw new AppException("Tipo auto non valido");
        }
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new AppException("Nome modello non valido");
        }

        switch (type) {
            case CITY:
                return new Car(modelName, type, 16000);
            case SUV:
                return new Car(modelName, type, 28000);
            case SPORT:
                return new Car(modelName, type, 45000);
            case ELECTRIC:
                return new Car(modelName, type, 38000);
            default:
                throw new AppException("Tipo auto non gestito");
        }
    }
}
