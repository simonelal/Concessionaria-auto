package com.concessionaria.model;

import com.concessionaria.exceptions.AppException;

public class OptionLeaf implements OptionComponent {

    private final String name;
    private final double price;

    public OptionLeaf(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException("Nome optional non valido");
        }
        if (price < 0) {
            throw new AppException("Prezzo optional negativo");
        }
        this.name = name.trim();
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String print(String indent) {
        return indent + "- " + name + " (EUR " + price + ")\n";
    }
}
