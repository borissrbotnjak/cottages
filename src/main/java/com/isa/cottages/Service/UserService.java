package com.isa.cottages.Service;

import com.isa.cottages.DTO.ChangePasswordAfterFirstLoginDTO;
import com.isa.cottages.Model.SystemAdministrator;
import com.isa.cottages.Model.User;
import com.isa.cottages.Model.UserRequest;

public interface UserService {

    User changePasswordAfterFirstLogin(User user, ChangePasswordAfterFirstLoginDTO c);

    User findById(Long id);

    User findByEmail(String email);

    User saveCottageOwner(UserRequest userRequest);

    User saveSystemAdmin(SystemAdministrator systemAdministrator);

    String buildEmail(String name, String link);
}


