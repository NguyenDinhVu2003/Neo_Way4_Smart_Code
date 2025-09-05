package security.instagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.instagram.controller.mapper.UserMapper;
import security.instagram.dto.UserDto;
import security.instagram.dto.UserProfile;
import security.instagram.entity.Role;
import security.instagram.entity.User;
import security.instagram.repository.UserRepository;
import security.instagram.utils.exception.InvalidRequestException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ket_ein17
 * @Date 5/27/2024
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserMapper userMapper;


    public UserDto createNewUser(UserDto request) {
        if (request.getEmail() == null
                || request.getEmail().equals("")
                || checkExistingEmail(request.getEmail(), request.getUsername())
        ) {
            throw new InvalidRequestException(LocalDateTime.now(), "ConstantMessages.INVALID_REQUEST",
                    String.format("ConstantMessages.INVALID_USERNAME", request.getUsername()), HttpStatus.BAD_REQUEST);
        }
        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .enabled(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        request.setUserId(savedUser.getId());
        request.setPassword(null);
        request.setConfirmPassword(null);
        return request;
    }

    @Override
    public UserProfile getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        UserProfile userProfile = userMapper.showProfile(user.get());
        return userProfile;
    }
    private boolean checkExistingEmail(String email, String username) {
        return userRepository.existsByEmail(email) || userRepository.existsByUsername(username);
    }
    private static boolean isNull(Object o){
        return Objects.isNull(o);
    }

    public UserProfile getUserByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), "User not found", String.format("No user with email: %s", email), HttpStatus.NOT_FOUND);
        }
        return userMapper.showProfile(userOpt.get());
    }

    public UserDto editUserByEmail(String email, UserDto dto) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), "User not found", String.format("No user with email: %s", email), HttpStatus.NOT_FOUND);
        }
        User user = userOpt.get();
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getPassword() != null) user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(user);
        dto.setUserId(savedUser.getId());
        dto.setPassword(null);
        dto.setConfirmPassword(null);
        return dto;
    }

    public void deleteUserByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new InvalidRequestException(LocalDateTime.now(), "User not found", String.format("No user with email: %s", email), HttpStatus.NOT_FOUND);
        }
        userRepository.delete(userOpt.get());
    }

    @Override
    public List<UserProfile> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::showProfile)
                .collect(Collectors.toList());
    }
}
