package com.example.user_service;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final UserPublisher userPublisher;

    public UserController(UserService userService, final UserPublisher userPublisher) {
        this.userService = userService;
        this.userPublisher = userPublisher;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<String> bulkCreateUsers(@RequestParam(defaultValue = "10000") int count) {
        IntStream.range(1, count + 1).forEach(i -> {
            User user = new User(
                    (long) i,
                    "User" + i,
                    "user" + i + "_" + UUID.randomUUID().toString().substring(0, 6) + "@example.com"
            );
            userPublisher.publishUserCreate(user);
        });

        return ResponseEntity.ok("✅ " + count + " kullanıcı gönderildi.");
    }


    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody User user){
        userPublisher.publishUserCreate(user);
        return ResponseEntity.ok("kullanıcı oluşturuldu"+ user.getName());

    }
}
