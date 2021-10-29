package com.isa.cottages.Service.impl;

import com.isa.cottages.DTO.ChangePasswordAfterFirstLoginDTO;
import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.UserRepository;
import com.isa.cottages.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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

    @Override
    public User findById(Long id) throws AccessDeniedException {
        User u = userRepository.findById(id).orElseGet(null);
        return u;
    }

    @Override
    public User findByEmail(String email) {
        User u = userRepository.findByEmail(email);
        return u;
    }

    @Override
    public CottageOwner saveCottageOwner(UserRequest userRequest){
        CottageOwner co = new CottageOwner();
        co.setEnabled(false);
        co.setEmail(co.getEmail());
        co.setPassword(co.getPassword());
        co.setFirstName(co.getFirstName());
        co.setLastName(co.getLastName());
        co.setResidence(co.getResidence());
        co.setCity(co.getCity());
        co.setState(co.getState());
        co.setPhoneNumber(co.getPhoneNumber());
        co.setRegistrationType(co.getRegistrationType());
        co.setExplanationOfRegistration(co.getExplanationOfRegistration());

        co.setUserRole(UserRole.COTTAGE_OWNER);

        this.userRepository.save(co);
        return co;
    }

    @Override
    public SystemAdministrator saveSystemAdmin(SystemAdministrator systemAdministrator){
        systemAdministrator.setEnabled(true);
        systemAdministrator.setPassword(systemAdministrator.getPassword());
        systemAdministrator.setUserRole(UserRole.SYS_ADMIN);

        this.userRepository.save(systemAdministrator);
        return systemAdministrator;
    }

}
