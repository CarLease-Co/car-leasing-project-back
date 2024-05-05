package com.carlease.project.user;

import com.carlease.project.email.EmailService;
import com.carlease.project.email.EmailTemplates;
import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.IncorrectPasswordException;
import com.carlease.project.exceptions.UserException;
import com.carlease.project.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll(long userId, UserRole role) throws UserNotFoundException, UserException {
        validateUserRole(userRepository, userId, role);

        if (!UserRole.SYSTEM_ADMIN.equals(role))
            throw new UserException("User role does not match the provided role");

        return userRepository.findAll();
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserSession login(String username, String password) throws IncorrectPasswordException {
        User user = userRepository.findByUsername(username);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect username or password");
        }

        UserSession userSession = new UserSession();
        userSession.setUserId(user.getUserId());
        userSession.setRole(user.getRole());
        return userSession;
    }

    @Override
    public User createUser(User user, long userId, UserRole role) throws UserNotFoundException, UserException {
        validateUserRole(userRepository, userId, role);

        if (!UserRole.SYSTEM_ADMIN.equals(role))
            throw new UserException("User role does not match the provided role");

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        EmailService.sendEmail("Welcome to CarLess!", String.format(EmailTemplates.NEW_USER, user.getName(), "CarLess", "CarLess Team"), user.getEmail());

        return userRepository.save(user);
    }

    public static void validateUserRole(UserRepository userRepository, long id, UserRole role) throws UserException, UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getRole().equals(role)) {
            throw new UserException("User role does not match the provided role");
        }
    }
}