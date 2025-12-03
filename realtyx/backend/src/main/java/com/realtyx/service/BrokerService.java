package com.realtyx.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.realtyx.model.Broker;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BrokerService {

    @Autowired
    private JsonFileStore jsonFileStore;

    private List<Broker> brokers = new ArrayList<>();

    @PostConstruct
    public void init() {
        loadBrokers();
    }

    private void loadBrokers() {
        brokers = jsonFileStore.readList("../data/brokers.json", new TypeReference<List<Broker>>() {});
        System.out.println("Loaded " + brokers.size() + " brokers from JSON");
    }

    public List<Broker> getAllBrokers() {
        return new ArrayList<>(brokers);
    }

    public Optional<Broker> getBrokerById(Long id) {
        return brokers.stream()
                .filter(broker -> broker.getId().equals(id))
                .findFirst();
    }
    
    public Broker addBroker(Broker broker) {
        brokers.add(broker);
        saveBrokers();
        return broker;
    }
    
    private void saveBrokers() {
        jsonFileStore.writeList("../data/brokers.json", brokers);
        System.out.println("Saved " + brokers.size() + " brokers to JSON");
    }
}
