package com.github.marekp95.rest.example.storage.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Repository<T> {

    private final Map<UUID, T> entryMap = new HashMap<>();

    protected void insert(UUID id, T data) {
        entryMap.put(id, data);
    }

    public void remove(UUID id) {
        entryMap.remove(id);
    }

    public Optional<T> find(UUID id) {
        return Optional.ofNullable(entryMap.get(id));
    }

    public Collection<T> findAll() {
        return entryMap.values();
    }
}
