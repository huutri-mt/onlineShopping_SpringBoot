package com.example.onlineshopping.service.impl;
import com.example.onlineshopping.dto.Request.UserChangePassword;
import com.example.onlineshopping.dto.Request.UserCreationRequest;
import com.example.onlineshopping.dto.Request.UserUpdateRequest;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Primary
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User creatUser(UserCreationRequest request) {
        User user = new User();
        if(userRepository.existsUserByEmail(request.getEmail())){
            throw new RuntimeException("Email da ton tai");
        }
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFullname(request.getFullname());
        user.setStatus(request.getStatus());
        user.setRole(request.getRole());
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById (int id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay User"));
    }


    public User updateUser(UserUpdateRequest request, int id) {
        User user = getUserById(id);

        if(userRepository.existsUserByEmail(request.getEmail())){
            throw new RuntimeException("Email da ton tai");
        }
        else {
            user.setEmail(request.getEmail());
        }
        user.setFullname(request.getFullname());
        user.setStatus(request.getStatus());
        return userRepository.save(user);
    }
    public void deleteUser(int id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
    public Boolean changePassword(int id, UserChangePassword userChangePassword) {
        User user = getUserById(id);

        if(!passwordEncoder.matches(userChangePassword.getOldPassword(), user.getPassword())){
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        user.setPassword(passwordEncoder.encode(userChangePassword.getNewPassword()));
        userRepository.save(user);

        return true;
    }
}
