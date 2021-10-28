package com.isa.cottages.Service;

import com.isa.cottages.DTO.ChangePasswordAfterFirstLoginDTO;
import com.isa.cottages.Model.User;

public interface UserService {

    User changePasswordAfterFirstLogin(User user, ChangePasswordAfterFirstLoginDTO c);


    User findByEmail(String email);
}
