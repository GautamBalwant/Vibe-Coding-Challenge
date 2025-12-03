package com.realtyx.controller;

import com.realtyx.model.Builder;
import com.realtyx.service.BuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/builders")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class BuilderController {

    @Autowired
    private BuilderService builderService;

    @GetMapping
    public List<Builder> getAllBuilders() {
        return builderService.getAllBuilders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Builder> getBuilderById(@PathVariable Long id) {
        Optional<Builder> builder = builderService.getBuilderById(id);
        return builder.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public List<Builder> getActiveBuilders() {
        return builderService.getActiveBuilders();
    }

    @PostMapping
    public Builder createBuilder(@RequestBody Builder builder) {
        return builderService.saveBuilder(builder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Builder> updateBuilder(@PathVariable Long id, @RequestBody Builder builder) {
        builder.setId(id);
        return ResponseEntity.ok(builderService.saveBuilder(builder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilder(@PathVariable Long id) {
        boolean deleted = builderService.deleteBuilder(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
