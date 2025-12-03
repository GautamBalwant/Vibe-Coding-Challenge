package com.realtyx.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class JsonFileStore<T> {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ObjectMapper objectMapper;

    public JsonFileStore(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<T> readList(String filePath, TypeReference<List<T>> typeRef) {
        lock.readLock().lock();
        try {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, typeRef);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
            return new ArrayList<>();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void writeList(String filePath, List<T> data) {
        lock.writeLock().lock();
        try {
            File file = new File(filePath);
            File backupFile = new File(filePath + ".bak");
            
            // Create backup if file exists
            if (file.exists()) {
                Files.copy(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            
            // Write new data
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            
            // Delete backup on success
            if (backupFile.exists()) {
                backupFile.delete();
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + filePath + " - " + e.getMessage());
            // Try to restore backup
            File backupFile = new File(filePath + ".bak");
            if (backupFile.exists()) {
                try {
                    Files.copy(backupFile.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    System.err.println("Error restoring backup: " + ex.getMessage());
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
