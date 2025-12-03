package com.realtyx.controller;

import com.realtyx.model.AdminUser;
import com.realtyx.service.JsonFileStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private AuthController controller;
    private MockedStatic<JsonFileStore> jsonFileStoreStatic;

    @BeforeEach
    void setup() {
        controller = new AuthController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() {
        if (jsonFileStoreStatic != null) {
            jsonFileStoreStatic.close();
        }
    }

    @Test
    void getAllAdminUsers_happyPath_returnsList() throws Exception {
        List<AdminUser> admins = List.of(createAdmin(1, "admin", "hash"), createAdmin(2, "admin2", "hash2"));

        jsonFileStoreStatic = mockStatic(JsonFileStore.class);
        jsonFileStoreStatic.when(JsonFileStore::getAdminUsers).thenReturn(admins);

        mockMvc.perform(get("/api/admin-users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("admin")));
    }

    @Test
    void getAdminUserById_happyPath_returnsUser() throws Exception {
        List<AdminUser> admins = List.of(createAdmin(5, "super", "h"));

        jsonFileStoreStatic = mockStatic(JsonFileStore.class);
        jsonFileStoreStatic.when(JsonFileStore::getAdminUsers).thenReturn(admins);

        mockMvc.perform(get("/api/admin-users/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.username", is("super")));
    }

    @Test
    void getAdminUserById_notFound_returnsNullBody() throws Exception {
        List<AdminUser> admins = List.of(createAdmin(10, "x", "h"));

        jsonFileStoreStatic = mockStatic(JsonFileStore.class);
        jsonFileStoreStatic.when(JsonFileStore::getAdminUsers).thenReturn(admins);

    mockMvc.perform(get("/api/admin-users/99"))
        .andExpect(status().isOk())
        // controller returns null -> empty response body
        .andExpect(content().string(""));
    }

    @Test
    void getAllAdminUsers_emptyList_returnsEmptyArray() throws Exception {
        jsonFileStoreStatic = mockStatic(JsonFileStore.class);
        jsonFileStoreStatic.when(JsonFileStore::getAdminUsers).thenReturn(List.of());

        mockMvc.perform(get("/api/admin-users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private AdminUser createAdmin(int id, String username, String hash) {
        AdminUser a = new AdminUser();
        a.id = id;
        a.username = username;
        a.passwordHash = hash;
        return a;
    }
}
