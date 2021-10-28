package com.isa.cottages.Service.impl;

import com.isa.cottages.DTO.ChangePasswordAfterFirstLoginDTO;
import com.isa.cottages.Model.User;
import com.isa.cottages.Repository.UserRepository;
import com.isa.cottages.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User changePasswordAfterFirstLogin(User user, ChangePasswordAfterFirstLoginDTO c) {
        user.setPassword(c.getNewPassword());
        this.userRepository.save(user);
        return user;
    }
}
