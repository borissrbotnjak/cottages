package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.*;
import com.isa.cottages.Repository.UserRequestRepository;
import com.isa.cottages.Service.UserRequestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRequestServiceImpl implements UserRequestService {

    private UserRequestRepository requestRepository;
    @Override
    public List<UserRequest> findNotEnabled() throws Exception
    {
        return this.requestRepository.findUserRequestByEnabledIsFalse();
    }
    @Override
    public UserRequest findById(Long id) throws Exception {
        if (this.requestRepository.findById(id).isEmpty()) {
            throw new Exception("No such value(Complaint service)");
        }
        return this.requestRepository.findById(id).get();
    }

    @Override
    public UserRequest update(UserRequest userRequest) {
        UserRequest c = this.requestRepository.getById(userRequest.getId());
        c.setFirstName(userRequest.getFirstName());
        c.setLastName(userRequest.getLastName());
        c.setEmail(userRequest.getEmail());
        c.setEnabled(userRequest.getEnabled());
        c.setExplanationOfRegistration(userRequest.getExplanationOfRegistration());

        return this.requestRepository.save(c);

    }

    @Override
    public void delete(UserRequest userRequest) {

         this.requestRepository.delete(userRequest);

    }
}
