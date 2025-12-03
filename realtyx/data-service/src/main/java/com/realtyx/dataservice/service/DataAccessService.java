package com.realtyx.dataservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.config.DataSourceConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DataAccessService {
    
    private final JsonFileStore<Object> jsonFileStore;
    private final DataSourceConfig dataSourceConfig;

    public DataAccessService(JsonFileStore<Object> jsonFileStore, DataSourceConfig dataSourceConfig) {
        this.jsonFileStore = jsonFileStore;
        this.dataSourceConfig = dataSourceConfig;
    }

    // Generic CRUD operations
    public <T> List<T> findAll(String fileKey, TypeReference<List<T>> typeRef) {
        String filePath = dataSourceConfig.getFilePath(fileKey);
        return (List<T>) jsonFileStore.readList(filePath, (TypeReference<List<Object>>) (TypeReference<?>) typeRef);
    }

    public <T> Optional<T> findById(String fileKey, TypeReference<List<T>> typeRef, long id, IdExtractor<T> idExtractor) {
        List<T> items = findAll(fileKey, typeRef);
        return items.stream()
                .filter(item -> idExtractor.getId(item) == id)
                .findFirst();
    }

    public <T> List<T> findByPredicate(String fileKey, TypeReference<List<T>> typeRef, Predicate<T> predicate) {
        List<T> items = findAll(fileKey, typeRef);
        return items.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public <T> T save(String fileKey, TypeReference<List<T>> typeRef, T item, IdExtractor<T> idExtractor, IdSetter<T> idSetter) {
        List<T> items = findAll(fileKey, typeRef);
        long id = idExtractor.getId(item);
        
        if (id == 0) {
            // New item - generate ID
            long maxId = items.stream()
                    .mapToLong(idExtractor::getId)
                    .max()
                    .orElse(0);
            idSetter.setId(item, maxId + 1);
            items.add(item);
        } else {
            // Update existing item
            Optional<T> existing = items.stream()
                    .filter(i -> idExtractor.getId(i) == id)
                    .findFirst();
            
            if (existing.isPresent()) {
                items.remove(existing.get());
                items.add(item);
            } else {
                items.add(item);
            }
        }
        
        String filePath = dataSourceConfig.getFilePath(fileKey);
        jsonFileStore.writeList(filePath, (List<Object>) items);
        return item;
    }

    public <T> boolean delete(String fileKey, TypeReference<List<T>> typeRef, long id, IdExtractor<T> idExtractor) {
        List<T> items = findAll(fileKey, typeRef);
        boolean removed = items.removeIf(item -> idExtractor.getId(item) == id);
        
        if (removed) {
            String filePath = dataSourceConfig.getFilePath(fileKey);
            jsonFileStore.writeList(filePath, (List<Object>) items);
        }
        
        return removed;
    }

    // Functional interfaces for ID operations
    @FunctionalInterface
    public interface IdExtractor<T> {
        long getId(T item);
    }

    @FunctionalInterface
    public interface IdSetter<T> {
        void setId(T item, long id);
    }
}
