package com.concessionaria.model;

import com.concessionaria.exceptions.AppException;

public class Car {

    private final String modelName;
    private final CarType type;
    private final double basePrice;

    private final OptionBundle optionsRoot = new OptionBundle("Optional");

    public Car(String modelName, CarType type, double basePrice) {
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new AppException("Nome modello non valido");
        }
        if (type == null) {
            throw new AppException("Tipo auto non valido");
        }
        if (basePrice < 0) {
            throw new AppException("Prezzo base negativo");
        }

        this.modelName = modelName.trim();
        this.type = type;
        this.basePrice = basePrice;
    }

    public String getModelName() {
        return modelName;
    }

    public CarType getType() {
        return type;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void addOption(OptionComponent option) {
        optionsRoot.add(option);
    }

    public OptionBundle getOptionsRoot() {
        return optionsRoot;
    }

    public double getTotalPrice() {
        return basePrice + optionsRoot.getPrice();
    }

    public String printSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Auto: ").append(modelName)
          .append(" (").append(type).append(")\n");
        sb.append("Prezzo base: EUR ").append(basePrice).append("\n");
        sb.append(optionsRoot.print(""));
        sb.append("TOTALE: EUR ").append(getTotalPrice()).append("\n");
        return sb.toString();
    }
}
