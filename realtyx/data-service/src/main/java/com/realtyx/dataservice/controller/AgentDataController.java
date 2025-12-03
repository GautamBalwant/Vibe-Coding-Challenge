package com.realtyx.dataservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.dataservice.model.Agent;
import com.realtyx.dataservice.service.DataAccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/data/agents")
public class AgentDataController {

    private final DataAccessService dataAccessService;
    private static final String FILE_KEY = "agents";
    private static final TypeReference<List<Agent>> TYPE_REF = new TypeReference<>() {};

    public AgentDataController(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

    @GetMapping
    public ResponseEntity<List<Agent>> getAllAgents() {
        List<Agent> agents = dataAccessService.findAll(FILE_KEY, TYPE_REF);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgentById(@PathVariable long id) {
        Optional<Agent> agent = dataAccessService.findById(FILE_KEY, TYPE_REF, id, a -> a.id);
        return agent.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Agent> getAgentByEmail(@PathVariable String email) {
        List<Agent> agents = dataAccessService.findByPredicate(
                FILE_KEY, TYPE_REF, a -> email.equalsIgnoreCase(a.email)
        );
        return agents.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(agents.get(0));
    }

    @PostMapping
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) {
        Agent saved = dataAccessService.save(FILE_KEY, TYPE_REF, agent, a -> a.id, (a, id) -> a.id = id);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agent> updateAgent(@PathVariable long id, @RequestBody Agent agent) {
        agent.id = id;
        Agent updated = dataAccessService.save(FILE_KEY, TYPE_REF, agent, a -> a.id, (a, newId) -> a.id = newId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable long id) {
        boolean deleted = dataAccessService.delete(FILE_KEY, TYPE_REF, id, a -> a.id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
