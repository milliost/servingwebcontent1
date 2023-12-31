package com.example.servingwebcontent.service.impl;

import com.example.servingwebcontent.entity.Role;
import com.example.servingwebcontent.entity.User;
import com.example.servingwebcontent.repository.RoleRepository;
import com.example.servingwebcontent.repository.UserRepository;
import com.example.servingwebcontent.service.UserDTOService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class UserDTOServiceImpl implements UserDTOService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public void addWin() {
        String userName =SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findByName(userName);
        user.setWins(user.getWins()+1);
        userRepository.save(user);

    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}
