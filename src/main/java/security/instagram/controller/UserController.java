package security.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.instagram.dto.UserDto;
import security.instagram.dto.UserProfile;
import security.instagram.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserProfile>> getAllUsers() {
        List<UserProfile> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserProfile> getUserByEmail(@PathVariable String email) {
        UserProfile user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<UserDto> editUserByEmail(@PathVariable String email, @RequestBody UserDto dto) {
        UserDto updatedUser = userService.editUserByEmail(email, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
