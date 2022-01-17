package com.isa.cottages.Service;

import com.isa.cottages.Model.Client;

import java.time.LocalDateTime;
import java.util.Set;

public interface ClientService {

    Client findById(Long id) throws Exception;
    Client findByEmail(String email) throws Exception;

    Client updateProfile(Client client) throws Exception;
    Client updateBasicInfo(Client client, Client forUpdate);

    Set<Client> findAllAvailable_Boat (Long oid) throws Exception;
    Set<Client> findAllAvailable_Cottage (Long oid) throws Exception;
}
