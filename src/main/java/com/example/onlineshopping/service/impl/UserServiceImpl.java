package com.example.onlineshopping.service.impl;
import com.example.onlineshopping.dto.Request.UserChangePassword;
import com.example.onlineshopping.dto.Request.UserCreationRequest;
import com.example.onlineshopping.dto.Request.UserUpdateRequest;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.mapper.UserMapper;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

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
        if(userRepository.existsUserByEmail(request.getEmail())){
            throw new RuntimeException("Email da ton tai");
        }

        user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }
    @PreAuthorize("hasRole('admin')")
    public List<User> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    @PostAuthorize("hasRole('admin')")
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay User"));
    }

    public User getByEmail (String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new RuntimeException("Khong tim thay User");
        }
        return user;
    }

    public User updateUser(UserUpdateRequest request, int id) {
        User user = getUserById(id);
        if(userRepository.existsUserByEmail(request.getEmail())){
            throw new RuntimeException("Email da ton tai");
        }
        user = userMapper.toUser(user, request);

        return userRepository.save(user);
    }
    public void deleteUser(int id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
    public Boolean changePassword(int id, UserChangePassword userChangePassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if(!passwordEncoder.matches(userChangePassword.getOldPassword(), user.getPassword())){
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        user.setPassword(passwordEncoder.encode(userChangePassword.getNewPassword()));
        userRepository.save(user);
        return true;
    }
    public void blockUser(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        user.setStatus("block");
        userRepository.save(user);
    }

}
