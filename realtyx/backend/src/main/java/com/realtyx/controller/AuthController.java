package com.realtyx.controller;

import com.realtyx.model.AdminUser;
import com.realtyx.service.JsonFileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class AuthController {
    
    @Autowired
    private JsonFileStore jsonFileStore;
    
    @GetMapping("/admin-users")
    public List<AdminUser> getAllAdminUsers() {
        return jsonFileStore.getAdminUsers();
    }
    
    @GetMapping("/admin-users/{id}")
    public AdminUser getAdminUserById(@PathVariable Long id) {
        return jsonFileStore.getAdminUsers().stream()
                .filter(admin -> admin.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
