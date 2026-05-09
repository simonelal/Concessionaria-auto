package com.concessionaria.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Generic repository — works for any domain object, not just Car.
// This satisfies the "custom Generics" requirement.
public class Repository<T> {

    private final Map<String, T> store = new HashMap<>();

    public void save(String key, T value) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("key must not be blank");
        if (value == null) throw new IllegalArgumentException("value must not be null");
        store.put(key, value);
    }

    public Optional<T> find(String key) {
        return Optional.ofNullable(store.get(key));
    }

    public void remove(String key) {
        store.remove(key);
    }

    public int size() {
        return store.size();
    }
}