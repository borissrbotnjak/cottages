package com.isa.cottages.Repository;


import com.isa.cottages.Model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    @Query(value = "SELECT * FROM user_request ur WHERE ur.enabled = FALSE ", nativeQuery = true)
    List<UserRequest> findUserRequestByEnabledIsFalse();
}
