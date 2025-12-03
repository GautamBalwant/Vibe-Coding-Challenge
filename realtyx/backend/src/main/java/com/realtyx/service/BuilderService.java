package com.realtyx.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtyx.model.Builder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BuilderService {
    private static final String DATA_FILE = "../data/builders.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Builder> getAllBuilders() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Builder>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Builder> getBuilderById(Long id) {
        return getAllBuilders().stream()
                .filter(builder -> builder.getId().equals(id))
                .findFirst();
    }

    public Optional<Builder> getBuilderByEmail(String email) {
        return getAllBuilders().stream()
                .filter(builder -> builder.getEmail().equals(email))
                .findFirst();
    }

    public Builder saveBuilder(Builder builder) {
        List<Builder> builders = getAllBuilders();
        
        // Generate new ID if not present
        if (builder.getId() == null) {
            long maxId = builders.stream()
                    .mapToLong(Builder::getId)
                    .max()
                    .orElse(0L);
            builder.setId(maxId + 1);
        }
        
        // Update existing builder or add new one
        builders.removeIf(b -> b.getId().equals(builder.getId()));
        builders.add(builder);
        
        saveToFile(builders);
        return builder;
    }

    public boolean deleteBuilder(Long id) {
        List<Builder> builders = getAllBuilders();
        boolean removed = builders.removeIf(builder -> builder.getId().equals(id));
        if (removed) {
            saveToFile(builders);
        }
        return removed;
    }

    public List<Builder> getActiveBuilders() {
        return getAllBuilders().stream()
                .filter(Builder::getIsActive)
                .toList();
    }

    private void saveToFile(List<Builder> builders) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), builders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
