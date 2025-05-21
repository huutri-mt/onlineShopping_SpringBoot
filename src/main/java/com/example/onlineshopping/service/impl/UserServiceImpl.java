package com.example.onlineshopping.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.example.onlineshopping.dto.Request.PasswordCreationRequest;
import com.example.onlineshopping.dto.Request.UserChangePassword;
import com.example.onlineshopping.dto.Request.UserCreationRequest;
import com.example.onlineshopping.dto.Request.UserUpdateRequest;
import com.example.onlineshopping.dto.Response.UserResponse;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.exception.AppException;
import com.example.onlineshopping.exception.ErrorCode;
import com.example.onlineshopping.mapper.UserMapper;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Primary
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User creatUser(UserCreationRequest request) {
        User user = new User();
        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('admin')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('admin')")
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
    }

    @PreAuthorize("hasRole('admin')")
    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return user;
    }

    public UserResponse getMyInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setNoPassword(!StringUtils.hasText(user.getPassword()));
        return userResponse;
    }

    @PreAuthorize("hasRole('admin') or #id == authentication.token.claims['userId']")
    public User updateUser(UserUpdateRequest request, int id) {
        User existingUser = getUserById(id);

        if (existingUser == null) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Nếu dữ liệu không thay đổi thì thông báo
        boolean noChange = existingUser.getEmail().equals(request.getEmail())
                && existingUser.getFullname().equals(request.getFullname());

        if (noChange) {
            throw new AppException(ErrorCode.NO_CHANGE_TO_UPDATE);
        }

        // Nếu email đã tồn tại ở người khác
        if (userRepository.existsUserByEmail(request.getEmail())
                && !existingUser.getEmail().equals(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        int rowEffects = userRepository.updateUser(request.getEmail(), request.getFullname(), id);

        if (rowEffects == 0) {
            throw new AppException(ErrorCode.UPDATE_FAILED);
        }

        return getUserById(id);
    }

    @PreAuthorize("hasRole('admin') or #id == authentication.token.claims['userId']")
    public void deleteUser(int id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @PreAuthorize("hasRole('admin') or #id == authentication.token.claims['userId']")
    public Boolean changePassword(int id, UserChangePassword userChangePassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        if (!passwordEncoder.matches(userChangePassword.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(userChangePassword.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @PreAuthorize("hasRole('admin')")
    public void blockUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        user.setStatus("block");
        userRepository.save(user);
    }

    public void createPassword(PasswordCreationRequest request) {
        log.info("request {}", request);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (StringUtils.hasText(user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_EXISTED);

        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        log.info("Password created for user {}", user.getEmail());
        userRepository.save(user);
    }


}
