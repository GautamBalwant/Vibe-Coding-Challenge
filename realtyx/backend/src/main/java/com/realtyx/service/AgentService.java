package com.realtyx.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtyx.model.Agent;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AgentService {
    private static final String DATA_FILE = "../data/agents.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Agent> getAllAgents() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Agent>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Agent> getAgentById(Long id) {
        return getAllAgents().stream()
                .filter(agent -> agent.getId().equals(id))
                .findFirst();
    }

    public Optional<Agent> getAgentByEmail(String email) {
        return getAllAgents().stream()
                .filter(agent -> agent.getEmail().equals(email))
                .findFirst();
    }

    public Agent saveAgent(Agent agent) {
        List<Agent> agents = getAllAgents();
        
        // Generate new ID if not present
        if (agent.getId() == null) {
            long maxId = agents.stream()
                    .mapToLong(Agent::getId)
                    .max()
                    .orElse(0L);
            agent.setId(maxId + 1);
        }
        
        // Update existing agent or add new one
        agents.removeIf(a -> a.getId().equals(agent.getId()));
        agents.add(agent);
        
        saveToFile(agents);
        return agent;
    }

    public boolean deleteAgent(Long id) {
        List<Agent> agents = getAllAgents();
        boolean removed = agents.removeIf(agent -> agent.getId().equals(id));
        if (removed) {
            saveToFile(agents);
        }
        return removed;
    }

    public List<Agent> getActiveAgents() {
        return getAllAgents().stream()
                .filter(Agent::getIsActive)
                .toList();
    }

    public List<Agent> searchAgents(String specialization) {
        return getAllAgents().stream()
                .filter(agent -> agent.getSpecialization().toLowerCase().contains(specialization.toLowerCase()))
                .toList();
    }

    private void saveToFile(List<Agent> agents) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), agents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
