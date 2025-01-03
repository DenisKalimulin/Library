package com.example.demo.controllers;

import com.example.demo.DTO.UserDTO;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public UserDTO createUser(@RequestParam String name, @RequestParam String email) {
        return userService.createUser(name, email);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@Valid @PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok("User with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @RequestParam String name,
                                             @RequestParam String email) {
        boolean isUpdated = userService.updateUser(id, name, email);
        if (isUpdated) {
            return ResponseEntity.ok("User with ID " + id + " updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found.");
        }

    }

    @PatchMapping("/{userId}/books")
    public ResponseEntity<String> addBookToUser(@PathVariable Long userId,
                                                @RequestParam Long bookId) {
        boolean success = userService.addBookToUser(userId, bookId);
        if (success) {
            return ResponseEntity.ok("Book with ID " + bookId + " added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book with ID " + bookId + " not found.");
        }
    }

    @PatchMapping("/{userId}/books/{bookId}/remove")
    public ResponseEntity<String> removeBookFromUser(@PathVariable Long bookId,
                                                     @PathVariable Long userId) {
    boolean success = userService.removeBookFromUser(userId, bookId);

    if (success) {
        return ResponseEntity.ok("Book with ID " + bookId + " removed successfully.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User or Book not found, or Book does not belong to the User.");
    }
    }
}
