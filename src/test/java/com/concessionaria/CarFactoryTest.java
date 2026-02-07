package com.concessionaria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.concessionaria.factory.CarFactory;
import com.concessionaria.model.Car;
import com.concessionaria.model.CarType;

public class CarFactoryTest {

    @Test
    void createSUV_setsBasePrice28000() {
        Car car = CarFactory.create(CarType.SUV, "Kuga");

        assertEquals(CarType.SUV, car.getType());
        assertEquals("Kuga", car.getModelName());
        assertEquals(28000.0, car.getBasePrice());
    }
}
