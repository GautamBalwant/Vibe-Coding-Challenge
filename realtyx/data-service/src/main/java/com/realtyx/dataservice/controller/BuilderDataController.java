package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Builder;
import com.realtyx.dataservice.service.DataAccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/data/builders")
public class BuilderDataController {

    private final DataAccessService dataAccessService;
    private static final String FILE_KEY = "builders";
    private static final TypeReference<List<Builder>> TYPE_REF = new TypeReference<>() {};

    public BuilderDataController(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

    @GetMapping
    public ResponseEntity<List<Builder>> getAllBuilders() {
        List<Builder> builders = dataAccessService.findAll(FILE_KEY, TYPE_REF);
        return ResponseEntity.ok(builders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Builder> getBuilderById(@PathVariable long id) {
        Optional<Builder> builder = dataAccessService.findById(FILE_KEY, TYPE_REF, id, b -> b.id);
        return builder.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Builder> createBuilder(@RequestBody Builder builder) {
        Builder saved = dataAccessService.save(FILE_KEY, TYPE_REF, builder, b -> b.id, (b, id) -> b.id = id);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Builder> updateBuilder(@PathVariable long id, @RequestBody Builder builder) {
        builder.id = id;
        Builder updated = dataAccessService.save(FILE_KEY, TYPE_REF, builder, b -> b.id, (b, newId) -> b.id = newId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilder(@PathVariable long id) {
        boolean deleted = dataAccessService.delete(FILE_KEY, TYPE_REF, id, b -> b.id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
