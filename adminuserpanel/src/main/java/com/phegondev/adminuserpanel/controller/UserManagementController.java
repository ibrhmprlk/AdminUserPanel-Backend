package com.phegondev.adminuserpanel.controller;

import com.phegondev.adminuserpanel.dto.ReqRes;
import com.phegondev.adminuserpanel.entity.OurUsers;
import com.phegondev.adminuserpanel.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementController {

    @Autowired
    private UsersManagementService usersManagementService;

    // --- AUTHENTICATION ENDPOINTS ---

    /**
     * YOL: POST /auth/register ve POST /auth/createaccount
     * İki farklı kaydolma yolunu tek bir metodla işlemek için @PostMapping'e String[] array formatında yollar tanımlandı.
     * Bu, "Metod imzası çakışması" (Duplicate method signature) hatasını giderir.
     */

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes req) {
        return ResponseEntity.ok(usersManagementService.register(req));
    }
    @PostMapping("/auth/create")
    public ResponseEntity<ReqRes>create(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.create(req));
    }

    // YOL: POST /auth/login
    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req) {
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    // YOL: POST /auth/refresh
    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req) {
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    // --- ADMIN ENDPOINTS ---

    // YOL: GET /admin/get-all-users
    // Bu endpoint'e erişim SecurityConfig'te ADMIN yetkisi gerektirir.
    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers() {
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    // YOL: GET /admin/get-users/{userId}
    // Bu endpoint'e erişim SecurityConfig'te ADMIN yetkisi gerektirir.
    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(usersManagementService.getUsersById(userId));
    }

    // YOL: PUT /admin/update/{userId}
    // Bu endpoint'e erişim SecurityConfig'te ADMIN yetkisi gerektirir.
    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody OurUsers reqres) {
        return ResponseEntity.ok(usersManagementService.updateUser(userId, reqres));
    }

    @PutMapping("/user/updateuser/{userId}")
    public ResponseEntity<ReqRes> userUpdate(@PathVariable Integer userId, @RequestBody OurUsers reqres) {
        return ResponseEntity.ok(usersManagementService.updateUser(userId, reqres));
    }

    // YOL: DELETE /admin/delete/{userId}
    // Bu endpoint'e erişim SecurityConfig'te ADMIN yetkisi gerektirir.
    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }

    // --- ADMIN/USER ENDPOINTS ---

    // YOL: GET /adminuser/get-profile
    // Bu endpoint'e erişim SecurityConfig'te hem ADMIN hem de USER yetkisi gerektirir.
    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile() {
        // Güvenlik bağlamından (Security Context) oturum açmış kullanıcının Authentication objesini alır.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Kullanıcının principal (genellikle email/username) bilgisini alır.
        String email = authentication.getName();

        // Kullanıcının bilgilerini servis aracılığıyla çeker.
        ReqRes response = usersManagementService.getMyInfo(email);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}