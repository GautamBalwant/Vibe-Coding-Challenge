package com.realtyx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class JsonFileStoreTest {

    @Test
    void smoke() {
        JsonFileStore<Object> store = new JsonFileStore<>(new ObjectMapper());
        assert store != null;
    }
}
