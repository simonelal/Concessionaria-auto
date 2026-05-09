package com.concessionaria.model;

import java.util.ArrayList;
import java.util.List;

import com.concessionaria.exceptions.AppException;

public class OptionBundle implements OptionComponent {

    private final String name;
    private final List<OptionComponent> children = new ArrayList<>();

    public OptionBundle(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException("Nome pacchetto non valido");
        }
        this.name = name.trim();
    }

    public void add(OptionComponent component) {
        if (component == null) {
            throw new AppException("Optional nullo");
        }
        children.add(component);
    }

    public List<OptionComponent> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return children.stream()
            .mapToDouble(OptionComponent::getPrice)
            .sum();
    }

    @Override
    public String print(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent)
          .append("+ ")
          .append(name)
          .append(" (EUR ")
          .append(getPrice())
          .append(")\n");

        children.forEach(c -> sb.append(c.print(indent + "  ")));

        return sb.toString();
    }
}