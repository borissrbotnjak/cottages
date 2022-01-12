package com.isa.cottages.Service;

import com.isa.cottages.Model.Client;

public interface ClientService {

    Client findById(Long id) throws Exception;
    Client findByEmail(String email) throws Exception;
    Client getCurrentClient() throws Exception;

    Client updateProfile(Client client) throws Exception;
    Client updateBasicInfo(Client client, Client forUpdate);
    Client update(Client client) throws Exception;

    Double getDiscount() throws Exception;
}
