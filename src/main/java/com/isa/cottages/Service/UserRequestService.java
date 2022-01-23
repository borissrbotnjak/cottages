package com.isa.cottages.Service;

import com.isa.cottages.Model.UserRequest;

import java.util.List;

public interface UserRequestService {
    List<UserRequest> findNotEnabled() throws Exception;

    UserRequest findById(Long id) throws Exception;

    UserRequest update(UserRequest userRequest);

    void delete(UserRequest userRequest);
}
