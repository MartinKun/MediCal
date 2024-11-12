package com.app.controller;

import com.app.controller.dto.request.UserProfileRequest;
import com.app.controller.dto.response.UserProfileResponse;
import com.app.persistence.entity.User;
import com.app.service.implementation.CloudinaryServiceImpl;
import com.app.service.implementation.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    CloudinaryServiceImpl cloudinaryService;

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @AuthenticationPrincipal String username,
            @ModelAttribute UserProfileRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file
    ) {
        User user = (User) userDetailService.loadUserByUsername(username);

        String image = file != null ? cloudinaryService.uploadFile(file) : null;

        if(image != null) user.setImage(image);

        UserProfileResponse response = userDetailService.updateUserProfile(request, user);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<User> getUserProfile(
            @AuthenticationPrincipal String username
    ) {
        User response = (User) userDetailService.loadUserByUsername(username);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(
            @AuthenticationPrincipal String username
    ) {
        User user = (User) userDetailService.loadUserByUsername(username);

        userDetailService.deleteUser(user);

        return ResponseEntity.ok("User was deleted successfully.");
    }
}
